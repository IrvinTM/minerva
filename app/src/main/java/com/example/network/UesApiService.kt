package com.example.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface UesApiService {
    @GET("main/institucion/facultadPlanEstudio")
    suspend fun getFacultadPlanEstudio(
        @Header("K-TOKEN") kToken: String,
        @Header("X-XSRF-TOKEN") xsrfToken: String
    ): Response<FacultadResponse>

    @GET("periodo/periodoCiclo/ciclosEstudio")
    suspend fun getCiclosEstudio(
        @Header("K-TOKEN") kToken: String,
        @Header("X-XSRF-TOKEN") xsrfToken: String
    ): Response<CiclosResponse>

    @GET("main/nota/materias/{id_periodo}")
    suspend fun getMaterias(
        @Header("K-TOKEN") kToken: String,
        @Header("X-XSRF-TOKEN") xsrfToken: String,
        @Path("id_periodo") idPeriodo: Int
    ): Response<MateriasResponse>

    companion object {
        private const val BASE_URL = "https://mimas.ues.edu.sv/"

        fun create(): UesApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (X11; CrOS x86_64 14541.0.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36")
                        .header("APP", "Febe-fQtuE-rcZMj8-C7qpAZdtHJJbU1nbVZyWTNlTkxXYXNxcHBZSWc9PQ==")
                        .header("Accept", "application/json, text/plain, */*")
                        .header("Origin", "https://eel.ues.edu.sv")
                        .header("PLAN-ESTUDIO", "3576")
                        .header("PLATFORM", "web")
                        .header("X-CLIENT", "academics-febe")
                        .build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()

            val moshi = com.squareup.moshi.Moshi.Builder()
                .addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create(UesApiService::class.java)
        }
    }
}
