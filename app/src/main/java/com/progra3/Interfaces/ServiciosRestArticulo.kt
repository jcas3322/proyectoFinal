package com.progra3.Interfaces

import com.progra3.modelos.Articulo
import com.progra3.modelos.ArticulosIngresados
import com.progra3.modelos.ArticulosVendidos
import com.progra3.modelos.RegistroDeVentas
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ServiciosRestArticulo {

    @GET("/maxid")
    fun getMaxIdArticulos():Call<Articulo>

    @GET("/all")
    fun getAllArticulos():Call<List<Articulo>>

    @GET("/buscarPor/{nombre}")
    fun getLikeAsArticulo(@Path("nombre")nombre:String):Call<List<Articulo>>

    @GET
    fun getRegistroDeVentas(@Url ruta: String):Call<List<RegistroDeVentas>>
}