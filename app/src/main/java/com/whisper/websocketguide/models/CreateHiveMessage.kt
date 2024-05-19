package com.whisper.websocketguide.models

import com.google.gson.annotations.SerializedName

data class CreateHiveMessage(
    @SerializedName("message") var message: HiveMessageIn
)

data class HiveMessageIn(
    @SerializedName("text") var text: String,
    @SerializedName("conversation_id") val conversationId: Int,
    @SerializedName("user_id") val userId: Int
)