package com.progra3.puntoventa

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.FileProvider
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.net.URI

class visorPdf : AppCompatActivity() {
    var uri:Uri?=null
    var uri2:URI?=null
    lateinit var pdfVisor:PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visor_pdf)
        pdfVisor=findViewById(R.id.visorPdf)
        val boton=findViewById<FloatingActionButton>(R.id.shareit)
        val datos=intent.extras
        if (datos!=null){
            uri2=URI.create(datos.getString("ruta",""))
            uri=Uri.parse(datos.getString("ruta",""))
        }
        if (uri!=null){
            pdfVisor.fromUri(uri)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load()
        }
        boton.setOnClickListener {
            val file=File(uri2)
            val ruta=FileProvider.getUriForFile(this,"com.progra3.puntoventa",file)
            val share=Intent().apply {
                action=Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM,ruta)
                type="application/pdf"
            }
            startActivity(Intent.createChooser(share,"Seleccione Aplicacion"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (uri2!=null){
            val file=File(uri2)
            val res=file.delete()
            if(res)Toast.makeText(this,"PROCESOS COMPLETOS",Toast.LENGTH_SHORT).show()
        }
    }
}