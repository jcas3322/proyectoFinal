package com.progra3.modelos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Articulo(
    @Expose
    @SerializedName("id")var id:Long,
    @Expose
    @SerializedName("codigo")var codigo:String,
    @Expose
    @SerializedName("nombre")var nombre:String,
    @Expose
    @SerializedName("precioCompra")var precioCompra:Double,
    @Expose
    @SerializedName("precioVenta")var precioVenta:Double,
    @Expose
    @SerializedName("stock")var stock:Int,
    @Expose
    @SerializedName("urlImagen")var urlImagen:String
)