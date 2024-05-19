package com.whisper.websocketguide.api

import com.whisper.websocketguide.models.CreateHiveConversation
import com.whisper.websocketguide.models.CreateHiveMessage
import com.whisper.websocketguide.models.HiveConversation
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HiveAPI {
    @POST("conversations")
    suspend fun createNewConversation(@Body conversation: CreateHiveConversation)

    @GET("conversations")
    suspend fun fetchConversations(): List<HiveConversation>

    @POST("messages")
    suspend fun createMessage(@Body message: CreateHiveMessage)
}