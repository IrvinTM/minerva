package com.uesopeneel.minervaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uesopeneel.minervaapp.network.model.MateriaItem
import com.uesopeneel.minervaapp.ui.theme.*

@Composable
fun MateriaCard(materiaItem: MateriaItem, onClick: () -> Unit) {
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
