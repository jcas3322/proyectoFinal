package com.progra3.puntoventa

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList

class CRUD(context: Context) {
    private var helper:DataBaseHelper?=null
    private var context:Context?=null
    private var db:SQLiteDatabase?=null

    init{
        this.helper= DataBaseHelper(context)
        this.context=context
        this.db=helper?.writableDatabase
        db?.execSQL("PRAGMA foreign_keys = ON")
    }

    fun cargarDatos():ArrayList<Elementos>{
        val dbArticulos:SQLiteDatabase=helper?.readableDatabase!!
        val dbInventario:SQLiteDatabase=helper?.readableDatabase!!
        var elementos:ArrayList<Elementos> = ArrayList()

        val cArticulos:Cursor=dbArticulos.rawQuery("select * from " +
        ContratoBaseDeDatos.Companion.Articulos.NOMBRE_TABLA,null)

        while(cArticulos.moveToNext()){
            var id_Art:Int= cArticulos.getInt(cArticulos.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_ID))
            val cInventario:Cursor=dbInventario.rawQuery("select "+ ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL +
            " from "+ContratoBaseDeDatos.Companion.Inventario.NOMBRE_TABLA+ " where " + ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_ARTICULO+
            " =?", arrayOf(id_Art.toString()))

            if(cInventario.count >0) {
                cInventario.moveToFirst()
                elementos.add(
                    Elementos(
                        cArticulos.getInt(cArticulos.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_ID)),
                        cArticulos.getString(cArticulos.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_CODIGO_ARTICULO)),
                        cArticulos.getString(cArticulos.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_NOMBRE_ARTICULO)),
                        cArticulos.getDouble(cArticulos.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_PRECIO_COMPRA)),
                        cArticulos.getDouble(cArticulos.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_PRECIO_VENTA)),
                        cArticulos.getString(cArticulos.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_URL_IMAGEN)),
                        cInventario.getInt(cInventario.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL))
                    )
                )
            }else{
                Toast.makeText(context,"ERROR STOCKS NO ENCONTRADOS",Toast.LENGTH_SHORT).show()
            }
        }

        return elementos
    }

//Ingresar nuevo Articulo a BD, automaticamente crea el registro en inventario
    /***
     * @param articulos es el arreglo de elemenos
     * @param stock es la cantidad de articulos a ingresar
     */
    fun nuevoArticulo(articulos: Articulos, stock:Int):Boolean{

        try {
    val db: SQLiteDatabase = helper?.writableDatabase!!
    val values = ContentValues()
    //mapeando valores de insercion---------------------------------------------------
    values.put(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_ID,
    articulos.id_articulo)
    values.put(
        ContratoBaseDeDatos.Companion.Articulos.COLUMNA_CODIGO_ARTICULO,
        articulos.codigo_articulo
    )
    values.put(
        ContratoBaseDeDatos.Companion.Articulos.COLUMNA_NOMBRE_ARTICULO,
        articulos.nombre_articulo
    )
    values.put(
        ContratoBaseDeDatos.Companion.Articulos.COLUMNA_PRECIO_COMPRA,
        articulos.precio_compra
    )
    values.put(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_PRECIO_VENTA, articulos.precio_venta)
            values.put(ContratoBaseDeDatos.Companion.Articulos.COLUMNA_URL_IMAGEN,articulos.url_imagen)
    var newRowId = db.insert(ContratoBaseDeDatos.Companion.Articulos.NOMBRE_TABLA, null, values)

    values.clear() //limpiamos values para realizar nueva insercion de datos
    values.put(ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_ARTICULO, articulos.id_articulo)
    values.put(ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL, stock)
    newRowId = db.insert(ContratoBaseDeDatos.Companion.Inventario.NOMBRE_TABLA, null, values)

//Ingresado registro de nuevo ingreso de STOCK-------------------------------------------------------------
    var itemm=ArticulosIngresados(10,articulos.id_articulo,stock,Date().toString(),Date().toString())
    ingresarStock(itemm)
    db.close()
}catch (e:Exception){
    Toast.makeText(context,"Error al escribir articulo",Toast.LENGTH_SHORT).show()
    return false
}
        Toast.makeText(context,"Registro de nuevo ARTICULO ingresado Exitosamente",Toast.LENGTH_SHORT).show()
    return true
    }

    //Ingreso de existencia de mercancia existente,
    //Se le debe de enviar el ID del articulo
    fun ingresarStock(item_ingreso:ArticulosIngresados):Boolean {
        val db: SQLiteDatabase = helper?.writableDatabase!!
        val values = ContentValues()
        try {
            //mapeando insert---------------------------------------------------------------------
            values.put(
                ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_ID_ARTICULO,
                item_ingreso.id_articulo
            )
            values.put(
                ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_CANTIDAD_INGRESO,
                item_ingreso.cantidad_ingreso
            )
            values.put(
                ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_FECHA_INGRESO,
                item_ingreso.fecha_ingreso
            )
            values.put(
                ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_HORA_INGRESO,
                item_ingreso.hora_ingreso
            )
            var newRowId = db.insert(
                ContratoBaseDeDatos.Companion.ArticulosIngresados.NOMBRE_TABLA,
                null,
                values
            )

        }catch (e:Exception){
            Toast.makeText(context,"Error durante ingreso de nuevo stock",Toast.LENGTH_SHORT).show()
            return false
        }
        Toast.makeText(context,"Registro de Stock ingresado con Exito",Toast.LENGTH_SHORT).show()
        return true
    }

