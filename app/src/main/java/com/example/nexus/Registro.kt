package com.example.nexus

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Registro : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var btnRegister: Button

    private lateinit var radioGroupDiagnosis: RadioGroup
    private lateinit var radioGroupConcentration: RadioGroup
    private lateinit var radioGroupImproveWithNexus: RadioGroup
    private lateinit var radioGroupCellphoneImpact: RadioGroup


    private var userEmail: String? = null
    private var userPassword: String? = null
    private var userName: String? = null
    private var userDiagnosis: String? = null
    private var userConcentration: String? = null
    private var userImproveWithNexus: String? = null
    private var userCellphoneImpact: String? = null

    @SuppressLint("MissingInflatedId", "UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        emailInput = findViewById(R.id.editTextEmail)
        passwordInput = findViewById(R.id.editTextPassword)
        nameInput = findViewById(R.id.editTextName)
        btnRegister = findViewById(R.id.buttonRegister)
        radioGroupDiagnosis = findViewById(R.id.radioGroupDiagnosis)
        radioGroupConcentration = findViewById(R.id.radioGroupConcentration)
        radioGroupImproveWithNexus = findViewById(R.id.radioGroupImproveWithNexus)
        radioGroupCellphoneImpact = findViewById(R.id.radioGroupCellphoneImpact)

        btnRegister.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val name = nameInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (radioGroupDiagnosis.checkedRadioButtonId == -1 ||
                radioGroupConcentration.checkedRadioButtonId == -1 ||
                radioGroupImproveWithNexus.checkedRadioButtonId == -1 ||
                radioGroupCellphoneImpact.checkedRadioButtonId == -1) {

                Toast.makeText(this, "Selecciona una opción en cada categoría", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val selectedDiagnosis = findViewById<RadioButton>(radioGroupDiagnosis.checkedRadioButtonId).text.toString()
            val selectedConcentration = findViewById<RadioButton>(radioGroupConcentration.checkedRadioButtonId).text.toString()
            val selectedImproveWithNexus = findViewById<RadioButton>(radioGroupImproveWithNexus.checkedRadioButtonId).text.toString()
            val selectedCellphoneImpact = findViewById<RadioButton>(radioGroupCellphoneImpact.checkedRadioButtonId).text.toString()


            userEmail = email
            userPassword = password
            userName = name
            userDiagnosis = selectedDiagnosis
            userConcentration = selectedConcentration
            userImproveWithNexus = selectedImproveWithNexus
            userCellphoneImpact = selectedCellphoneImpact


            val sharedPreferences: SharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("userEmail", email)
            editor.putString("userPassword", password)
            editor.putString("username", name)
            editor.apply()


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
