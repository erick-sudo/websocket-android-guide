package com.whisper.websocketguide.api

import com.google.gson.annotations.SerializedName

data class AccessTokenRequest(
    @SerializedName("username") var username: String,
    @SerializedName("password") var paaaword: String,
)