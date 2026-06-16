package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CicloItem(
    val id: Int,
    @Json(name = "id_periodo") val idPeriodo: Int,
    val anho: Int,
    val ciclo: String,
    val periodo: Periodo?
)

@JsonClass(generateAdapter = true)
data class Periodo(
    val id: Int,
    val inicio: String?,
    val fin: String?
)

@JsonClass(generateAdapter = true)
data class CiclosResponse(
    val data: List<CicloItem>,
    val message: String,
    val status: String
)

@JsonClass(generateAdapter = true)
data class MateriaItem(
    val id: Int,
    @Json(name = "nota_final") val notaFinal: Double,
    val estado: String,
    @Json(name = "grupo_pensum") val grupoPensum: GrupoPensum?
)

@JsonClass(generateAdapter = true)
data class GrupoPensum(
    val pensum: Pensum?
)

@JsonClass(generateAdapter = true)
data class Pensum(
    val codigo: String,
    val uv: Int,
    val materia: Materia?,
    @Json(name = "plan_estudio") val planEstudio: PlanEstudio?
)

@JsonClass(generateAdapter = true)
data class Materia(
    val id: Int,
    val nombre: String
)

@JsonClass(generateAdapter = true)
data class PlanEstudio(
    val carrera: Carrera?
)

@JsonClass(generateAdapter = true)
data class Carrera(
    val nombre: String
)

@JsonClass(generateAdapter = true)
data class MateriasResponse(
    val data: List<MateriaItem>,
    val message: String,
    val status: String
)

@JsonClass(generateAdapter = true)
data class EvaluacionesResponse(
    val data: List<EvaluacionDataItem>,
    val message: String,
    val status: String
)

@JsonClass(generateAdapter = true)
data class EvaluacionDataItem(
    val id: Long,
    val nota: NotaDetalle?
)

@JsonClass(generateAdapter = true)
data class NotaDetalle(
    val id: Long,
    val nota: Double,
    val evaluacion: EvaluacionInfo?
)

@JsonClass(generateAdapter = true)
data class EvaluacionInfo(
    val id: Long,
    val nombre: String,
    val porcentaje: Double,
    val fecha: String?,
    val hora: String?
)

@JsonClass(generateAdapter = true)
data class FacultadResponse(
    val data: FacultadData?,
    val message: String,
    val status: String
)

@JsonClass(generateAdapter = true)
data class FacultadData(
    val nombre: String,
    @Json(name = "institucion_padre") val institucionPadre: InstitucionPadre?
)

@JsonClass(generateAdapter = true)
data class InstitucionPadre(
    val nombre: String
)
