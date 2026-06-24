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
import com.uesopeneel.minervaapp.network.model.EvaluacionDataItem
import com.uesopeneel.minervaapp.ui.theme.*

@Composable
fun EvaluacionCard(evalItem: EvaluacionDataItem) {
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
