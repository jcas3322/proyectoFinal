package com.progra3.puntoventa

import android.provider.BaseColumns

class ContratoBaseDeDatos {
    companion object{
        val VERSION=1
        class BaseDatos:BaseColumns{
            companion object{
                val NOMBRE_DB="TIENDA"
            }
        }
        class Articulos:BaseColumns{
            companion object{
                val NOMBRE_TABLA="Articulos"
                val COLUMNA_ID="id_articulo"
                val COLUMNA_CODIGO_ARTICULO="codigo_articulo"
                val COLUMNA_NOMBRE_ARTICULO="nombre_articulo"
                val COLUMNA_PRECIO_COMPRA="precio_compra"
                val COLUMNA_PRECIO_VENTA="precio_venta"
                val COLUMNA_URL_IMAGEN="url_imagen"
            }
        }
        class Inventario:BaseColumns{
            companion object{
                val NOMBRE_TABLA="Inventario"
                val COLUMNA_ID_INVENTARIO="id_inventario"
                val COLUMNA_ID_ARTICULO="id_articulo"
                val COLUMNA_STOCK_ACTUAL="stock_actual"
            }
        }
        class RegistroDeVentas:BaseColumns{
            companion object{
                val NOMBRE_TABLA="Registro_de_Ventas"
                val COLUMNA_ID_VENTA="id_venta"
                val COLUMNA_FECHA_VENTA="fecha_venta"
                val COLUMNA_HORA_VENTA="hora_venta"
                val COLUMNA_MONTO_VENTA="monto_de_venta"
                val COLUMNA_TOTAL_ARTICULOS_VENDIDOS="total_articulos_vendidos"
            }
        }
        class ArticulosVendidos:BaseColumns{
            companion object{
                val NOMBRE_TABLA="Articulos_Vendidos"
                val COLUMNA_ID_REGISTRO="id_registro"
                val COLUMNA_ID_VENTA="id_venta"
                val COLUMNA_ID_ARTICULO="id_articulo"
                val COLUMNA_CANTIDAD_VENTA="cantidad_venta"
            }
        }
        class RegistroIngresoDeArticulos:BaseColumns{
            companion object{
                val NOMBRE_TABLA="Registro_Ingreso_de_Articulos"
                val COLUMNA_ID_INGRESO="id_ingreso"
                val COLUMNA_FECHA_INGRESO="fecha_ingreso"
                val COLUMNA_HORA_INGRESO="hora_ingreso"
                val COLUMNA_TOTAL_ARTICULOS_INGRESADOS="total_articulos_ingresados"
            }
        }
        class ArticulosIngresados:BaseColumns{
            companion object{
                val NOMBRE_TABLA="Articulos_Ingresados"
                val COLUMNA_ID_INGRESO="id_ingreso"
                val COLUMNA_ID_ARTICULO="id_articulo"
                val COLUMNA_CANTIDAD_INGRESO="cantidad_ingreso"
                val COLUMNA_FECHA_INGRESO="fecha_ingreso"
                val COLUMNA_HORA_INGRESO="hora_ingreso"
            }
        }
    }
}