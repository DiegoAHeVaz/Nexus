package com.example.nexus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Chatopen : AppCompatActivity() {

    private val client = OkHttpClient()
    private val apiKey = "" //Aqui agregar la api de openai chatgpt -3.5 turbo

        private lateinit var inputText: EditText
    private lateinit var sendButton: Button
    private lateinit var responseText: TextView

    private val messages = mutableListOf<Map<String, String>>(
        mapOf("role" to "system", "content" to "Eres un asistente llamado Mindy, muy amigable, curioso y siempre dispuesto a ayudar, te contactaremos para ser un apoyo para alunmos o jovenes con tda tdah y toc, no exigiras cambios acompañaras en ellos, no te basaras en castigos o listas rigidas, te debes de adaptar al usuario y no el a ti, genera consejos breves dinamicos y positivos, da recordatorios amigables y seguimiento diario, analiza el ritmo de usuario y su comportamiento para definir tareas ajustadas a el, todo esto con una forma de comunicacion educativa  .")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatopen)

        val boton = findViewById<Button>(R.id.regreso)
        boton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        inputText = findViewById(R.id.inputText)
        sendButton = findViewById(R.id.sendButton)
        responseText = findViewById(R.id.responseText)

        sendButton.setOnClickListener {
            val userInput = inputText.text.toString().trim()
            if (userInput.isNotEmpty()) {

                responseText.append("\n\nTú: $userInput")

                messages.add(mapOf("role" to "user", "content" to userInput))

                askChatGPT { reply ->
                    runOnUiThread {
                        responseText.append("\nMindy: $reply")
                    }
                }

                inputText.text.clear()
            }
        }
    }

    private fun askChatGPT(callback: (String) -> Unit) {
        val url = "https://api.openai.com/v1/chat/completions"

        val jsonMessages = JSONArray()
        for (msg in messages) {
            val messageObject = JSONObject()
            messageObject.put("role", msg["role"])
            messageObject.put("content", msg["content"])
            jsonMessages.put(messageObject)
        }

        val json = JSONObject()
        json.put("model", "gpt-3.5-turbo")
        json.put("messages", jsonMessages)

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $apiKey")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                try {
                    val jsonObject = JSONObject(responseBody)

                    if (jsonObject.has("error")) {
                        val errorMessage = jsonObject.getJSONObject("error").getString("message")
                        callback("Error de la API: $errorMessage")
                        return
                    }

                    val choices = jsonObject.getJSONArray("choices")
                    val reply = choices.getJSONObject(0).getJSONObject("message").getString("content")

                    messages.add(mapOf("role" to "assistant", "content" to reply))
                    callback(reply.trim())

                } catch (e: Exception) {
                    e.printStackTrace()
                    callback("Error al procesar respuesta: ${e.message}")
                }
            }

        })
    }
}
