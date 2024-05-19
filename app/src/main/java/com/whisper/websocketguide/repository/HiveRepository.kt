package com.whisper.websocketguide.repository

import com.whisper.websocketguide.api.AccessTokenRequest
import com.whisper.websocketguide.api.HiveAPI
import com.whisper.websocketguide.models.CreateHiveConversation
import com.whisper.websocketguide.models.CreateHiveMessage
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HiveRepository {

    suspend fun login(credentials: AccessTokenRequest) = API_INSTANCE.login(credentials)

    suspend fun fetchCurrentUser(authorization: String) = API_INSTANCE.fetchCurrentUser(authorization)

    suspend fun createConversation(authorization: String, conversation: CreateHiveConversation) = API_INSTANCE.createNewConversation(authorization, conversation)

    suspend fun fetchConversations(authorization: String) = API_INSTANCE.fetchConversations(authorization)

    suspend fun createMessage(authorization: String, message: CreateHiveMessage) = API_INSTANCE.createMessage(authorization, message)

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