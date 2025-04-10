package com.example.nexus

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Home : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val chat = findViewById<Button>(R.id.chat)
        chat.setOnClickListener {
            val intent = Intent(this, Chatopen::class.java)
            startActivity(intent)
        }
        val calendario = findViewById<Button>(R.id.calendario)
        calendario.setOnClickListener {
            val intent = Intent(this, Calendario::class.java)
            startActivity(intent)
        }
        val diario = findViewById<Button>(R.id.diario)
        diario.setOnClickListener {
            val intent = Intent(this, Diario::class.java)
            startActivity(intent)
        }

    }
}
