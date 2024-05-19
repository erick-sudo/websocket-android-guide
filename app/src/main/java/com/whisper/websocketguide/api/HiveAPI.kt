package com.whisper.websocketguide.api

import com.whisper.websocketguide.models.CreateHiveConversation
import com.whisper.websocketguide.models.CreateHiveMessage
import com.whisper.websocketguide.models.HiveConversation
import com.whisper.websocketguide.models.HiveCurrentUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface HiveAPI {

    @POST("login")
    suspend fun login(@Body loginCredentials: AccessTokenRequest): AccessTokenResponse

    @GET("current-user")
    suspend fun fetchCurrentUser(@Header("Authorization") authorization: String): HiveCurrentUser

    @POST("conversations")
    suspend fun createNewConversation(@Header("Authorization") authorization: String, @Body conversation: CreateHiveConversation)

    @GET("conversations")
    suspend fun fetchConversations(@Header("Authorization") authorization: String): List<HiveConversation>

    @POST("messages")
    suspend fun createMessage(@Header("Authorization") authorization: String, @Body message: CreateHiveMessage)
}