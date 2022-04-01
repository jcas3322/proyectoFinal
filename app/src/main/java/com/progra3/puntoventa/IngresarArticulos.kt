package com.progra3.puntoventa

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class IngresarArticulos : AppCompatActivity() {

    val SOLICITUD_TOMAR_FOTO=1
    val SOLICITUD_LEER_BARRAS=2
    val permisoCamara=android.Manifest.permission.CAMERA
    val permisoWriteStorage=android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    val permisoReadStorage=android.Manifest.permission.READ_EXTERNAL_STORAGE
    var UrlFotoActual=""

    var iVfoto:ImageView?=null

    var textoCodArticulo:EditText?=null
    var textoNombreArticulo:EditText?=null
    var textoPrecioCompra:EditText?=null
    var textoPrecioVenta:EditText?=null
    var textoIdArticulo:EditText?=null
    var textoStock:EditText?=null

    var fotofoto:ActivityResultLauncher<Intent>? =null
    var scannerBarras:ActivityResultLauncher<Intent>? =null

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
            if (result.resultCode==Activity.RESULT_OK){
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

        botonVolver.setOnClickListener { finish()}
        botonGuardar.setOnClickListener {
            if (verificarCampos()){
                var crud=CRUD(this)
                var articulos=Articulos(textoIdArticulo?.text!!.toString().toInt(),textoCodArticulo?.text!!.toString()
                ,textoNombreArticulo?.text!!.toString(),textoPrecioCompra?.text!!.toString().toDouble(),
                textoPrecioVenta?.text!!.toString().toDouble(),UrlFotoActual)

                if(crud.nuevoArticulo(articulos,textoStock?.text!!.toString().toInt())) limpiarControles()
            }
        }
    }
    fun limpiarControles(){
        textoCodArticulo?.setText("")
        textoNombreArticulo?.setText("")
        textoPrecioCompra?.setText("")
        textoPrecioVenta?.setText("")
        textoIdArticulo?.setText("")
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
        val imagen = File.createTempFile(nombreArchivoImagen,"jpg",directorio)

        UrlFotoActual="file://"+imagen.absolutePath
        return imagen
    }
}