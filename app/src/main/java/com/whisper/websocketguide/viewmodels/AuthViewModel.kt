package com.whisper.websocketguide.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whisper.websocketguide.api.AccessTokenRequest
import com.whisper.websocketguide.api.ApiCallWrapper
import com.whisper.websocketguide.models.HiveConversation
import com.whisper.websocketguide.models.HiveCurrentUser
import com.whisper.websocketguide.repository.HiveRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val hiveRepository = HiveRepository()

    private var _currentUserFlow = MutableSharedFlow<HiveCurrentUser>()

    val currentUserFlow
        get() = _currentUserFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            ApiCallWrapper.call(
                apiCall = {
                    hiveRepository.login(AccessTokenRequest("erick", "password"))
                }
            ) { errorLevel, code, responseBody, exceptionMessage ->
                val responseMessage: String = responseBody?.toString() ?: ""
                Log.d("API_ERROR", "ERROR_LEVEL $errorLevel, STATUS_CODE: $code, RESPONSE_BODY: $responseBody, ERROR_MESSAGE: $exceptionMessage")
            }?.let { user ->

                Log.d("API-RESPONSE", user.toString())
            }
        }
    }
}