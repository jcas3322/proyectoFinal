package com.progra3.modelos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArticulosIngresados(
    @Expose
    @SerializedName("id")var id:Long?,
    @Expose
    @SerializedName("idArticulo")var idArticulo:Long,
    @Expose
    @SerializedName("cantidadIngreso")var cantidadIngreso:Int,
    @Expose
    @SerializedName("fechaIngreso")var fechaIngreso:String?
)
