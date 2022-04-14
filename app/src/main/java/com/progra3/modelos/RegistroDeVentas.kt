package com.progra3.modelos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.sql.Time

data class RegistroDeVentas(
    @Expose
    @SerializedName("id")var id:Long,
    @Expose
    @SerializedName("fechaVenta")var fechaVenta:Date,
    @Expose
    @SerializedName("horaVenta")var horaVenta:Time,
    @Expose
    @SerializedName("montoVenta")var montoVenta:Double,
    @Expose
    @SerializedName("cantidadArticulosVendidos")var cantidadArticulosVendidos:Int
)
