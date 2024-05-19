package com.whisper.websocketguide.models

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class Greeting {
    private val client = HttpClient()

    suspend fun greeting(): String {
        val response = client.get("http://10.0.2.2:3000")
        return response.bodyAsText()
    }
}