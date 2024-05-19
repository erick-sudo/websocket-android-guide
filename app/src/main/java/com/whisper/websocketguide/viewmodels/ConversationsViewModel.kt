package com.whisper.websocketguide.viewmodels

import androidx.lifecycle.ViewModel
import com.whisper.websocketguide.models.HiveConversation
import kotlinx.coroutines.flow.MutableSharedFlow

class ConversationsViewModel: ViewModel() {

    var conversationsFlow = MutableSharedFlow<List<HiveConversation>>()
}