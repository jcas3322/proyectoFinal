package com.progra3.puntoventa

import android.content.Context
import android.widget.Toast
import com.progra3.Interfaces.ServiciosRestRegistroVentas
import com.progra3.modelos.RegistroDeVentas
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImplementacionRestRegistroDeVentas(val contexto: Context) {
    val retrofit=ConexionRetrofit.buildService(ServiciosRestRegistroVentas::class.java)

    fun addVenta(venta:RegistroDeVentas, onResult: (Int?) -> Unit){
        retrofit.addVenta(venta).enqueue(object :Callback<RegistroDeVentas>{
            override fun onResponse(
                call: Call<RegistroDeVentas>,
                response: Response<RegistroDeVentas>
            ) {
                val codigo=response.code()
                onResult(codigo)
            }

            override fun onFailure(call: Call<RegistroDeVentas>, t: Throwable) {
                Toast.makeText(contexto,"ERROR AL GRABAR VENTA "+ t.message,Toast.LENGTH_LONG).show()
                onResult(null)
            }

        })
    }

    fun reporteFecha(fecha:String,onResult: (ArrayList<RegistroDeVentas>?) -> Unit){
        retrofit.getRegistroFecha(fecha).enqueue(object : Callback<ArrayList<RegistroDeVentas>> {
            override fun onResponse(
                call: Call<ArrayList<RegistroDeVentas>>,
                response: Response<ArrayList<RegistroDeVentas>>
            ) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ArrayList<RegistroDeVentas>>, t: Throwable) {
                Toast.makeText(contexto,"ERROR AL PEDIR DATOS "+t.message,Toast.LENGTH_LONG).show()
                onResult(null)
            }

        })
    }

    fun getMaxId(onResult:(RegistroDeVentas?)-> Unit){
        retrofit.getMaxIdRegistroVentas().enqueue(object :Callback<RegistroDeVentas>{
            override fun onResponse(
                call: Call<RegistroDeVentas>,
                response: Response<RegistroDeVentas>
            ) {
                val respuesta=response.body()
                onResult(respuesta)
            }

            override fun onFailure(call: Call<RegistroDeVentas>, t: Throwable) {
                onResult(null)
            }

        })
    }
}