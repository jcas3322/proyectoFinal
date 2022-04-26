package com.progra3.puntoventa

import android.content.Context
import android.os.StrictMode
import android.widget.Toast
import com.progra3.Interfaces.ServiciosRestArticulosVendidos
import com.progra3.modelos.ArticulosVendidos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImplementacionRestArticulosVendidos(val contexto:Context) {
    val retrofit=ConexionRetrofit.buildService(ServiciosRestArticulosVendidos::class.java)

    fun addArticulosVendidos(item:ArticulosVendidos,onResult:(Int?)->Unit){
        retrofit.addArticulosVendidos(item).enqueue(object :Callback<ArticulosVendidos>{
            override fun onResponse(
                call: Call<ArticulosVendidos>,
                response: Response<ArticulosVendidos>
            ) {
                val codigo=response.code()
                onResult(codigo)
            }

            override fun onFailure(call: Call<ArticulosVendidos>, t: Throwable) {
                onResult(null)
                Toast.makeText(contexto,"ERROR AL GRABAR DETALLE DE VENTA "+ t.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    fun otraForma(idVenta: Long):ArrayList<ArticulosVendidos>{
        val policia=StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policia)
        return retrofit.buscarIdVenta(idVenta).execute().body()!!
    }

    fun obtenerVentasporId(idVenta:Long, onResult: (ArrayList<ArticulosVendidos>?) -> Unit){
        retrofit.buscarIdVenta(idVenta).enqueue(object : Callback<ArrayList<ArticulosVendidos>> {
            override fun onResponse(
                call: Call<ArrayList<ArticulosVendidos>>,
                response: Response<ArrayList<ArticulosVendidos>>
            ) {
                onResult(response.body())
            }
            override fun onFailure(call: Call<ArrayList<ArticulosVendidos>>, t: Throwable) {
                Toast.makeText(contexto,"ERROR DE COMUNICACION "+ t.message,Toast.LENGTH_LONG).show()
                onResult(null)
            }
        })
    }
}