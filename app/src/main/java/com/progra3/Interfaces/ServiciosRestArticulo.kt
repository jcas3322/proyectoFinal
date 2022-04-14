package com.progra3.Interfaces

import com.progra3.modelos.Articulo
import com.progra3.modelos.ArticulosIngresados
import com.progra3.modelos.ArticulosVendidos
import com.progra3.modelos.RegistroDeVentas
import retrofit2.Call
import retrofit2.http.*

interface ServiciosRestArticulo {

    @GET("/articulos/maxid")
    fun getMaxIdArticulos():Call<Articulo>

    @GET("/articulos/all")
    fun getAllArticulos():Call<List<Articulo>>

    @GET("/articulos/buscarPor/{nombre}")
    fun getLikeAsArticulo(@Path("nombre")nombre:String):Call<List<Articulo>>

    @POST("/articulos/agregar")
    fun createArticulo(@Body articulo:Articulo):Call<Articulo>

    @PUT("/articulos/actualizar/{id}")
    fun updateArticulo(@Path("id")id:Long,@Body articulo:Articulo):Call<Articulo>

    @DELETE("/articulos/borrar/{id}")
    fun deleteArticulo(@Path("id")id:Long):Call<Void>
}