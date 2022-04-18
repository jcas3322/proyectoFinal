package com.progra3.puntoventa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonInventario=findViewById<Button>(R.id.botonInventario)
        val botonVentas=findViewById<Button>(R.id.botonVentas)

        botonVentas.setOnClickListener {
            startActivity(Intent(this,actividad_ventas::class.java))
        }
        botonInventario.setOnClickListener {
            startActivity(Intent(this,ActividadInventario::class.java))
        }
    }
}