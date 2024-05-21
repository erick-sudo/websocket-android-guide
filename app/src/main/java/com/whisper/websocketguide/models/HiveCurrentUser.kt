package com.whisper.websocketguide.models

import com.google.gson.annotations.SerializedName

data class HiveCurrentUser(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("phone") var phone: String
)
