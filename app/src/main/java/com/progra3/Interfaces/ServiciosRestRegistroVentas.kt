package com.progra3.Interfaces

import com.progra3.modelos.RegistroDeVentas
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiciosRestRegistroVentas {
    @GET("/RegistroDeVentas/maxid")
    fun getMaxIdRegistroVentas ():Call<RegistroDeVentas>

    @GET("/RegistroDeVentas/buscarFecha/{fecha}")
    fun getRegistroFecha(@Path("fecha")fecha:String):Call<ArrayList<RegistroDeVentas>>

    @POST("/RegistroDeVentas/agregar")
    fun addVenta(@Body venta:RegistroDeVentas):Call<RegistroDeVentas>
}