package com.progra3.puntoventa

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.progra3.modelos.Articulo
import com.progra3.modelos.ArticulosIngresados
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class IngresarArticulos : AppCompatActivity() {

    val SOLICITUD_TOMAR_FOTO=1
    val SOLICITUD_LEER_BARRAS=2
    val permisoCamara=android.Manifest.permission.CAMERA
    val permisoWriteStorage=android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    val permisoReadStorage=android.Manifest.permission.READ_EXTERNAL_STORAGE

    var iVfoto:ImageView?=null

    var textoCodArticulo:EditText?=null
    var textoNombreArticulo:EditText?=null
    var textoPrecioCompra:EditText?=null
    var textoPrecioVenta:EditText?=null
    var textoIdArticulo:EditText?=null
    var textoStock:EditText?=null

    var fotofoto:ActivityResultLauncher<Intent>? =null
    var scannerBarras:ActivityResultLauncher<Intent>? =null

    companion object{
        var modificar=false
        var idArticulo:Long=0
        var codigo:String=""
        var nombre:String=""
        var compra:Double=0.0
        var venta:Double=0.0
        var stock:Int=0 //Recibe stock actual para abajo sumarlo al stock que esta ingresando
        var UrlFotoActual=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresar_articulos)
        val tomarFoto=findViewById<Button>(R.id.tomarFoto)

        scannerBarras= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val intentoLeer = IntentIntegrator.parseActivityResult(it.resultCode,it.data)
            if(intentoLeer!=null){
                if(intentoLeer.contents != null){
                    textoCodArticulo?.setText(intentoLeer.contents)
                    textoNombreArticulo?.requestFocus()
                }else{
                    Toast.makeText(this,"Operacion Cancelada",Toast.LENGTH_SHORT).show()
                }
            }
        }

        fotofoto=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result:ActivityResult ->
            if (result.resultCode== RESULT_OK){
                val uri= Uri.parse(UrlFotoActual)
                val stream=contentResolver.openInputStream(uri)
                val imageBitmap=BitmapFactory.decodeStream(stream)
                iVfoto?.setImageBitmap(imageBitmap)
            }
        }

        iVfoto=findViewById(R.id.fotoArticulo)
        tomarFoto.setOnClickListener {
            pedirPermisos()
        }

        textoCodArticulo=findViewById(R.id.texto_cod_articulol1)
        textoNombreArticulo=findViewById(R.id.texto_nombre_articulol1)
        textoPrecioCompra=findViewById(R.id.texto_precioCompra_articulol1)
        textoPrecioVenta=findViewById(R.id.texto_precioVenta_articulol1)
        textoIdArticulo=findViewById(R.id.texto_id_articulo)
        textoStock=findViewById(R.id.texto_id_stock)

        val botonGuardar=findViewById<Button>(R.id.boton_guardar_l1)
        val botonVolver=findViewById<Button>(R.id.boton_volver_l1)
        val botonLeerBarras=findViewById<Button>(R.id.btn_leer_barras)

        botonLeerBarras.setOnClickListener {
            var integrador:IntentIntegrator= IntentIntegrator(this)
            integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrador.setPrompt("Lector-Programa")
            integrador.setCameraId(0)
            integrador.setBeepEnabled(true)
            integrador.setBarcodeImageEnabled(true)
            integrador.setOrientationLocked(true)
            scannerBarras?.launch(integrador.createScanIntent())
        }

        botonVolver.setOnClickListener {
            finish()
            /*
            val enviar=SolicitudFTP()
            enviar.enviarFoto(UrlFotoActual){
                    println("Respuesta del Hilo: " + it)
            }*/
        }
        botonGuardar.setOnClickListener {
            if (!modificar){
                if (verificarCampos()){
                    var articulo=Articulo(textoIdArticulo?.text!!.toString().toLong(),textoCodArticulo?.text!!.toString(),
                        textoNombreArticulo?.text!!.toString(),textoPrecioCompra?.text!!.toString().toDouble(),
                        textoPrecioVenta?.text!!.toString().toDouble(),textoStock?.text!!.toString().toInt(),UrlFotoActual)

                    //Guardar Articulo Nuevo
                    var RestArticulo=ImplementacionRestArticulo(this)
                    RestArticulo.addArticulo(articulo){
                        if (it != null){limpiarControles()}
                    }

                    //Registrarlo en Ingreso de Articulos
                    var RestIngresoArticulo=ImplementacionRestArticulosIngresados(this)
                    val ingreso=ArticulosIngresados(null,textoIdArticulo?.text!!.toString().toLong(),
                    textoStock?.text!!.toString().toInt(),null)
                    RestIngresoArticulo.addIngresoArticulo(ingreso){
                        if (it != null){
                            Toast.makeText(this,"Operacion Finalizada con EXITO",Toast.LENGTH_SHORT).show()
                        }
                    }
    /*METODO SQLITE
                    var crud=CRUD(this)
                    var articulos=Articulos(textoIdArticulo?.text!!.toString().toInt(),textoCodArticulo?.text!!.toString()
                    ,textoNombreArticulo?.text!!.toString(),textoPrecioCompra?.text!!.toString().toDouble(),
                    textoPrecioVenta?.text!!.toString().toDouble(),UrlFotoActual)
                    if(crud.nuevoArticulo(articulos,textoStock?.text!!.toString().toInt())) limpiarControles()
    */
                }
            }else{
                //Implementacion de UPDATE para ingreso de nuevo stock y por si el usuario desea actualizar
                //algun valor del item
                if(verificarCampos()){
                    //Creando el objeto para dar el Update
                    var articulo=Articulo(textoIdArticulo?.text!!.toString().toLong(),textoCodArticulo?.text!!.toString(),
                        textoNombreArticulo?.text!!.toString(),textoPrecioCompra?.text!!.toString().toDouble(),
                        textoPrecioVenta?.text!!.toString().toDouble(),(textoStock?.text!!.toString().toInt() + stock),UrlFotoActual)

                    //Haciendo UPDATE
                    var RestArticulo=ImplementacionRestArticulo(this)
                    RestArticulo.updateArticulo(textoIdArticulo?.text!!.toString().toLong(),articulo){
                        if (it != null){limpiarControles()}
                    }

                    //Registrarlo en Ingreso de Articulos
                    var RestIngresoArticulo=ImplementacionRestArticulosIngresados(this)
                    val ingreso=ArticulosIngresados(null,textoIdArticulo?.text!!.toString().toLong(),
                        textoStock?.text!!.toString().toInt(),null)
                    RestIngresoArticulo.addIngresoArticulo(ingreso){
                        if (it != null){
                            Toast.makeText(this,"Operacion Finalizada con EXITO",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        textoIdArticulo?.focusable= View.NOT_FOCUSABLE
        siModificaroNo()
        if (modificar) {
            textoStock?.requestFocus()
            textoStock?.selectAll()
        }
    }

    //Creando funcion de modificar o ingresar nuevo articulo
    fun siModificaroNo(){
        if (modificar){
            val botonGuardar=findViewById<Button>(R.id.boton_guardar_l1)
            botonGuardar.setText("INGRESAR STOCK")
            val titulo=findViewById<TextView>(R.id.etiquetaTituloArticulos)
            titulo.setText("MODIFICAR ARTICULO")
            textoCodArticulo?.setText(codigo)
            textoNombreArticulo?.setText(nombre)
            textoPrecioCompra?.setText(compra.toString())
            textoPrecioVenta?.setText(venta.toString())
            textoIdArticulo?.setText(idArticulo.toString())
            textoStock?.setText("1")
            val uri= Uri.parse(UrlFotoActual)
            val stream=contentResolver.openInputStream(uri)
            val imageBitmap=BitmapFactory.decodeStream(stream)
            iVfoto?.setImageBitmap(imageBitmap)
        }else{
            var buscarmaxId=ImplementacionRestArticulo(this)
            textoIdArticulo?.setText("1")
            buscarmaxId.maxIdArticulo(){
                if (it?.id!=null){
                    var contador:Long=0
                    contador=it?.id+1
                    textoIdArticulo?.setText(contador.toString())
                }
            }
        }
        textoCodArticulo?.requestFocus()
    }

    fun limpiarControles(){
        textoCodArticulo?.setText("")
        textoNombreArticulo?.setText("")
        textoPrecioCompra?.setText("")
        textoPrecioVenta?.setText("")
        var conteo:Long=textoIdArticulo?.text.toString().toLong()+1
        textoIdArticulo?.setText(conteo.toString())
        textoStock?.setText("")
        iVfoto?.setImageBitmap(null)
        textoCodArticulo?.requestFocus()
        UrlFotoActual=""
    }

    fun verificarCampos():Boolean{
        if (textoCodArticulo?.text!!.isEmpty() || textoNombreArticulo?.text!!.isEmpty() ||
                textoPrecioCompra?.text!!.isEmpty() || textoPrecioVenta?.text!!.isEmpty() ||
                textoIdArticulo?.text!!.isEmpty() || textoStock?.text!!.isEmpty()
            || UrlFotoActual==""){
            Toast.makeText(this,"DEBE LLENAR TODOS LOS CAMPOS Y ADJUNTAR IMAGEN",Toast.LENGTH_SHORT).show()
            textoCodArticulo?.requestFocus()
            return false
        }
        return true
    }

    fun dispararIntentTomarFoto(){
        val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager)!=null){
            var archivoFoto:File?=null
            archivoFoto=crearArchivoImagen()

            if(archivoFoto!=null){
                val urlFoto=FileProvider.getUriForFile(this,"com.progra3.puntoventa",archivoFoto)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,urlFoto)
                fotofoto?.launch(intent)
            //startActivityForResult(intent,SOLICITUD_TOMAR_FOTO)
            }
        }
    }

    fun pedirPermisos(){
        val ProveerContexto=ActivityCompat.shouldShowRequestPermissionRationale(this,permisoCamara)
        if(ProveerContexto){
            solicitudPermiso()
        }else{
            solicitudPermiso()
        }
    }
    fun solicitudPermiso(){

        requestPermissions(arrayOf(permisoCamara,permisoReadStorage,permisoWriteStorage),SOLICITUD_TOMAR_FOTO)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            SOLICITUD_TOMAR_FOTO ->{
                if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2]== PackageManager.PERMISSION_GRANTED){
                    //tenemos permiso
                    dispararIntentTomarFoto()
                }else{
                    Toast.makeText(this,"SIN PERMISOS NO SE PUEDE TOMAR FOTO",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun crearArchivoImagen():File{
        val timestamp=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val nombreArchivoImagen="JPEG_" + timestamp + "_"
        val directorio =getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imagen = File.createTempFile(nombreArchivoImagen,".jpg",directorio)

        UrlFotoActual="file://"+imagen.absolutePath
        return imagen
    }
}