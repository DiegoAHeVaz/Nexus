package com.example.nexus

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Calendario : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var selectedDate: TextView

    // Mapa para guardar notas por fecha
    private val notesMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendario)

        val regresar = findViewById<Button>(R.id.regreso)
        regresar.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        calendarView = findViewById(R.id.calendarView)
        selectedDate = findViewById(R.id.selectedDate)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dateKey = "$dayOfMonth/${month + 1}/$year"
            selectedDate.text = "Fecha seleccionada: $dateKey"

            // Si ya hay nota, mostrarla
            if (notesMap.containsKey(dateKey)) {
                showNoteDialog(dateKey, notesMap[dateKey])
            } else {
                showAddNoteDialog(dateKey)
            }
        }
    }

    private fun showNoteDialog(date: String, note: String?) {
        AlertDialog.Builder(this)
            .setTitle("Nota para $date")
            .setMessage(note ?: "Sin nota")
            .setPositiveButton("Editar") { _, _ ->
                showAddNoteDialog(date, note)
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }

    private fun showAddNoteDialog(date: String, currentNote: String? = null) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null)
        val noteInput = dialogView.findViewById<EditText>(R.id.noteInput)
        val saveBtn = dialogView.findViewById<Button>(R.id.saveNoteBtn)

        noteInput.setText(currentNote ?: "")

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Agregar nota para $date")
            .create()

        saveBtn.setOnClickListener {
            val note = noteInput.text.toString()
            if (note.isNotEmpty()) {
                notesMap[date] = note
                Toast.makeText(this, "Nota guardada para $date", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}
