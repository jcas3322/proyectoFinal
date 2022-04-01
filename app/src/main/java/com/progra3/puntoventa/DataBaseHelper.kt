package com.progra3.puntoventa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context):SQLiteOpenHelper(context,ContratoBaseDeDatos.Companion.BaseDatos.NOMBRE_DB,null,ContratoBaseDeDatos.VERSION) {

    companion object{
        val CREATE_TABLA_ARTICULOS="CREATE TABLE IF NOT EXISTS " +ContratoBaseDeDatos.Companion.Articulos.NOMBRE_TABLA +
                " (" + ContratoBaseDeDatos.Companion.Articulos.COLUMNA_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ContratoBaseDeDatos.Companion.Articulos.COLUMNA_CODIGO_ARTICULO + " VARCHAR(20) NOT NULL, " +
                ContratoBaseDeDatos.Companion.Articulos.COLUMNA_NOMBRE_ARTICULO + " VARCHAR(50) NOT NULL, " +
                ContratoBaseDeDatos.Companion.Articulos.COLUMNA_PRECIO_COMPRA + " DOUBLE(6,3), " +
                ContratoBaseDeDatos.Companion.Articulos.COLUMNA_PRECIO_VENTA + " DOUBLE(6,3), " +
                ContratoBaseDeDatos.Companion.Articulos.COLUMNA_URL_IMAGEN + " VARCHAR(75) NOT NULL)"

        val CREATE_TABLA_INVENTARIO="CREATE TABLE IF NOT EXISTS " +ContratoBaseDeDatos.Companion.Inventario.NOMBRE_TABLA +
                " ("+ ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_INVENTARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_ARTICULO + " INTEGER NOT NULL, " +
                ContratoBaseDeDatos.Companion.Inventario.COLUMNA_STOCK_ACTUAL + " INTEGER NOT NULL, "+
                "FOREIGN KEY("+ContratoBaseDeDatos.Companion.Inventario.COLUMNA_ID_ARTICULO+") REFERENCES "+
                ContratoBaseDeDatos.Companion.Articulos.NOMBRE_TABLA +"("+ContratoBaseDeDatos.Companion.Articulos.COLUMNA_ID +"))"

        val CREATE_TABLA_REGISTRO_DE_VENTAS="CREATE TABLE IF NOT EXISTS " + ContratoBaseDeDatos.Companion.RegistroDeVentas.NOMBRE_TABLA +
                " (" + ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_ID_VENTA + " INTEGER PRIMARY KEY, "+
                ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_FECHA_VENTA +" DATETIME NOT NULL, "+
                ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_HORA_VENTA + " DATETIME NOT NULL, "+
                ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_MONTO_VENTA + " DOUBLE(6,3) NOT NULL, " +
                ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_TOTAL_ARTICULOS_VENDIDOS + " INTEGER NOT NULL)"

        val CREATE_TABLA_ARTICULOS_VENDIDOS="CREATE TABLE IF NOT EXISTS " + ContratoBaseDeDatos.Companion.ArticulosVendidos.NOMBRE_TABLA +
                " ("+ ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_ID_REGISTRO + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_ID_VENTA + " INTEGER NOT NULL, "+
                ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_ID_ARTICULO + " INTEGER NOT NULL, "+
                ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_CANTIDAD_VENTA + " INTEGER NOT NULL, "+
                "FOREIGN KEY("+ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_ID_VENTA+") REFERENCES "+
                ContratoBaseDeDatos.Companion.RegistroDeVentas.NOMBRE_TABLA +"("+ContratoBaseDeDatos.Companion.RegistroDeVentas.COLUMNA_ID_VENTA+"), "+
                "FOREIGN KEY("+ContratoBaseDeDatos.Companion.ArticulosVendidos.COLUMNA_ID_ARTICULO+") REFERENCES "+
                ContratoBaseDeDatos.Companion.Articulos.NOMBRE_TABLA +"("+ContratoBaseDeDatos.Companion.Articulos.COLUMNA_ID+"))"

        val CREATE_TABLA_REGISTRO_INGRESO_DE_ARTICULOS="CREATE TABLE IF NOT EXISTS " + ContratoBaseDeDatos.Companion.RegistroIngresoDeArticulos.NOMBRE_TABLA +
                " (" + ContratoBaseDeDatos.Companion.RegistroIngresoDeArticulos.COLUMNA_ID_INGRESO + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ContratoBaseDeDatos.Companion.RegistroIngresoDeArticulos.COLUMNA_FECHA_INGRESO + " DATETIME NOT NULL, "+
                ContratoBaseDeDatos.Companion.RegistroIngresoDeArticulos.COLUMNA_HORA_INGRESO + " DATETIME NOT NULL, "+
                ContratoBaseDeDatos.Companion.RegistroIngresoDeArticulos.COLUMNA_TOTAL_ARTICULOS_INGRESADOS + " INTEGER NOT NULL)"

        val CREATE_TABLA_ARTICULOS_INGRESADOS="CREATE TABLE IF NOT EXISTS " + ContratoBaseDeDatos.Companion.ArticulosIngresados.NOMBRE_TABLA +
                " (" + ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_ID_INGRESO + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_ID_ARTICULO + " INTEGER NOT NULL, "+
                ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_CANTIDAD_INGRESO + " INTEGER NOT NULL, "+
                ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_FECHA_INGRESO + " DATETIME NOT NULL, " +
                ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_HORA_INGRESO + " DATETIME NOT NULL, " +
                "FOREIGN KEY("+ContratoBaseDeDatos.Companion.ArticulosIngresados.COLUMNA_ID_ARTICULO+") REFERENCES "+
                ContratoBaseDeDatos.Companion.Articulos.NOMBRE_TABLA +"("+ContratoBaseDeDatos.Companion.Articulos.COLUMNA_ID+"))"

        val DELETE_TABLA_ARTICULOS="DROP TABLE IF EXISTS " + ContratoBaseDeDatos.Companion.Articulos.NOMBRE_TABLA
        val DELETE_TABLA_ARTICULOS_INGRESADOS="DROP TABLE IF EXISTS " + ContratoBaseDeDatos.Companion.ArticulosIngresados.NOMBRE_TABLA
        val DELETE_TABLA_ARTICULOS_VENDIDOS="DROP TABLE IF EXISTS " + ContratoBaseDeDatos.Companion.ArticulosVendidos.NOMBRE_TABLA
        val DELETE_TABLA_REGISTRO_INGRESO_DE_ARTICULOS="DROP TABLE IF EXISTS " + ContratoBaseDeDatos.Companion.RegistroIngresoDeArticulos.NOMBRE_TABLA
        val DELETE_TABLA_INVENTARIO="DROP TABLE IF EXISTS " + ContratoBaseDeDatos.Companion.Inventario.NOMBRE_TABLA
        val DELETE_TABLA_REGISTRO_DE_VENTAS="DROP TABLE IF EXISTS " + ContratoBaseDeDatos.Companion.RegistroDeVentas.NOMBRE_TABLA
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLA_ARTICULOS)
        db?.execSQL(CREATE_TABLA_ARTICULOS_INGRESADOS)
        db?.execSQL(CREATE_TABLA_ARTICULOS_VENDIDOS)
   //     db?.execSQL(CREATE_TABLA_REGISTRO_INGRESO_DE_ARTICULOS)
        db?.execSQL(CREATE_TABLA_INVENTARIO)
        db?.execSQL(CREATE_TABLA_REGISTRO_DE_VENTAS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DELETE_TABLA_ARTICULOS)
        db?.execSQL(DELETE_TABLA_ARTICULOS_INGRESADOS)
        db?.execSQL(DELETE_TABLA_ARTICULOS_VENDIDOS)
        db?.execSQL(DELETE_TABLA_REGISTRO_INGRESO_DE_ARTICULOS)
        db?.execSQL(DELETE_TABLA_INVENTARIO)
        db?.execSQL(DELETE_TABLA_REGISTRO_DE_VENTAS)
        onCreate(db)
    }

}