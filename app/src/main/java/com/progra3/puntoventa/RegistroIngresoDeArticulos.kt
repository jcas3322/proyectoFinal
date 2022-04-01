package com.progra3.puntoventa

import java.util.*

class RegistroIngresoDeArticulos(id_ingreso:Int,fecha_ingreso:Date,hora_ingreso:Date,total_articulos_ingresado:Int) {
    var id_ingreso:Int=0
    var fecha_ingreso:Date=Date()
    var hora_ingreso:Date=Date()
    var total_articulos_ingresado:Int=0
    init {
        this.id_ingreso=id_ingreso
        this.fecha_ingreso=fecha_ingreso
        this.hora_ingreso=hora_ingreso
        this.total_articulos_ingresado=total_articulos_ingresado
    }
}