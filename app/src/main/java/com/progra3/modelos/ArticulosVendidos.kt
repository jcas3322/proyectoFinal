package com.progra3.modelos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArticulosVendidos(
    @Expose
    @SerializedName("id")var id:Long,
    @Expose
    @SerializedName("idVenta")var idVenta:Long,
    @Expose
    @SerializedName("idArticulo")var idArticulo:Long,
    @Expose
    @SerializedName("cantidadVendida")var cantidadVendida:Int
)
