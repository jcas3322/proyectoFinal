package com.progra3.Interfaces

import com.progra3.modelos.RegistroDeVentas
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServiciosRestRegistroVentas {
    @GET("/RegistroDeVentas/maxid")
    fun getMaxIdRegistroVentas ():Call<RegistroDeVentas>

    @POST("/RegistroDeVentas/agregar")
    fun addVenta(@Body venta:RegistroDeVentas):Call<RegistroDeVentas>
}