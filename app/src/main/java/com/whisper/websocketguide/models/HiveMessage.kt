package com.whisper.websocketguide.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class HiveMessage(
    @SerializedName("id") val id: Int,
    @SerializedName("text") var text: String,
    @SerializedName("viewed") val viewed: Boolean,
    @SerializedName("created_at") var createdAt: Date,
    @SerializedName("conversation_id") val conversationId: Int,
    @SerializedName("user_id") val userId: Int
)