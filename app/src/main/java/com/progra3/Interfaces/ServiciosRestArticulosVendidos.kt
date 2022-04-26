package com.progra3.Interfaces

import com.progra3.modelos.ArticulosVendidos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiciosRestArticulosVendidos {
    @POST("/ArticulosVendidos/agregar")
    fun addArticulosVendidos(@Body venta:ArticulosVendidos):Call<ArticulosVendidos>

    @GET("/ArticulosVendidos/buscarIdVenta/{venta}")
    fun buscarIdVenta(@Path("venta")venta:Long):Call<ArrayList<ArticulosVendidos>>
}