//Realiza el UPDATE de INVANTARIO despues de registrar ingreso de stock de articulos
    fun actualizarStockInventario(item_ingreso:ArticulosIngresados):Boolean{

        val db:SQLiteDatabase=helper?.writableDatabase!!
        val values=ContentValues()
        try {
            ingresarStock(item_ingreso)
            var stock_actual=0
            val dbAux:SQLiteDatabase=helper?.readableDatabase!!
            val c:Cursor=dbAux.rawQuery("Select " + ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL +
            " from " + ContratoBaseDeDatos.Companion.Inventario.NOMBRE_TABLA + " where " +
            ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_ARTICULO + " =?", arrayOf(item_ingreso.id_articulo.toString()))

            if(c.count > 0) {
                c.moveToFirst()
                stock_actual =
                    c.getInt(c.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL))

                stock_actual += item_ingreso.cantidad_ingreso

                values.clear()
                values.put(
                    ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL,
                    stock_actual
                )

                db.update(
                    ContratoBaseDeDatos.Companion.Inventario.NOMBRE_TABLA,
                    values,
                    ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_ARTICULO + " =?",
                    arrayOf(item_ingreso.id_articulo.toString())
                )
                db.close()
            }else{
                Toast.makeText(context,"ERROR DURANTE ACTUALIZACION STOCK EN INVENTARIO",Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            Toast.makeText(context,"Error al escribir Ingreso de Stock",Toast.LENGTH_SHORT).show()
            return false
        }
    Toast.makeText(context,"Actualizacion de Inventario EXITOSA",Toast.LENGTH_SHORT).show()
        return true
    }
//Realizar Ventas, un registro por Registro de Ventas y varios registros por articulos separados-------
    //IMPORTANTE!!!!!! SE LE DEBE DE PASAR EL REGISTRO DE ID_VENTA A REGISTRO DE VENTAS MANUALMENTE
    //INCREMENTAR ID_VENTA MANUALMENTE
    //DEBE DE VENIR RECTIFICADO QUE LA CANTIDAD DE VENTA DE CADA ARTICULO COINCIDA CON EL STOCK ACTUAL
    fun realizarVentas(item_venta:RegistroDeVentas,item_articulo:ArrayList<ArticulosVendidos>):Boolean{
        try {
//PENDIENTE DE REVISAR CODIGO---------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------
        //---------------------------------------------------------------------------------------------------
            val db:SQLiteDatabase=helper?.writableDatabase!!
            val values=ContentValues()
            val dbAux:SQLiteDatabase=helper?.readableDatabase!!

            values.put(ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_ID_VENTA,item_venta.id_venta)
            values.put(ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_FECHA_VENTA,item_venta.fecha_venta)
            values.put(ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_HORA_VENTA,item_venta.hora_venta)
            values.put(ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_MONTO_VENTA,item_venta.monto_de_venta)
            values.put(ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_TOTAL_ARTICULOS_VENDIDOS,item_venta.total_articulos_vendidos)
            var newRowId=db.insert(ContratoBaseDeDatos.Companion.RegistroDeVentas.NOMBRE_TABLA,null,values)

            //MAPEANDO ARTICULOS VENDIDOS
            values.clear()
            for (item in item_articulo){
                values.put(ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_ID_VENTA,item.id_venta)
                values.put(ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_ID_ARTICULO,item.id_articulo)
                values.put(ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_CANTIDAD_VENTA,item.cantidad_venta)
                newRowId=db.insert(ContratoBaseDeDatos.Companion.ArticulosVendidos.NOMBRE_TABLA,null,values)
                values.clear()
                var venta_realizada:Int=item.cantidad_venta

                val c:Cursor=dbAux.rawQuery("Select " + ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL +
                " from " + ContratoBaseDeDatos.Companion.Inventario.NOMBRE_TABLA + " where "+ ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_ARTICULO +
                " =?", arrayOf(item.id_articulo.toString()))

                c.moveToFirst()
                venta_realizada-=c.getInt(c.getColumnIndexOrThrow(ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL))

                values.put(ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL,venta_realizada)

                db.update(ContratoBaseDeDatos.Companion.Inventario.NOMBRE_TABLA,values,ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_ARTICULO +
                " =?", arrayOf(item.id_articulo.toString()))
                values.clear()
            }
        }catch (e:Exception){
            Toast.makeText(context,"Error durante la Venta "+e.message,Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}