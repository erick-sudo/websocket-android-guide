package com.whisper.websocketguide.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.whisper.websocketguide.api.AccessTokenResponse
import com.whisper.websocketguide.api.ApiCallWrapper
import com.whisper.websocketguide.models.ActionCableIdentifier
import com.whisper.websocketguide.models.ActionCableSubscribeMessage
import com.whisper.websocketguide.models.CreateHiveConversation
import com.whisper.websocketguide.models.CreateHiveMessage
import com.whisper.websocketguide.models.HiveConversation
import com.whisper.websocketguide.models.HiveMessage
import com.whisper.websocketguide.repository.HiveRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.serialization.gson.GsonWebsocketContentConverter
import io.ktor.websocket.Frame
import io.ktor.websocket.FrameType
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class ConversationsViewModel: ViewModel() {

    private val hiveRepository = HiveRepository()

    private val newMessagesReceiverChannel: ReceiveChannel<CreateHiveMessage> = Channel()
    private val newConversationsReceiverChannel: ReceiveChannel<CreateHiveConversation> = Channel()
    private val conversationsSubscriptionStagingChannel: Channel<ActionCableSubscribeMessage> = Channel()
    private val conversationMessagesSubscriptionStagingChannel: Channel<ActionCableSubscribeMessage> = Channel()

    private var _conversationsFlow = MutableStateFlow<List<HiveConversation>>(emptyList())

    private var _activeConversationFlow = MutableStateFlow<HiveConversation?>(null)

    val conversationsFlow
        get() = _conversationsFlow.asStateFlow()

    val activeConversationFlow
        get() = _activeConversationFlow.asStateFlow()

    suspend fun updateActiveConversation(conversationId: Int) {
        _activeConversationFlow.value = conversationsFlow.value.find { it.id == conversationId }
    }

    suspend fun updateActiveConversation(conversation: HiveConversation) {
        _activeConversationFlow.value = conversation
    }

    suspend fun fetchConversations(accessTokenResponse: AccessTokenResponse) {
        viewModelScope.launch {
            ApiCallWrapper.call(
                apiCall = {
                    hiveRepository.fetchConversations("Bearer ${accessTokenResponse.accessToken}")
                }
            ) { errorLevel, code, responseBody, exceptionMessage ->
                Log.d("API_ERROR", "ERROR_LEVEL $errorLevel, STATUS_CODE: $code, RESPONSE_BODY: $responseBody, ERROR_MESSAGE: $exceptionMessage")
            }?.let { conversations ->
                _conversationsFlow.value = conversations.map { conv -> conv.apply { messages.map { it.viewed = true } } }.toMutableList()
                // Subscribe to the messages channels
                conversations.forEach { conversation ->
                    subscribeToMessagesChannelOfConversation(conversation)
                }
                Log.d("API-RESPONSE", conversations.toString())
            }

            subscribeToConversationsChannel()
        }
    }

    /**
     * Subscribe to the conversations channel
     */
    private suspend fun subscribeToConversationsChannel() {
        conversationsSubscriptionStagingChannel.send(
            ActionCableSubscribeMessage(
                "subscribe",
                ActionCableIdentifier(
                    "ConversationsChannel",
                )
            )
        )
    }


    private suspend fun subscribeToMessagesChannelOfConversation(conversation: HiveConversation) {
        conversationMessagesSubscriptionStagingChannel.send(
            ActionCableSubscribeMessage(
                "subscribe",
                ActionCableIdentifier(
                    "MessagesChannel",
                    mapOf("conversation" to conversation.id.toString())
                )
            )
        )
    }

    suspend fun createNewConversation(accessTokenResponse: AccessTokenResponse, payload: CreateHiveConversation) {
        ApiCallWrapper.call(
            apiCall = {
                hiveRepository.createConversation("Bearer ${accessTokenResponse.accessToken}", payload)
            }
        ) { errorLevel, code, responseBody, exceptionMessage ->
            Log.d("API_ERROR", "ERROR_LEVEL $errorLevel, STATUS_CODE: $code, RESPONSE_BODY: $responseBody, ERROR_MESSAGE: $exceptionMessage")
        }
    }

    suspend fun createNewMessage(accessTokenResponse: AccessTokenResponse, payload: CreateHiveMessage) {
        ApiCallWrapper.call(
            apiCall = {
                hiveRepository.createMessage("Bearer ${accessTokenResponse.accessToken}", payload)
            }
        ) { errorLevel, code, responseBody, exceptionMessage ->
            Log.d("API_ERROR", "ERROR_LEVEL $errorLevel, STATUS_CODE: $code, RESPONSE_BODY: $responseBody, ERROR_MESSAGE: $exceptionMessage")
        }
    }

    suspend fun DefaultClientWebSocketSession.receiveConversation() {
        val hiveConversation = receiveDeserialized<HiveConversation>()
        Log.d("RECEIVED-CONV", "$hiveConversation")
    }

    suspend fun initWebSocket() = coroutineScope {
        try {
            WEBSOCKET_CLIENT_INSTANCE.webSocket(
                method = HttpMethod.Get,
                host = "10.0.2.2",
                port = 3000,
                path = "/cable"
            ) {
                val incomingMessagesCoroutine = launch {
                    for (frame in incoming) {
                        if(frame.frameType == FrameType.TEXT) {
                            val jsonElement = try {
                                val frameText = (frame as? Frame.Text)?.readText() ?: ""
                                JsonParser.parseString(frameText)
                            } catch (e: JsonParseException) {
                                //
                                Log.d("HIVE_FRAMES_JsonParseException", e.toString())
                                null
                            } catch (e: JsonSyntaxException) {
                                //
                                Log.d("HIVE_FRAMES_JsonSyntaxException", e.toString())
                                null
                            }
                            if(jsonElement != null) {
                                if(jsonElement.isJsonObject) {
                                    val jsonObject = jsonElement.asJsonObject
                                    if(jsonObject.has("identifier") && jsonObject.has("message")) {
                                        val identifierJsonObject = JsonParser.parseString(jsonObject["identifier"].asString.replace("\\\"", "\"")).asJsonObject
                                        if(identifierJsonObject["channel"].toString() == "\"MessagesChannel\"") {
                                            val conversationId = identifierJsonObject["conversation"].toString()
                                            val conversationMessageElement = jsonObject["message"].asJsonObject["message"]
                                            val hiveMessage = Gson().fromJson(conversationMessageElement, HiveMessage::class.java)
                                            _conversationsFlow.value = (listOf<HiveConversation>() + _conversationsFlow.value).map { conversation ->
                                                if(conversation.id == hiveMessage.conversationId) {
                                                    conversation.messages.add(hiveMessage)
                                                    if(_activeConversationFlow.value != null && conversation.id == _activeConversationFlow.value!!.id) {
                                                        updateActiveConversation(conversation = conversation)
                                                    }
                                                    conversation
                                                } else {
                                                    conversation
                                                }
                                            }
                                            Log.d("HIVE_FRAMES_NEW_MESSAGE", "MESSAGE RECEIVED")
                                        } else if (identifierJsonObject["channel"].toString() == "\"ConversationsChannel\"") {
                                            val conversationJsonElement = jsonObject["message"].asJsonObject["conversation"]
                                            val hiveConversation = Gson().fromJson(conversationJsonElement, HiveConversation::class.java)
                                            val prev = (listOf<HiveConversation>() + _conversationsFlow.value).toMutableList()
                                            _conversationsFlow.value = prev.apply { add(0, hiveConversation) }.toList()
                                            subscribeToMessagesChannelOfConversation(hiveConversation)
                                            Log.d("HIVE_FRAMES_RESUB", "SUBSCRIPTION_SENT")
                                        }
                                    }
                                } else {
                                    Log.d("HIVE_FRAMES", "$jsonElement" )
                                }
                            }

                        }
                    }
                }

                val outgoingMessagesCoroutine = launch {
                    for (createHiveMessage in newMessagesReceiverChannel) {
                        sendSerialized(createHiveMessage)
                    }
                }

                val outgoingConversationsCoroutine = launch {
                    for(createHiveConversation in newConversationsReceiverChannel) {
                        sendSerialized(createHiveConversation)
                    }
                }

                val conversationsSubscriptionCoroutine = launch {
                    for(subscription in conversationsSubscriptionStagingChannel) {
                        send(subscription.toSubscriptionCommand())
                    }
                }

                val conversationMessagesSubscriptionCoroutine = launch {
                    for(subscription in conversationMessagesSubscriptionStagingChannel) {
                        send(subscription.toSubscriptionCommand())
                    }
                }

                joinAll(
                    incomingMessagesCoroutine,
                    outgoingMessagesCoroutine,
                    outgoingConversationsCoroutine,
                    conversationMessagesSubscriptionCoroutine,
                    conversationsSubscriptionCoroutine
                )
            }
        } catch (e: Exception) {
            // Websocket exceptions
            Log.d("WEBSOCKET-SESSION", e.toString())
        } finally {
            // Cancel all children
            coroutineContext.cancelChildren()
        }
    }

    companion object {
        private val WEBSOCKET_CLIENT_INSTANCE by lazy {
            HttpClient {
                install(WebSockets) {
                    contentConverter = GsonWebsocketContentConverter()
                }
            }
        }
    }
}