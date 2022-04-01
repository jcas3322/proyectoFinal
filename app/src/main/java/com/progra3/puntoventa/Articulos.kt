package com.progra3.puntoventa

open class Articulos(id_articulo:Int, codigo_articulo:String, nombre_articulo:String, precio_compra:Double, precio_venta:Double, url_imagen:String) {

    var id_articulo:Int=0
    var codigo_articulo:String=""
    var nombre_articulo:String=""
    var precio_compra:Double=0.0
    var precio_venta:Double=0.0
    var url_imagen:String=""

    init {
        this.id_articulo=id_articulo
        this.codigo_articulo=codigo_articulo
        this.nombre_articulo=nombre_articulo
        this.precio_compra=precio_compra
        this.precio_venta=precio_venta
        this.url_imagen=url_imagen
    }
}