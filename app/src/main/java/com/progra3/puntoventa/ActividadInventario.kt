package com.progra3.puntoventa

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.progra3.Interfaces.ClickListener
import com.progra3.modelos.Articulo
import java.io.File
import java.net.URI

class ActividadInventario : AppCompatActivity(), SearchView.OnQueryTextListener {
    //PERMISOS PARA ELIMINAR IMAGEN
    val CODIGO_PERMISOS=100
    val permisoEscritura=android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    val permisoLectura=android.Manifest.permission.READ_EXTERNAL_STORAGE

    var lista:RecyclerView?=null
    var adaptador:AdaptadorCustom?=null
    var layoutManager:RecyclerView.LayoutManager?=null

    var seleccionOperacion:Int=-1
    var index2:Int?=null

    //Objeto a usar para RecyclerView
    var nuevoListado=ArrayList<Articulo>()
    var fotoEliminar:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_inventario)

        var busqueda=findViewById<SearchView>(R.id.buscar)
        busqueda.setOnQueryTextListener(this)

        //Boton Refrescar
        val btnRefresh=findViewById<FloatingActionButton>(R.id.botonRefrescar)
        btnRefresh.setOnClickListener { cargarDatos() }

        lista=findViewById(R.id.lista)

        //Cada fila tiene un alto especifico
        lista?.setHasFixedSize(true)

        layoutManager=LinearLayoutManager(this)
        lista?.layoutManager=layoutManager

        //Metodo de SQLite
        //var elementos = crud?.cargarDatos()

        //Pendiente de actualizar
        adaptador=AdaptadorCustom(this,nuevoListado,object: ClickListener{
            override fun conClick(vista: View, index: Int) {
                index2=index
                dialogoReciclerView()

                //Esta seria la forma de interactuar entre objetos dentro del reciclerview
                //if (vista.tag=="SOYFOTO")Toast.makeText(applicationContext,"ES LA FOTO POSICION: "+index,Toast.LENGTH_LONG).show()
            }

        })
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
            nuevoListado.clear()
            if (it != null)nuevoListado.addAll(it)
            adaptador?.notifyDataSetChanged()
        }

    }

    override fun onResume() {
        super.onResume()
        cargarDatos()
    }

    fun dialogoReciclerView() {
        val dialogo=AlertDialog.Builder(this)
        dialogo.setTitle("Seleccione Opcion Deseada")

        val adaptadorDialogo=ArrayAdapter<String>(this,android.R.layout.simple_selectable_list_item)
        adaptadorDialogo.add("Ingresar Stock de Articulo")
        adaptadorDialogo.add("Eliminar Articulo")

        dialogo.setAdapter(adaptadorDialogo){ dialogInterface: DialogInterface, i: Int ->
            seleccionOperacion=i
            seleccionDialogo()
        }

        dialogo.setNegativeButton("Cancelar"){ dialogInterface: DialogInterface, i: Int ->
            seleccionOperacion=-1
            dialogInterface.dismiss()
        }

        dialogo.show()
    }

    fun seleccionDialogo(){
        when(seleccionOperacion){
            0 -> abrirEdicion(index2!!)
            1 -> eliminarArticulo(index2!!)
        }
    }

    fun abrirEdicion(index: Int?){
        val articulo=nuevoListado.get(index!!)
        IngresarArticulos.modificar=true
        IngresarArticulos.idArticulo=articulo.id
        IngresarArticulos.codigo=articulo.codigo
        IngresarArticulos.nombre=articulo.nombre
        IngresarArticulos.compra=articulo.precioCompra
        IngresarArticulos.venta=articulo.precioVenta
        IngresarArticulos.UrlFotoActual=articulo.urlImagen
        IngresarArticulos.stock=articulo.stock
        startActivity(Intent(this,IngresarArticulos::class.java))
    }

    fun eliminarArticulo(index:Int?){
        val dialogo=AlertDialog.Builder(this)
        dialogo.setTitle("¿Confirma Eliminar?")
        dialogo.setMessage("Esta Operacion no se puede revertir")

        dialogo.setNegativeButton("CANCELAR") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }

        dialogo.setPositiveButton("SI, ELIMINAR"){ dialogInterface: DialogInterface, i: Int ->
            val item=nuevoListado.get(index!!)
            val retrofitEliminar= ImplementacionRestArticulo(this)
            fotoEliminar=item.urlImagen
            retrofitEliminar.deleteArticulo(item.id){
                Toast.makeText(this,"Recurso Eliminado... Codigo " + it,Toast.LENGTH_SHORT).show()
                cargarDatos()
            }
            pedirPermisos()
        }
        dialogo.show()
    }

    fun eliminarImagen(ruta:String){
        val uri=URI.create(ruta)
        val file= File(uri)
        val resultado=file.delete()
        if (resultado) Toast.makeText(this,"FOTO ELIMINADA CON EXITO",Toast.LENGTH_LONG).show()
        else Toast.makeText(this,"ERROR AL ELIMINAR FOTO",Toast.LENGTH_LONG).show()
    }

    fun pedirPermisos(){
        var Escritura= ContextCompat.checkSelfPermission(this,permisoEscritura)
        var Lectura= ContextCompat.checkSelfPermission(this,permisoLectura)

        if (!(Escritura == PackageManager.PERMISSION_GRANTED || Lectura== PackageManager.PERMISSION_GRANTED)){
            requestPermissions(arrayOf(permisoEscritura,permisoLectura),CODIGO_PERMISOS)
        }else{
            eliminarImagen(fotoEliminar)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CODIGO_PERMISOS ->{
                if (grantResults.size >0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                        eliminarImagen(fotoEliminar)
                    }else{
                        Toast.makeText(this,"SIN PERMISOS",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun buscarPorNombre(nombre:String){
        val buscar=ImplementacionRestArticulo(this)
        buscar.buscarPorArticulo(nombre){
            nuevoListado.clear()
            if (it != null)nuevoListado.addAll(it)
            adaptador?.notifyDataSetChanged()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            buscarPorNombre(query)
        }
        val v=this.currentFocus
        if(v!=null){
            val esconder=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            esconder.hideSoftInputFromWindow(v.windowToken,0)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}