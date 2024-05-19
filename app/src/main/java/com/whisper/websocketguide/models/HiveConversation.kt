package com.whisper.websocketguide.models

import com.google.gson.annotations.SerializedName

data class HiveConversation(
    @SerializedName("title") var title: String,
    @SerializedName("messages") var messages: List<HiveMessage>
)