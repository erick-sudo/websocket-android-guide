package com.whisper.websocketguide.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.whisper.websocketguide.api.AccessTokenRequest
import com.whisper.websocketguide.api.AccessTokenResponse
import com.whisper.websocketguide.api.ApiCallWrapper
import com.whisper.websocketguide.models.LoginUIState
import com.whisper.websocketguide.repository.HiveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {
    private val hiveRepository = HiveRepository()

    private var _loginUIStateFlow = MutableStateFlow(LoginUIState())

    val loginUIStateFlow
        get() = _loginUIStateFlow.asStateFlow()

    private suspend fun fetchCurrentUser(accessTokenResponse: AccessTokenResponse) {
        ApiCallWrapper.call(
            apiCall = {
                hiveRepository.fetchCurrentUser("Bearer ${accessTokenResponse.accessToken}")
            }
        ) { errorLevel, code, responseBody, exceptionMessage ->
            Log.d("API_ERROR", "ERROR_LEVEL $errorLevel, STATUS_CODE: $code, RESPONSE_BODY: $responseBody, ERROR_MESSAGE: $exceptionMessage")
        }?.let { user ->
            _loginUIStateFlow.value = _loginUIStateFlow.value.copy(currentUser = user)
        }
    }

    suspend fun login(accessTokenRequest: AccessTokenRequest) {
        ApiCallWrapper.call(
            apiCall = {
                hiveRepository.login(accessTokenRequest)
            }
        ) { errorLevel, code, responseBody, exceptionMessage ->
            Log.d("API_ERROR", "ERROR_LEVEL $errorLevel, STATUS_CODE: $code, RESPONSE_BODY: $responseBody, ERROR_MESSAGE: $exceptionMessage")
        }?.let { accessToken ->
            _loginUIStateFlow.value = _loginUIStateFlow.value.copy(accessToken = accessToken)
            fetchCurrentUser(accessToken)
        }
    }
}