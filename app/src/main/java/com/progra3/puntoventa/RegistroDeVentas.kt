package com.progra3.puntoventa

import java.util.*

class RegistroDeVentas(id_venta:Int,fecha_venta:String,hora_venta:String,monto_de_venta:Double,total_articulos_vendidos:Int) {
    var id_venta:Int=0
    var fecha_venta:String=Date().toString()
    var hora_venta:String=Date().toString()
    var monto_de_venta:Double=0.0
    var total_articulos_vendidos:Int=0
    init {
        this.id_venta=id_venta
        this.fecha_venta=fecha_venta
        this.hora_venta=hora_venta
        this.monto_de_venta=monto_de_venta
        this.total_articulos_vendidos=total_articulos_vendidos
    }
}