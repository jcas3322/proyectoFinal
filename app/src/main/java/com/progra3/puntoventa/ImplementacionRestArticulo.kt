package com.progra3.puntoventa

import android.content.Context
import android.os.StrictMode
import android.widget.Toast
import com.progra3.Interfaces.ServiciosRestArticulo
import com.progra3.modelos.Articulo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImplementacionRestArticulo(val context: Context) {

    val retrofit=ConexionRetrofit.buildService(ServiciosRestArticulo::class.java)

    fun buscarPorCodigo(codigo:String, onResult: (Articulo?) -> Unit){
        retrofit.getByCodigo(codigo).enqueue(object :Callback<Articulo>{
            override fun onResponse(call: Call<Articulo>, response: Response<Articulo>) {
                val resultado=response.body()
                onResult(resultado)
            }

            override fun onFailure(call: Call<Articulo>, t: Throwable) {
                onResult(null)
            }

        })
    }

    fun buscarPorArticulo(nombre:String, onResult: (ArrayList<Articulo>?) -> Unit){
        retrofit.getLikeAsArticulo(nombre).enqueue(object :Callback<ArrayList<Articulo>>{
            override fun onResponse(
                call: Call<ArrayList<Articulo>>,
                response: Response<ArrayList<Articulo>>
            ) {
                var respuesta=response.body()
                onResult(respuesta)
            }

            override fun onFailure(call: Call<ArrayList<Articulo>>, t: Throwable) {
                Toast.makeText(context,"Error de Busqueda por nombre... " + t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
    fun allArticulos2():ArrayList<Articulo>?{
        val policia= StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policia)
        return retrofit.getAllArticulos().execute().body()
    }

    fun allArticulos(onResult: (ArrayList<Articulo>?) -> Unit){
        retrofit.getAllArticulos().enqueue(object :Callback<ArrayList<Articulo>> {
            override fun onResponse(
                call: Call<ArrayList<Articulo>>,
                response: Response<ArrayList<Articulo>>
            ) {
                var respuesta= response.body()
                onResult(respuesta)
            }

            override fun onFailure(call: Call<ArrayList<Articulo>>, t: Throwable) {
                Toast.makeText(context,"Error de Solicitud de Inventario " + t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun maxIdArticulo(onResult: (Articulo?) -> Unit){
        retrofit.getMaxIdArticulos().enqueue(object :Callback<Articulo>{
            override fun onResponse(call: Call<Articulo>, response: Response<Articulo>) {
                val respuesta=response.body()
                onResult(respuesta)
            }

            override fun onFailure(call: Call<Articulo>, t: Throwable) {
                onResult(null)
                Toast.makeText(context,"Error de Operacion MaxId... Codigo " + t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun deleteArticulo(id: Long, onResult: (Int) -> Unit){
//        val retrofit=ConexionRetrofit.buildService(ServiciosRestArticulo::class.java)
        retrofit.deleteArticulo(id).enqueue(object :Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context,"Error de Operacion Eliminar... Codigo " + t.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    fun updateArticulo(id:Long, articulo: Articulo, onResult: (Articulo?) -> Unit){
//        val retrofit=ConexionRetrofit.buildService(ServiciosRestArticulo::class.java)
        retrofit.updateArticulo(id, articulo).enqueue(object :Callback<Articulo>{
            override fun onResponse(call: Call<Articulo>, response: Response<Articulo>) {
                val addArticulo=response.body()
                onResult(addArticulo)
                Toast.makeText(context,"Recurso Modificado... Codigo " + response.code(),Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Articulo>, t: Throwable) {
                onResult(null)
                Toast.makeText(context,"Error de Operacion Actualizar... Codigo " + t.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    fun addArticulo(articulo: Articulo, onResult: (Articulo?) -> Unit){
        retrofit.createArticulo(articulo).enqueue(object :Callback<Articulo>{
            override fun onResponse(call: Call<Articulo>, response: Response<Articulo>) {
                val addArticulo=response.body()
                onResult(addArticulo)
                Toast.makeText(context,"Recurso Creado... Codigo " + response.code(),Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Articulo>, t: Throwable) {
                onResult(null)
                Toast.makeText(context,"Error de Operacion Agregar... Codigo " + t.message,Toast.LENGTH_LONG).show()
            }
        })
    }
}