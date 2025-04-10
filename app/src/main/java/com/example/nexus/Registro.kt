package com.example.nexus

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Registro : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth

    private lateinit var radioGroupDiagnosis: RadioGroup
    private lateinit var radioGroupFocus: RadioGroup
    private lateinit var radioGroupConcentration: RadioGroup
    private lateinit var radioGroupImproveWithNexus: RadioGroup
    private lateinit var radioGroupCellphoneImpact: RadioGroup

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        // Inicialización
        auth = FirebaseAuth.getInstance()
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

            // Validaciones básicas
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar selección de radios
            if (radioGroupDiagnosis.checkedRadioButtonId == -1 ||
                radioGroupFocus.checkedRadioButtonId == -1 ||
                radioGroupConcentration.checkedRadioButtonId == -1 ||
                radioGroupImproveWithNexus.checkedRadioButtonId == -1 ||
                radioGroupCellphoneImpact.checkedRadioButtonId == -1) {

                Toast.makeText(this, "Selecciona una opción en cada categoría", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener textos seleccionados
            val selectedDiagnosis = findViewById<RadioButton>(radioGroupDiagnosis.checkedRadioButtonId).text.toString()
            val selectedFocus = findViewById<RadioButton>(radioGroupFocus.checkedRadioButtonId).text.toString()
            val selectedConcentration = findViewById<RadioButton>(radioGroupConcentration.checkedRadioButtonId).text.toString()
            val selectedImproveWithNexus = findViewById<RadioButton>(radioGroupImproveWithNexus.checkedRadioButtonId).text.toString()
            val selectedCellphoneImpact = findViewById<RadioButton>(radioGroupCellphoneImpact.checkedRadioButtonId).text.toString()

            // Desactivar el botón para evitar múltiples registros
            btnRegister.isEnabled = false

            // Registrar al usuario
            registerUser(email, password, name, selectedDiagnosis, selectedFocus, selectedConcentration, selectedImproveWithNexus, selectedCellphoneImpact)
        }
    }

    private fun registerUser(
        email: String,
        password: String,
        name: String,
        diagnosis: String,
        focus: String,
        concentration: String,
        improveWithNexus: String,
        cellphoneImpact: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                btnRegister.isEnabled = true

                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "diagnosis" to diagnosis,
                        "focus" to focus,
                        "concentration" to concentration,
                        "improveWithNexus" to improveWithNexus,
                        "cellphoneImpact" to cellphoneImpact
                    )

                    FirebaseFirestore.getInstance().collection("users").document(currentUser!!.uid)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(this, "Error de registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
