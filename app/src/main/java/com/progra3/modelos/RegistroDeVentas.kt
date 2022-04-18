package com.progra3.modelos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegistroDeVentas(
    @Expose
    @SerializedName("id")var id:Long,
    @Expose
    @SerializedName("fechaVenta")var fechaVenta:String?,
    @Expose
    @SerializedName("horaVenta")var horaVenta:String?,
    @Expose
    @SerializedName("montoVenta")var montoVenta:Double,
    @Expose
    @SerializedName("cantidadArticulosVendidos")var cantidadArticulosVendidos:Int
)
