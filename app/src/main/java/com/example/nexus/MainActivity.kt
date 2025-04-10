package com.example.nexus

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginbtn: Button
    private lateinit var regisbtn: Button
    private lateinit var auth: FirebaseAuth

    private val predefinedEmail = "diegohernandezz2006@gmail.com"
    private val predefinedPassword = "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginbtn = findViewById(R.id.login_btn)
        regisbtn = findViewById(R.id.registro_btn)

        // Verificar si los datos están guardados en SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("userEmail", null)
        val savedPassword = sharedPreferences.getString("userPassword", null)

        // Si los datos están guardados, realizar login con esos datos
        if (savedEmail != null && savedPassword != null) {
            usernameInput.setText(savedEmail)
            passwordInput.setText(savedPassword)
            Toast.makeText(this, "Datos de usuario guardados, por favor inicia sesión", Toast.LENGTH_SHORT).show()
        }

        regisbtn.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        loginbtn.setOnClickListener {
            val email = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Primero, intenta login con los datos guardados en SharedPreferences
            if (savedEmail != null && savedPassword != null && email == savedEmail && password == savedPassword) {
                loginUser(email, password)
            }
            // Si no es exitoso, verifica con los datos predefinidos
            else if (email == predefinedEmail && password == predefinedPassword) {
                loginUser(email, password)
            }
            else {
                Toast.makeText(this, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error de autenticación: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
