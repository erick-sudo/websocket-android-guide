package com.whisper.websocketguide.models

import com.google.gson.annotations.SerializedName

data class CreateHiveConversation(
    @SerializedName("conversation") var conversation: HiveConversationIn
)

data class  HiveConversationIn(
    @SerializedName("title") var title: String
)