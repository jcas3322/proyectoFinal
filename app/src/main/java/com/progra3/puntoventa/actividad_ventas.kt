package com.progra3.puntoventa

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.progra3.Interfaces.ClickListener
import com.progra3.modelos.Articulo
import com.progra3.modelos.ArticulosVendidos
import com.progra3.modelos.RegistroDeVentas

class actividad_ventas : AppCompatActivity() {

    var articulos=ArrayList<Articulo>()
    var totalArticulos:Int=0
    var total:Double=0.0
    var idVenta:Long=1

    lateinit var scannerBarras: ActivityResultLauncher<Intent>
    lateinit var textoCantidad:EditText
    lateinit var textoCodigo:EditText
    lateinit var textoTotal:TextView
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adaptador:AdaptadorVentas
    val permisoCamara=android.Manifest.permission.CAMERA
    val CODIGO_PERMISOS=100
    var verificadorCamara=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_ventas)

        //Instanciando objetos de uso primario
        val botonScan=findViewById<Button>(R.id.scanear)
        val botonVenta=findViewById<Button>(R.id.botonGrabarVenta)
        textoCantidad=findViewById(R.id.cantidadArticulos)
        textoCodigo=findViewById(R.id.codigoArticulo)
        textoTotal=findViewById(R.id.textoTotal)
        recyclerView=findViewById(R.id.listaVentasReciclerView)
        layoutManager=LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=layoutManager
        adaptador=AdaptadorVentas(articulos,object :ClickListener{
            override fun conClick(vista: View, index: Int) {
                eliminar(index)
            }

        })

        recyclerView.adapter=adaptador

        //Cargando idVenta
        getMaxId()

        textoCantidad.setText("1")
        textoCodigo.requestFocus()

