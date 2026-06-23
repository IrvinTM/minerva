package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.PortalViewModel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.History
import kotlinx.coroutines.launch

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
  viewModel: PortalViewModel,
  modifier: Modifier = Modifier
) {
  val studentName by viewModel.studentName.collectAsState()
  val isExpedienteOpen by viewModel.isExpedienteOpen.collectAsState()
  val isLoading by viewModel.isLoadingExpediente.collectAsState()
  val materias by viewModel.materias.collectAsState()
  val facultad by viewModel.facultad.collectAsState()
  val selectedMateria by viewModel.selectedMateria.collectAsState()
  val isPerfilOpen by viewModel.isPerfilOpen.collectAsState()
  val profilePhotoBase64 by viewModel.profilePhotoBase64.collectAsState()
  val expedienteTab by viewModel.expedienteTab.collectAsState()
  val recordNotas by viewModel.recordNotas.collectAsState()
  val isLoadingRecord by viewModel.isLoadingRecord.collectAsState()

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()

  var showLogoutMenu by remember { mutableStateOf(false) }

  ModalNavigationDrawer(
    drawerState = drawerState,
    gesturesEnabled = !isExpedienteOpen && selectedMateria == null && !isPerfilOpen,
    drawerContent = {
        ModalDrawerSheet {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Menú", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider()
            NavigationDrawerItem(
                label = { Text("Inicio") },
                selected = !isExpedienteOpen && !isPerfilOpen && selectedMateria == null,
                onClick = { 
                    coroutineScope.launch { drawerState.close() }
                    viewModel.closeExpediente()
                    viewModel.closePerfil()
                },
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            NavigationDrawerItem(
                label = { Text("Expediente") },
                selected = isExpedienteOpen,
                onClick = { 
                    coroutineScope.launch { drawerState.close() }
                    viewModel.closePerfil()
                    viewModel.openExpediente()
                },
                icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            NavigationDrawerItem(
                label = { Text("Perfil") },
                selected = isPerfilOpen,
                onClick = { 
                    coroutineScope.launch { drawerState.close() }
                    viewModel.closeExpediente()
                    viewModel.openPerfil()
                },
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            HorizontalDivider()
            NavigationDrawerItem(
                label = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                selected = false,
                onClick = { 
                    coroutineScope.launch { drawerState.close() }
                    viewModel.handleSignOut()
                },
                icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
  ) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
              if (selectedMateria != null) {
                  Text("Detalle de Notas", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
              } else if (isExpedienteOpen) {
                  Text("Expediente", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
              } else if (isPerfilOpen) {
                  Text("Perfil", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
              } else {
                  Text("Inicio", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
              }
          },
          navigationIcon = {
              if (selectedMateria != null) {
                  IconButton(onClick = { viewModel.clearSelectedMateria() }) {
                      Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a Expediente", tint = Color.White)
                  }
              } else if (isExpedienteOpen) {
                  IconButton(onClick = { viewModel.closeExpediente() }) {
                      Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                  }
              } else if (isPerfilOpen) {
                  IconButton(onClick = { viewModel.closePerfil() }) {
                      Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                  }
              } else {
                  IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                      Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                  }
              }
          },
          actions = {
              Box(
                  modifier = Modifier
                      .padding(end = 16.dp)
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(Color.White.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
              ) {
                  if (profilePhotoBase64 != null) {
                      val bitmap = remember(profilePhotoBase64) { decodeBase64ToBitmap(profilePhotoBase64!!) }
                      if (bitmap != null) {
                          Image(
                              bitmap = bitmap.asImageBitmap(),
                              contentDescription = "Foto de perfil",
                              contentScale = ContentScale.Crop,
                              modifier = Modifier.fillMaxSize()
                          )
                      } else {
                          Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
                      }
                  } else {
                      Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
                  }
              }
          },
          colors = TopAppBarDefaults.topAppBarColors(containerColor = MinervaBlue)
        )
      },
      modifier = modifier.fillMaxSize()
    ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(innerPadding)
    ) {
        if (!isExpedienteOpen && !isPerfilOpen && selectedMateria == null) {
            // Main clean screen with minimalist options
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hola, $studentName",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(48.dp))

                MenuOptionCard(
                    title = "Expediente",
                    icon = Icons.Default.Assignment,
                    onClick = { viewModel.openExpediente() }
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuOptionCard(
                    title = "Perfil",
                    icon = Icons.Default.Person,
                    onClick = { viewModel.openPerfil() }
                )
            }
        } else if (isPerfilOpen) {
            val studentId by viewModel.studentId.collectAsState()
            val personaInfo by viewModel.personaInfo.collectAsState()
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MinervaBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (profilePhotoBase64 != null) {
                        val bitmap = remember(profilePhotoBase64) { decodeBase64ToBitmap(profilePhotoBase64!!) }
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                modifier = Modifier.size(80.dp),
                                tint = MinervaBlue
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(80.dp),
                            tint = MinervaBlue
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = studentName,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = studentId,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                if (personaInfo != null) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                            Text("Información Personal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(16.dp))
                            if (!personaInfo?.dui.isNullOrEmpty()) {
                                ProfileDetailRow("DUI", personaInfo!!.dui!!)
                            }
                            if (!personaInfo?.nit.isNullOrEmpty()) {
                                ProfileDetailRow("NIT", personaInfo!!.nit!!)
                            }
                            if (!personaInfo?.nacimiento.isNullOrEmpty()) {
                                ProfileDetailRow("Fecha de Nacimiento", personaInfo!!.nacimiento!!)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = { viewModel.handleSignOut() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else if (selectedMateria != null) {
            val isLoadingEvals by viewModel.isLoadingEvaluaciones.collectAsState()
            val evaluaciones by viewModel.evaluaciones.collectAsState()
            val materia = selectedMateria!!

            if (isLoadingEvals) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MinervaBlue)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        val codigo = materia.grupoPensum?.pensum?.codigo ?: ""
                        val nombre = materia.grupoPensum?.pensum?.materia?.nombre ?: "Desconocida"
                        
                        Text(
                            text = "$codigo - $nombre",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Nota Final Actual: ${materia.notaFinal}",
                            style = MaterialTheme.typography.titleMedium.copy(color = MinervaBlueDark)
                        )
                        Text(
                            text = "Estado: ${materia.estado}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Evaluaciones Registradas",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(evaluaciones) { eval ->
                        EvaluacionCard(eval)
                    }

                    if (evaluaciones.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text(text = "No hay evaluaciones registradas.", color = MinervaGrayMedium)
                            }
                        }
                    }
                }
            }
        } else {
            // Expediente content with tabs
            Column(modifier = Modifier.fillMaxSize()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MinervaNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = facultad?.institucionPadre?.nombre ?: "",
                            style = MaterialTheme.typography.labelMedium.copy(color = MinervaBlueLight)
                        )
                        Text(
                            text = facultad?.nombre ?: "Facultad desconocida",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val carrera = materias.firstOrNull()?.grupoPensum?.pensum?.planEstudio?.carrera?.nombre ?: "Carrera no especificada"
                        Text(
                            text = carrera,
                            style = MaterialTheme.typography.bodyMedium.copy(color = MinervaBlueLight)
                        )
                    }
                }

                TabRow(
                    selectedTabIndex = expedienteTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MinervaBlue,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Tab(
                        selected = expedienteTab == 0,
                        onClick = { viewModel.setExpedienteTab(0) },
                        text = { Text("Notas Parciales", fontWeight = if (expedienteTab == 0) FontWeight.Bold else FontWeight.Normal) },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = expedienteTab == 1,
                        onClick = { viewModel.setExpedienteTab(1) },
                        text = { Text("Record de Notas", fontWeight = if (expedienteTab == 1) FontWeight.Bold else FontWeight.Normal) },
                        icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }

                if (expedienteTab == 0) {
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MinervaBlue)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(materias) { materiaItem ->
                                MateriaCard(materiaItem = materiaItem, onClick = { viewModel.selectMateria(materiaItem) })
                            }
                            if (materias.isEmpty() && !isLoading) {
                                item {
                                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                        Text(text = "No se encontraron materias inscritas.", color = MinervaGrayMedium)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (isLoadingRecord) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MinervaBlue)
                        }
                    } else {
                        RecordNotasContent(recordNotas = recordNotas)
                    }
                }
            }
        }
    }
  }
}
}

@Composable
fun MateriaCard(materiaItem: com.example.network.MateriaItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val codigo = materiaItem.grupoPensum?.pensum?.codigo ?: "N/D"
                val nombre = materiaItem.grupoPensum?.pensum?.materia?.nombre ?: "Desconocida"
                val uv = materiaItem.grupoPensum?.pensum?.uv ?: 0
                
                Text(
                    text = codigo,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MinervaBlue
                    )
                )
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "$uv U.V. • Estado: ${materiaItem.estado}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MinervaGrayMedium)
                )
            }
            
            Box(
                modifier = Modifier
                    .background(
                        color = MinervaBlueLight.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = materiaItem.notaFinal.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = MinervaBlueDark
                        )
                    )
                    Text(
                        text = "Nota Final",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EvaluacionCard(evalItem: com.example.network.EvaluacionDataItem) {
    val evaluacion = evalItem.nota?.evaluacion
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = evaluacion?.nombre ?: "Evaluación",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "Ponderación: ${evaluacion?.porcentaje ?: 0.0}%",
                    style = MaterialTheme.typography.bodySmall.copy(color = MinervaGrayMedium)
                )
                if (evaluacion?.fecha != null) {
                    Text(
                        text = "Fecha: ${evaluacion.fecha} ${evaluacion.hora ?: ""}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MinervaGrayMedium)
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .background(
                        color = MinervaBlueLight.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = evalItem.nota?.nota?.toString() ?: "-",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MinervaBlueDark
                    )
                )
            }
        }
    }
  }

