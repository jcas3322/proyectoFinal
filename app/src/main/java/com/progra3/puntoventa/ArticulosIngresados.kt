package com.progra3.puntoventa

import java.util.*

class ArticulosIngresados(id_ingreso:Int,id_articulo:Int,cantidad_ingreso:Int,fecha_ingreso:String,hora_ingreso:String) {

    var id_ingreso:Int=0
    var id_articulo:Int=0
    var cantidad_ingreso:Int=0
    var fecha_ingreso:String= Date().toString()
    var hora_ingreso:String= Date().toString()
    init {
        this.id_ingreso=id_ingreso
        this.id_articulo=id_articulo
        this.cantidad_ingreso=cantidad_ingreso
        this.fecha_ingreso=fecha_ingreso
        this.hora_ingreso=hora_ingreso
    }
}