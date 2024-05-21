package com.whisper.websocketguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.whisper.websocketguide.api.AccessTokenResponse
import com.whisper.websocketguide.models.HiveConversation
import com.whisper.websocketguide.viewmodels.AuthViewModel
import com.whisper.websocketguide.viewmodels.ConversationsViewModel

@Composable
fun ConversationListings(
    conversations: List<HiveConversation>,
    onSelect: (conversationId: Int) -> Unit
) {
    LazyColumn {
        items(conversations) {hiveConversation ->
            ConversationListItem(
                hiveConversation = hiveConversation,
                onClick = onSelect
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListItem(
    hiveConversation: HiveConversation,
    onClick: (conversationId: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable {
                onClick(hiveConversation.id)
            },
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(4.dp, MaterialTheme.colorScheme.onBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = hiveConversation.title.slice(0..1),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }
            Text(
                text = hiveConversation.title,
                modifier = Modifier
                    .padding(10.dp, 5.dp)
                    .weight(1f)
            )
            val unreadMessages = hiveConversation.messages.count { !it.viewed }
            if(unreadMessages > 0) {
                Badge {
                    Text(text = unreadMessages.toString())
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.8.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
}