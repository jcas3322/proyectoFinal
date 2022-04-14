package com.progra3.puntoventa

import android.content.Context
import android.widget.Toast
import com.progra3.Interfaces.ServiciosRestArticulo
import com.progra3.modelos.Articulo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImplementacionRestArticulo(val context: Context) {

    fun updateArticulo(id:Long, articulo: Articulo, onResult: (Articulo?) -> Unit){
        val retrofit=ConexionRetrofit.buildService(ServiciosRestArticulo::class.java)
        retrofit.updateArticulo(id, articulo).enqueue(object :Callback<Articulo>{
            override fun onResponse(call: Call<Articulo>, response: Response<Articulo>) {
                val addArticulo=response.body()
                onResult(addArticulo)
                Toast.makeText(context,"Recurso Modificado... Codigo " + response.code(),Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Articulo>, t: Throwable) {
                onResult(null)
                Toast.makeText(context,"Error de Comunicacion... Codigo " + t.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    fun addArticulo(articulo: Articulo, onResult: (Articulo?) -> Unit){
        val retrofit=ConexionRetrofit.buildService(ServiciosRestArticulo::class.java)
        retrofit.createArticulo(articulo).enqueue(object :Callback<Articulo>{
            override fun onResponse(call: Call<Articulo>, response: Response<Articulo>) {
                val addArticulo=response.body()
                onResult(addArticulo)
                Toast.makeText(context,"Recurso Creado... Codigo " + response.code(),Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Articulo>, t: Throwable) {
                onResult(null)
                Toast.makeText(context,"Error de Comunicacion... Codigo " + t.message,Toast.LENGTH_LONG).show()
            }

        })
    }
}