package com.uesopeneel.minervaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uesopeneel.minervaapp.network.model.RecordItem
import com.uesopeneel.minervaapp.ui.theme.*

@Composable
fun RecordNotasContent(recordNotas: List<RecordItem>) {
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
fun RecordMateriaCard(record: RecordItem) {
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
