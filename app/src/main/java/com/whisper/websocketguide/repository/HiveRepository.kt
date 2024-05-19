package com.whisper.websocketguide.repository

import com.whisper.websocketguide.api.HiveAPI
import com.whisper.websocketguide.models.CreateHiveConversation
import com.whisper.websocketguide.models.CreateHiveMessage
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HiveRepository {

    suspend fun createConversation(conversation: CreateHiveConversation) = API_INSTANCE.createNewConversation(conversation)

    suspend fun fetchConversations() = API_INSTANCE.fetchConversations()

    suspend fun createMessage(message: CreateHiveMessage) = API_INSTANCE.createMessage(message)

    companion object {
        private val API_INSTANCE: HiveAPI by lazy {
            // Http Client
            val okHttpClient: OkHttpClient = OkHttpClient
                .Builder()
                .build()

            // Retrofit builder
            val retrofit: Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://10.0.2.2:3000")
                .client(okHttpClient)
                .build()

            // Initialize API
            retrofit.create(HiveAPI::class.java)
        }
    }
}