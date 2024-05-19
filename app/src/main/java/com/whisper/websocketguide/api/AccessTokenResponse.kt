package com.whisper.websocketguide.api

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("message") val message: String,
    @SerializedName("access_token") val accessToken: String
)