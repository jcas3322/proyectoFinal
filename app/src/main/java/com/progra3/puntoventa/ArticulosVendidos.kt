package com.progra3.puntoventa

class ArticulosVendidos(id_registro:Int,id_venta:Int,id_articulo:Int,cantidad_venta:Int) {
    var id_registro:Int=0
    var id_venta:Int=0
    var id_articulo=0
    var cantidad_venta:Int=0
    init {
        this.id_registro=id_registro
        this.id_venta=id_venta
        this.id_articulo=id_articulo
        this.cantidad_venta=cantidad_venta
    }
}