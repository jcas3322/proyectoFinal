package com.progra3.puntoventa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActividadInventario : AppCompatActivity() {
    var crud:CRUD?=null
    var lista:RecyclerView?=null
    var adaptador:AdaptadorCustom?=null
    var layoutManager:RecyclerView.LayoutManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_inventario)
        crud= CRUD(this)

        lista=findViewById(R.id.lista)
        lista?.setHasFixedSize(true)
        layoutManager=LinearLayoutManager(this)
        lista?.layoutManager=layoutManager

        var elementos = crud?.cargarDatos()

        adaptador=AdaptadorCustom(this,elementos!!)
        lista?.adapter=adaptador
    }
}