package com.uesopeneel.minervaapp.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DocumentosResponse(
    val data: List<CuentaDocumento>?,
    val message: String?,
    val status: String?
)

@JsonClass(generateAdapter = true)
data class CuentaDocumento(
    val id_documento: Long?,
    val documento: DocumentoItem?
)

@JsonClass(generateAdapter = true)
data class DocumentoItem(
    val id: Long?,
    val documento_tipo: DocumentoTipo?
)

@JsonClass(generateAdapter = true)
data class DocumentoTipo(
    val nombre: String?
)

@JsonClass(generateAdapter = true)
data class FotografiaBase64Response(
    val data: String?,
    val message: String?,
    val status: String?
)

@JsonClass(generateAdapter = true)
data class PersonaInfoResponse(
    val data: PersonaInfoData?,
    val message: String?,
    val status: String?
)

@JsonClass(generateAdapter = true)
data class PersonaInfoData(
    val persona: PersonaDetalle?
)

@JsonClass(generateAdapter = true)
data class PersonaDetalle(
    val nombre: String?,
    val apellido: String?,
    val dui: String?,
    val nit: String?,
    val nacimiento: String?,
    val apellidoCompleto: String?
)
