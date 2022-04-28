package com.progra3.puntoventa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.progra3.modelos.ArticulosIngresados
import com.progra3.modelos.ArticulosVendidos
import com.progra3.modelos.RegistroDeVentas
import com.progra3.modelos.ReportesPDF
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var fecha:Date
    var total:Double=0.0
    var row=ArrayList<Array<String>>()
    var rowDetalleIngresos=ArrayList<Array<String>>()
    var RegistroVentas=ArrayList<RegistroDeVentas>()
    var DetalleVenta=ArrayList<ArticulosVendidos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonInventario=findViewById<Button>(R.id.botonInventario)
        val botonVentas=findViewById<Button>(R.id.botonVentas)
        val botonreportes=findViewById<Button>(R.id.botonReportes)
        val calendario=findViewById<CalendarView>(R.id.calendario)

        calendario.setDate(System.currentTimeMillis())
        fecha= Date(calendario.date)

        calendario.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val calendar:Calendar= Calendar.getInstance()
            calendar.set(i,i2,i3)
            calendarView.setDate(calendar.timeInMillis,true,true)
        }

        botonreportes.setOnClickListener {
            fecha= Date(calendario.date)
            val master=findViewById<RelativeLayout>(R.id.master)
            val barra=findViewById<ProgressBar>(R.id.progresoBarra)
            barra.visibility=View.VISIBLE
            master.visibility=View.GONE
            cargarDetalleIngresos()
        }

        botonVentas.setOnClickListener {
            startActivity(Intent(this,actividad_ventas::class.java))
        }
        botonInventario.setOnClickListener {
            startActivity(Intent(this,ActividadInventario::class.java))
        }
    }

    fun llenarGeneral(){
        val titulo1= arrayOf("HORA","MONTO","TOTAL ITEMS","CODIGO","CANTIDAD")
        val modeloPdf=ReportesPDF(this)
        modeloPdf.openDocument()
        modeloPdf.addMetaData("REPORTE GENERAL","AGROVETERINARIA KEVIN", "PERSONAL")
        modeloPdf.addTitles("NOMBRE DEL ESTABLECIMIENTO", "REPORTE DE VENTAS REALIZADAS", fecha.toString())
        modeloPdf.createTable(titulo1,row)
        modeloPdf.addParrafo("TOTAL DE VENTAS EXPRESADAS EN QUETZALES Q" + total.toString())
        modeloPdf.addParrafo("--------------------------------------------------------------------------")
        //Nueva Tabla
        val titulo2= arrayOf("ID","CODIGO DE ARTICULO","CANTIDAD INGRESO")
        modeloPdf.createTable(titulo2,rowDetalleIngresos)
        modeloPdf.closeDocument()
        val master=findViewById<RelativeLayout>(R.id.master)
        val barra=findViewById<ProgressBar>(R.id.progresoBarra)
        barra.visibility=View.GONE
        master.visibility=View.VISIBLE
        //Disparo de INTENT
        modeloPdf.viewPdf()
    }

    fun cargarDetalleVentas(){
        val resultadoDetalleVentas=ImplementacionRestArticulosVendidos(this)
        for(i in RegistroVentas){
            total+=i.montoVenta
            row.add(arrayOf(i.horaVenta.toString(),"Q"+i.montoVenta.toString(),i.cantidadArticulosVendidos.toString(),
            "-------------","------------"))

            DetalleVenta=resultadoDetalleVentas.otraForma(i.id)
            for (x in DetalleVenta){
                row.add(arrayOf("-------------","-------------","-------------",x.idArticulo.toString(),
                    x.cantidadVendida.toString()))
            }

            /*
            resultadoDetalleVentas.obtenerVentasporId(i.id){
                n=false
                if(it!=null){
                    DetalleVenta.addAll(it)
                }
            }*/
        }
        llenarGeneral()
    }

    fun cargarDetalleIngresos(){
        rowDetalleIngresos.clear()
        val detalleIngresos=ImplementacionRestArticulosIngresados(this)
        detalleIngresos.buscarIngresosPorFecha(fecha.toString()){
            if (it!=null){
                for(i in it) {
                    rowDetalleIngresos.add(arrayOf(i.id.toString(),i.idArticulo.toString(),
                    i.cantidadIngreso.toString()))
                }
            }
            cargarVentas()
        }
    }

    fun cargarVentas(){
        RegistroVentas.clear()
        DetalleVenta.clear()
        row.clear()
        total=0.0
        val resultadoVentas=ImplementacionRestRegistroDeVentas(this)
        resultadoVentas.reporteFecha(fecha.toString()){
            if(it!=null){
                RegistroVentas=it
                cargarDetalleVentas()
            }
        }
    }
}