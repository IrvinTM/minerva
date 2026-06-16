package com.example.ui.screens

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

  var showLogoutMenu by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
            if (isExpedienteOpen) {
                Text("Expediente", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
            }
        },
        navigationIcon = {
            if (isExpedienteOpen) {
                IconButton(onClick = { viewModel.closeExpediente() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            }
        },
        actions = {
          Box {
              IconButton(onClick = { showLogoutMenu = true }) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White
                  )
                }
              }
              DropdownMenu(
                  expanded = showLogoutMenu,
                  onDismissRequest = { showLogoutMenu = false }
              ) {
                  DropdownMenuItem(
                      text = { Text("Cerrar Sesión") },
                      onClick = {
                          showLogoutMenu = false
                          viewModel.handleSignOut()
                      },
                      leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = null) }
                  )
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
        if (!isExpedienteOpen) {
            // Main clean screen with just one option
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
                        fontWeight = FontWeight.Bold,
                        color = MinervaNavy
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    onClick = { viewModel.openExpediente() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MinervaBlue),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = "Expediente",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Expediente",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        } else {
            // Expediente content
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MinervaBlue)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
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
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Notas Parciales",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MinervaGrayDark
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(materias) { materiaItem ->
                        MateriaCard(materiaItem = materiaItem)
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
        }
    }
  }
}

@Composable
fun MateriaCard(materiaItem: com.example.network.MateriaItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        color = MinervaGrayDark
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
                            color = MinervaGrayDark,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}