/*        textoCodigo.setOnKeyListener(object :View.OnKeyListener{
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if ((p2?.action == KeyEvent.ACTION_DOWN && p2?.action==KeyEvent.KEYCODE_ENTER)){
                    accionarInsercion()
                    textoCodigo.setText("")
                    textoCodigo.requestFocus()
                    return true
                }
                return false
            }
        })
*/
        botonScan.setOnClickListener {
            if(textoCodigo.text.isEmpty()){pedirPermisos()}
            else{
                accionarInsercion()
            }
        }

        botonVenta.setOnClickListener {
            grabarVenta()
        }
        scannerBarras= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val intentoLeer = IntentIntegrator.parseActivityResult(it.resultCode,it.data)
            if(intentoLeer!=null){
                if(intentoLeer.contents != null){
                    textoCodigo.setText(intentoLeer.contents)
                    accionarInsercion()
                }else{
                    Toast.makeText(this,"Operacion Cancelada",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun dispararScan(){
        if (verificadorCamara){
            var integrador:IntentIntegrator= IntentIntegrator(this)
            integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrador.setPrompt("Lector-Programa")
            integrador.setCameraId(0)
            integrador.setBeepEnabled(true)
            integrador.setBarcodeImageEnabled(true)
            integrador.setOrientationLocked(true)
            scannerBarras.launch(integrador.createScanIntent())
        }
    }

    fun eliminar(index:Int){
        val dialogo=AlertDialog.Builder(this)
        dialogo.setTitle("ATENCION")
        dialogo.setMessage("¿Eliminar Item de la Lista?")
        dialogo.setNegativeButton("CANCELAR"){ dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        dialogo.setPositiveButton("SI, ELIMINAR") { dialogInterface: DialogInterface, i: Int ->
            articulos.removeAt(index)
            accionarTotales()
            adaptador.notifyDataSetChanged()
        }
        dialogo.show()
    }

    fun grabarVenta(){
        if(total>0){
            val vender=ImplementacionRestRegistroDeVentas(this)
            val detalle=ImplementacionRestArticulosVendidos(this)
            val actualizar=ImplementacionRestArticulo(this)
            val actualizarAux=ImplementacionRestArticulo(this)
            val registro=RegistroDeVentas(idVenta,null,null,total,totalArticulos)
            vender.addVenta(registro){
                if (it != null){
                    Toast.makeText(this,"VENTA GRABADA CODIGO "+ it,Toast.LENGTH_LONG).show()
                }
            }
            for(articulo in articulos){
                val articuloVendido=ArticulosVendidos(null,idVenta,articulo.id,articulo.stock)
                actualizar.buscarPorCodigo(articulo.codigo){
                    if (it != null){
                        it.stock-=articulo.stock
                        actualizarAux.updateArticulo(it.id,it){}
                    }else{
                        Toast.makeText(this,"ERROR AL MODIFICAR STOCK ARTICULO",Toast.LENGTH_LONG).show()
                    }
                }
                detalle.addArticulosVendidos(articuloVendido){}
            }
            articulos.clear()
            accionarTotales()
            getMaxId()
            adaptador.notifyDataSetChanged()
            textoCantidad.setText("1")
            textoCodigo.requestFocus()
        }else{
            Toast.makeText(this,"LISTADO DE ARTICULOS VACIO",Toast.LENGTH_LONG).show()
            textoCodigo.requestFocus()
        }
    }

    fun accionarTotales(){
        total=0.0
        totalArticulos=0
        if (articulos.size>0){
            for(articulo in articulos){
                totalArticulos+=articulo.stock
                total+=(articulo.stock*articulo.precioVenta)
            }
        }
        textoTotal.setText("TOTAL Q"+total)
        textoCantidad.setText("1")
        textoCodigo.setText("")
        textoCodigo.requestFocus()
    }

    fun getMaxId(){
        val solicitar=ImplementacionRestRegistroDeVentas(this)
        solicitar.getMaxId {
            if (it != null){
                idVenta=it.id+1
            }
        }
    }
    fun accionarInsercion(){
        if(verificarCampos()){
            val codigo:String=textoCodigo.text.toString()
            val cantidad:Int=textoCantidad.text.toString().toInt()

            val servicio=ImplementacionRestArticulo(this)
            servicio.buscarPorCodigo(codigo){
                if(it != null){
                    if (it.stock >= cantidad){

                        if (articulos.size==0){
                            it.stock=cantidad
                            articulos.add(it)

                            adaptador.notifyDataSetChanged()

                        }else{
                            var n:Int=0
                            var verificador=false
                            for (articulo in articulos){
                                if (articulo.codigo.equals(codigo)){
                                    if (it.stock >= (articulo.stock+cantidad)){
                                        //Intentando modificar fila de articulos
                                        articulos.get(n).stock+=cantidad
                                        verificador=true

                                        adaptador.notifyDataSetChanged()
                                    }else{
                                        Toast.makeText(this,"STOCK NO DISPONIBLE",Toast.LENGTH_LONG).show()
                                        verificador=true
                                    }
                                }
                                n++
                            }
                            if(!verificador){
                                it.stock=cantidad
                                articulos.add(it)

                                adaptador.notifyDataSetChanged()
                            }
                        }
                        //FIN DEL ENQUEUE
                        accionarTotales()
                    }else{
                        Toast.makeText(this,"STOCK NO DISPONIBLE",Toast.LENGTH_LONG).show()
                        textoCantidad.setText("1")
                        textoCodigo.requestFocus()
                    }
                }else{
                    Toast.makeText(this,"Articulo No Encontrado",Toast.LENGTH_LONG).show()
                    textoCantidad.setText("1")
                    textoCodigo.requestFocus()
                }
            }
    }else{
        Toast.makeText(this,"Debe llenar los campos vacios",Toast.LENGTH_LONG).show()
        }
    }

    fun verificarCampos():Boolean{
        if (textoCantidad.text.isEmpty() || textoCodigo.text.isEmpty()){
            Toast.makeText(this,"Debe asignar los valores, campos Vacios!!",Toast.LENGTH_LONG).show()
            return false
        }else{
            val verificar:Int=textoCantidad.text.toString().toInt()
            if (verificar<=0){
                return false
            }
            return true
        }
    }
    fun pedirPermisos(){
        var Camara= ContextCompat.checkSelfPermission(this,permisoCamara)
        if (Camara != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(permisoCamara),CODIGO_PERMISOS)
        }else{
            verificadorCamara=true
            dispararScan()
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
                if (grantResults.size >=0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        verificadorCamara=true
                        dispararScan()
                    }else{
                        Toast.makeText(this,"SIN PERMISOS",Toast.LENGTH_LONG).show()
                        verificadorCamara=false
                    }
                }
            }
        }
    }

}