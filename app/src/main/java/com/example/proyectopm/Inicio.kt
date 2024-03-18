package com.example.proyectopm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val btnStart = findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener { navigateJuego() }
    }

    private fun navigateJuego() {
        val intent = Intent(this, Juego::class.java)
        startActivity(intent)
    }
}