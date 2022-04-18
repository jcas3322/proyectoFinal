package com.progra3.Interfaces

import com.progra3.modelos.ArticulosVendidos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiciosRestArticulosVendidos {
    @POST("/ArticulosVendidos/agregar")
    fun addArticulosVendidos(@Body venta:ArticulosVendidos):Call<ArticulosVendidos>
}