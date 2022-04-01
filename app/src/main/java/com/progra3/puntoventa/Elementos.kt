package com.progra3.puntoventa

class Elementos(id_elemento:Int,codigo:String,nombre:String,precio_compra:Double,precio_venta:Double,url_imagen:String,stock:Int) {
    var id_elemento:Int=0
    var codigo:String=""
    var nombre:String=""
    var precio_compra:Double=0.0
    var precio_venta:Double=0.0
    var stock:Int=0
    var url_imagen:String=""

    init {
        this.id_elemento=id_elemento
        this.codigo=codigo
        this.nombre=nombre
        this.precio_compra=precio_compra
        this.precio_venta=precio_venta
        this.url_imagen=url_imagen
        this.stock=stock
    }
}