package com.whisper.websocketguide.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class HiveMessage(
    @SerializedName("id") var id: Int,
    @SerializedName("text") var text: String,
    @SerializedName("viewed") var viewed: Boolean,
    @SerializedName("created_at") var createdAt: Date,
    @SerializedName("conversation_id") var conversationId: Int,
    @SerializedName("user_id") var userId: Int
)