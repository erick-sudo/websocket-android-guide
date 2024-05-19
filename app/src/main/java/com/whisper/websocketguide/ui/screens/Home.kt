package com.whisper.websocketguide.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whisper.websocketguide.viewmodels.AuthViewModel
import com.whisper.websocketguide.viewmodels.ConversationsViewModel

@Composable
fun Home(
    authViewModel: AuthViewModel,
    conversationsViewModel: ConversationsViewModel = viewModel()
) {
    val auth by authViewModel.loginUIStateFlow.collectAsState()
    val conversationsStateFlow by conversationsViewModel.conversationsFlow.collectAsState()


    LaunchedEffect(key1 = "ConversationsStateFlowEffect") {
        auth.accessToken?.let { accessToken ->
            conversationsViewModel.fetchConversations(accessToken)
        }
    }

    LazyColumn {
        items(conversationsStateFlow) {hiveConversation ->
            Text(
                text = hiveConversation.title,
                modifier = Modifier
                    .padding(10.dp, 5.dp)
            )
        }
    }
}