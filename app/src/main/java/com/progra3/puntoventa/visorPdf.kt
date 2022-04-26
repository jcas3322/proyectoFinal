package com.progra3.puntoventa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView

class visorPdf : AppCompatActivity() {

    lateinit var pdfVisor:PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visor_pdf)
        pdfVisor=findViewById(R.id.visorPdf)

    }
}