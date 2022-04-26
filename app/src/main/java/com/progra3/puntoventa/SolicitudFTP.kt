package com.progra3.puntoventa

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URI

class SolicitudFTP {
    fun enviarFoto(nombre:String, onResult:(String?)-> Unit){
        Thread(Runnable {
            try{
            var ftp2= FTPClient()
            println("Crear objeto FTP")
            ftp2.connect("192.168.1.3",21)
            ftp2.login("julioaguilar","Anastasia680")
            println("Conectar Servidor")
            val uri= URI.create(nombre)
            val archivo= File(uri)
            val input: InputStream = FileInputStream(archivo)
            println("Crear objeto archivo")
            ftp2.enterLocalPassiveMode()
            ftp2.setFileTransferMode(FTP.STREAM_TRANSFER_MODE)
            ftp2.setFileType(FTP.BINARY_FILE_TYPE)
            println("Cambiar a binario")
            var res=ftp2.storeFile("/Prueba/foto2.jpg",input)
            println("Guardar Archivo " +res)
            ftp2.logout()
            input.close()
            println("TERMINADO")
            onResult("Imagen Cargada")}
            catch (e:Exception){println("ERROR " + e.message)}
        }).start()
    }
}