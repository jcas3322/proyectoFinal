package com.progra3.Interfaces

import com.progra3.modelos.ArticulosIngresados
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiciosResArticulosIngresados {
    @POST("/articulosIngresados/agregar")
    fun createIngresoArticulo(@Body articulo: ArticulosIngresados): Call<ArticulosIngresados>

}