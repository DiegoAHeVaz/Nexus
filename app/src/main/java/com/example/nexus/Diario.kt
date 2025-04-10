package com.example.nexus

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class Diario : AppCompatActivity() {

    private lateinit var entryEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var entryListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val entries = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diario)

        entryEditText = findViewById(R.id.entryEditText)
        saveButton = findViewById(R.id.saveButton)
        entryListView = findViewById(R.id.entryListView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, entries)
        entryListView.adapter = adapter

        loadEntries()

        saveButton.setOnClickListener {
            val text = entryEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                val fullEntry = "$date\n$text"
                entries.add(0, fullEntry)
                adapter.notifyDataSetChanged()
                saveEntries()
                entryEditText.text.clear()
                Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Escribe algo primero", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEntries() {
        val prefs = getSharedPreferences("Diario", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putStringSet("Notas", entries.toSet())
        editor.apply()
    }

    private fun loadEntries() {
        val prefs = getSharedPreferences("Diario", Context.MODE_PRIVATE)
        val savedSet = prefs.getStringSet("Notas", emptySet()) ?: emptySet()
        entries.addAll(savedSet)
        entries.sortDescending()
        adapter.notifyDataSetChanged()
    }
}
