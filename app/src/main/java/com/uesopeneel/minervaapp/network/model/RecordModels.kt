package com.uesopeneel.minervaapp.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecordNotasResponse(
    val data: List<RecordPlanData>?,
    val message: String?,
    val status: String?
)

@JsonClass(generateAdapter = true)
data class RecordPlanData(
    val record: List<RecordItem>?
)

@JsonClass(generateAdapter = true)
data class RecordItem(
    val pensum: RecordPensum?,
    val materia: RecordMateria?,
    val expediente: RecordExpediente?,
    val periodoCiclo: RecordPeriodoCiclo?,
    val pensumTipo: RecordPensumTipo?
)

@JsonClass(generateAdapter = true)
data class RecordPensum(
    val id: Int?,
    val codigo: String?,
    val uv: Int?,
    val materia: RecordMateria?
)

@JsonClass(generateAdapter = true)
data class RecordMateria(
    val id: Int?,
    val nombre: String?
)

@JsonClass(generateAdapter = true)
data class RecordExpediente(
    val id: Int?,
    val nota_final: Double?,
    val estado: String?,
    val tipo_curso: String?
)

@JsonClass(generateAdapter = true)
data class RecordPeriodoCiclo(
    val id: Int?,
    val anho: Int?,
    val ciclo: String?
)

@JsonClass(generateAdapter = true)
data class RecordPensumTipo(
    val id: Int?,
    val codigo: String?,
    val nombre: String?
)
