package com.whisper.websocketguide.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whisper.websocketguide.api.AccessTokenResponse
import com.whisper.websocketguide.api.ApiCallWrapper
import com.whisper.websocketguide.models.HiveConversation
import com.whisper.websocketguide.repository.HiveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConversationsViewModel: ViewModel() {

    private val hiveRepository = HiveRepository()

    private var _conversationsFlow = MutableStateFlow<List<HiveConversation>>(emptyList())

    val conversationsFlow
        get() = _conversationsFlow.asStateFlow()

    suspend fun fetchConversations(accessTokenResponse: AccessTokenResponse) {
        viewModelScope.launch {
            ApiCallWrapper.call(
                apiCall = {
                    hiveRepository.fetchConversations("Bearer ${accessTokenResponse.accessToken}")
                }
            ) { errorLevel, code, responseBody, exceptionMessage ->
                Log.d("API_ERROR", "ERROR_LEVEL $errorLevel, STATUS_CODE: $code, RESPONSE_BODY: $responseBody, ERROR_MESSAGE: $exceptionMessage")
            }?.let { conversations ->
                _conversationsFlow.value = conversations
                Log.d("API-RESPONSE", conversations.toString())
            }
        }
    }
}