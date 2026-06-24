package com.uesopeneel.minervaapp.ui.screens

import android.util.Base64
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uesopeneel.minervaapp.ui.components.*
import com.uesopeneel.minervaapp.ui.theme.*
import com.uesopeneel.minervaapp.ui.viewmodel.PortalViewModel
import com.uesopeneel.minervaapp.utils.decodeBase64ToBitmap
import kotlinx.coroutines.launch

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