@Composable
fun RecordNotasContent(recordNotas: List<com.example.network.RecordItem>) {
    val grouped = remember(recordNotas) {
        recordNotas
            .groupBy { item ->
                val pc = item.periodoCiclo
                if (pc != null) "Ciclo ${pc.ciclo} - ${pc.anho}" else "Sin periodo"
            }
            .toSortedMap(compareByDescending { key ->
                val parts = key.replace("Ciclo ", "").split(" - ")
                val anho = parts.getOrNull(1)?.toIntOrNull() ?: 0
                val cicloOrder = when (parts.getOrNull(0)) {
                    "I" -> 0; "P" -> 1; else -> 2
                }
                anho * 10 + cicloOrder
            })
    }

    if (recordNotas.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text(text = "No se encontraron registros.", color = MinervaGrayMedium)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            grouped.forEach { (cicloLabel, items) ->
                item {
                    Text(
                        text = cicloLabel,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MinervaBlue
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(items) { record ->
                    RecordMateriaCard(record)
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun RecordMateriaCard(record: com.example.network.RecordItem) {
    val codigo = record.pensum?.codigo ?: ""
    val nombre = record.materia?.nombre ?: "Materia desconocida"
    val uv = record.pensum?.uv ?: 0
    val nota = record.expediente?.nota_final
    val estado = record.expediente?.estado ?: ""
    val tipoPensum = record.pensumTipo?.nombre ?: ""

    val estadoLabel = when (estado) {
        "AP" -> "Aprobada"
        "RP" -> "Reprobada"
        "EC" -> "En curso"
        "RT" -> "Retirada"
        else -> estado
    }
    val estadoColor = when (estado) {
        "AP" -> Color(0xFF2E7D32)
        "RP" -> Color(0xFFC62828)
        "EC" -> MinervaBlue
        "RT" -> MinervaGrayMedium
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = codigo,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MinervaBlue
                    )
                )
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "$uv UV",
                        style = MaterialTheme.typography.bodySmall.copy(color = MinervaGrayMedium)
                    )
                    Text(
                        text = tipoPensum,
                        style = MaterialTheme.typography.bodySmall.copy(color = MinervaGrayMedium)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(
                            color = estadoColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nota?.let { String.format("%.1f", it) } ?: "-",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = estadoColor
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = estadoLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = estadoColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

fun decodeBase64ToBitmap(base64Str: String): android.graphics.Bitmap? {
    return try {
        android.util.Log.d("ProfilePhoto", "Attempting to decode base64 string, original length: ${base64Str.length}")
        val cleanBase64 = if (base64Str.contains(",")) base64Str.substringAfter(",") else base64Str
        android.util.Log.d("ProfilePhoto", "Clean base64 length: ${cleanBase64.length}")
        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        val bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        if (bmp == null) {
            android.util.Log.e("ProfilePhoto", "BitmapFactory returned null!")
        } else {
            android.util.Log.d("ProfilePhoto", "Bitmap loaded: ${bmp.width}x${bmp.height}")
        }
        bmp
    } catch (e: Exception) {
        android.util.Log.e("ProfilePhoto", "Error decoding base64: ${e.message}")
        e.printStackTrace()
        null
    }
}

@Composable
fun MenuOptionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MinervaBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MinervaBlue,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ir",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

