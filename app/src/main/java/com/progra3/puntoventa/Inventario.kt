package com.progra3.puntoventa

class Inventario(id_inventario:Int,id_articulo:Int,stock_actual:Int) {
    var id_inventario:Int=0
    var id_articulo:Int=0
    var stock_actual:Int=0
    init {
        this.id_articulo=id_articulo
        this.id_inventario=id_inventario
        this.stock_actual=stock_actual
    }
}