package com.whisper.websocketguide.models

import com.whisper.websocketguide.api.AccessTokenResponse

data class LoginUIState(
    var currentUser: HiveCurrentUser? = null,
    var accessToken: AccessTokenResponse? = null
)