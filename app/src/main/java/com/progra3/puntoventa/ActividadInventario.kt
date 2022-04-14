package com.progra3.puntoventa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progra3.modelos.Articulo

class ActividadInventario : AppCompatActivity() {
    var crud:CRUD?=null
    var lista:RecyclerView?=null
    var adaptador:AdaptadorCustom?=null
    var layoutManager:RecyclerView.LayoutManager?=null

    //Objeto a usar para RecyclerView
    var listadoInventario= mutableListOf<Articulo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_inventario)
        //crud= CRUD(this)

        lista=findViewById(R.id.lista)
        //lista?.setHasFixedSize(true)
        layoutManager=LinearLayoutManager(this)
        lista?.layoutManager=layoutManager

        //Metodo de SQLite
        //var elementos = crud?.cargarDatos()

        //Pendiente de actualizar
        adaptador=AdaptadorCustom(this,listadoInventario)
        lista?.adapter=adaptador
        cargarDatos()

        //Boton Flotante Agregar Articulo
        val botonAgregar=findViewById<FloatingActionButton>(R.id.botonagregar)
        botonAgregar.setOnClickListener {
            IngresarArticulos.modificar=false
            startActivity(Intent(this,IngresarArticulos::class.java))
        }
    }
//Metodo Retrofit
    fun cargarDatos(){
        var restServiceArticulo=ImplementacionRestArticulo(this)
        restServiceArticulo.allArticulos(){
            listadoInventario.clear()
            listadoInventario.addAll(it)
            adaptador!!.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatos()
    }
}