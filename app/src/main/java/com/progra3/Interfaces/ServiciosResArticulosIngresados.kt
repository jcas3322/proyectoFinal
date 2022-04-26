package com.progra3.Interfaces

import com.progra3.modelos.ArticulosIngresados
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiciosResArticulosIngresados {
    @POST("/articulosIngresados/agregar")
    fun createIngresoArticulo(@Body articulo: ArticulosIngresados): Call<ArticulosIngresados>

    @GET("/articulosIngresados/buscarFecha/{fecha}")
    fun buscarIngresoArticulos(@Path("fecha")fecha:String):Call<ArrayList<ArticulosIngresados>>

}