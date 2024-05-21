package com.whisper.websocketguide.models

import com.google.gson.annotations.SerializedName

data class HiveConversation(
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("messages") var messages: MutableList<HiveMessage>
)