package com.progra3.puntoventa

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConexionRetrofit {

    private val cliente=OkHttpClient.Builder().build()
    private val retrofit=Retrofit.Builder()
        .baseUrl("http://192.168.1.35:8044")
        .addConverterFactory(GsonConverterFactory.create())
        .client(cliente)
        .build()

    fun<T> buildService(servicio:Class<T>):T{
        return retrofit.create(servicio) as T
    }
}