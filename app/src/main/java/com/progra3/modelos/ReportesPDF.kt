package com.progra3.modelos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.progra3.puntoventa.visorPdf
import java.io.File
import java.io.FileOutputStream
import java.sql.Date
import java.sql.Time
import kotlin.collections.ArrayList

class ReportesPDF(val context: Context) {
    lateinit var pdfFile:File
    lateinit var document:Document
    lateinit var pdfWriter:PdfWriter
    lateinit var parrafo:Paragraph
    var fTitle=Font(Font.FontFamily.TIMES_ROMAN,20F,Font.BOLD)
    var fSubTitle=Font(Font.FontFamily.TIMES_ROMAN,18F,Font.BOLD)
    var fSubTitle2=Font(Font.FontFamily.TIMES_ROMAN,14F,Font.BOLD)
    var fText=Font(Font.FontFamily.TIMES_ROMAN,12F,Font.BOLD)
    var fHighTitle=Font(Font.FontFamily.TIMES_ROMAN,15F,Font.BOLD, BaseColor.RED)

    fun viewPdf(){
        val intent=Intent(context,visorPdf::class.java)
        intent.putExtra("ruta",rutaUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun rutaUri():String{
        return "file://"+pdfFile.absolutePath
    }

    fun createFile(){
        val f=context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        //val timestamp= "Reporte"//+ Date(System.currentTimeMillis())//Date(System.currentTimeMillis()).toString()+Time(System.currentTimeMillis()).toString() //SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val nombreFichero="REPORTE"
        pdfFile=File.createTempFile(nombreFichero,".pdf",f)
    }

    fun closeDocument(){document.close()}

    fun openDocument(){
        createFile()
        document= Document(PageSize.LETTER)
        pdfWriter= PdfWriter.getInstance(document,FileOutputStream(pdfFile))
        document.open()
    }

    fun addMetaData(titulo:String, subTitulo:String, autor:String){
        document.addTitle(titulo)
        document.addSubject(subTitulo)
        document.addAuthor(autor)
    }

    fun addTitles(titulo: String, subTitulo: String, fecha: String){
        parrafo= Paragraph()
        addChild(Paragraph(titulo,fTitle))
        addChild(Paragraph(subTitulo,fSubTitle))
        addChild(Paragraph("Fecha: "+fecha,fHighTitle))
        parrafo.spacingAfter=30F
        document.add(parrafo)
    }

    fun addParrafo(texto:String){
        parrafo=Paragraph(texto,fText)
        parrafo.spacingAfter=5F
        parrafo.spacingBefore=5F
        document.add(parrafo)
    }

    fun createTable(header:Array<String>, clientes: ArrayList<Array<String>> ){
        parrafo= Paragraph()
        parrafo.font=fText
        var pdfTable=PdfPTable(header.size)
        pdfTable.widthPercentage=100F
        var pdfCell:PdfPCell
        var index=0

        while(index<header.size){
            pdfCell= PdfPCell(Phrase(header[index++],fSubTitle2))
            pdfCell.horizontalAlignment=Element.ALIGN_CENTER
            pdfCell.backgroundColor= BaseColor.GREEN
            pdfTable.addCell(pdfCell)
        }

        var itetador=clientes.iterator()
        while (itetador.hasNext()){
            var siguiente=itetador.next()
            var siguiente2=siguiente.iterator()
            while(siguiente2.hasNext()){
                var linea=siguiente2.next()
                pdfCell= PdfPCell(Phrase(linea))
                pdfCell.horizontalAlignment=Element.ALIGN_CENTER
                pdfCell.fixedHeight=20F
                pdfTable.addCell(pdfCell)
            }
        }
        parrafo.add(pdfTable)
        document.add(parrafo)
    }

    fun addChild(childParagraph: Paragraph){
        childParagraph.alignment=Element.ALIGN_CENTER
        parrafo.add(childParagraph)
    }
}