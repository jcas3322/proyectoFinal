package com.progra3.puntoventa

import android.content.Context
import android.widget.Toast
import com.progra3.Interfaces.ServiciosResArticulosIngresados
import com.progra3.modelos.ArticulosIngresados
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImplementacionRestArticulosIngresados (val context: Context){
    val retrofit=ConexionRetrofit.buildService(ServiciosResArticulosIngresados::class.java)

    fun addIngresoArticulo(articulo: ArticulosIngresados, onResult: (ArticulosIngresados?) -> Unit){
        retrofit.createIngresoArticulo(articulo).enqueue(object : Callback<ArticulosIngresados> {
            override fun onResponse(call: Call<ArticulosIngresados>, response: Response<ArticulosIngresados>) {
                val addArticulo=response.body()
                onResult(addArticulo)
                Toast.makeText(context,"Registro de Ingreso de Articulo Creado... Codigo " + response.code(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ArticulosIngresados>, t: Throwable) {
                onResult(null)
                println("Ingreso Articulos... " +t.message)
                Toast.makeText(context,"Error, Registro de Ingreso de Articulos... Mensaje: " + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun buscarIngresosPorFecha(fecha:String, onResult: (ArrayList<ArticulosIngresados>?) -> Unit){
        retrofit.buscarIngresoArticulos(fecha).enqueue(object : Callback<ArrayList<ArticulosIngresados>> {
            override fun onResponse(
                call: Call<ArrayList<ArticulosIngresados>>,
                response: Response<ArrayList<ArticulosIngresados>>
            ) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArrayList<ArticulosIngresados>>, t: Throwable) {
                Toast.makeText(context,"ERROR AL SOLICITAR DATOS "+ t.message,Toast.LENGTH_LONG).show()
                onResult(null)
            }

        })
    }
}