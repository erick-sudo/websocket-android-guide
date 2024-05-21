package com.whisper.websocketguide.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.whisper.websocketguide.api.AccessTokenResponse
import com.whisper.websocketguide.models.CreateHiveMessage
import com.whisper.websocketguide.models.HiveMessageIn
import com.whisper.websocketguide.models.LoginUIState
import com.whisper.websocketguide.viewmodels.AuthViewModel
import com.whisper.websocketguide.viewmodels.ConversationsViewModel
import kotlinx.coroutines.launch

@Composable
fun HiveNavHost(
    navHostController: () -> NavHostController,
    accessToken: () -> AccessTokenResponse,
    conversationsViewModel: ConversationsViewModel = viewModel(),
) {

    val coroutineScope = rememberCoroutineScope()

    val conversationsStateFlow by conversationsViewModel.conversationsFlow.collectAsState()
    
    val activeConversationStateFlow by conversationsViewModel.activeConversationFlow.collectAsState()

    LaunchedEffect(key1 = "ConversationsStateFlowEffect") {
        accessToken().let { accessToken ->
            conversationsViewModel.fetchConversations(accessToken)
        }
    }

    LaunchedEffect(key1 = "ConversationsWebSocket") {
        conversationsViewModel.initWebSocket()
    }

    NavHost(
        navController = navHostController(),
        startDestination = NavRoutes.ConversationListings.route
    ) {
        composable(NavRoutes.ConversationListings.route) {
            ConversationListings(
                conversations = conversationsStateFlow,
                onSelect = { conversationId ->
                    run {
                        coroutineScope.launch {
                            conversationsViewModel.updateActiveConversation(conversationId)
                            NavRoutes.UseNavigate.navigate(navHostController(), NavRoutes.ConversationScreen.route + "/$conversationId")
                        }
                    }
                }
            )
        }

        composable(NavRoutes.ConversationScreen.route + "/{conversationId}") {backstackEntry ->
            val conversationId = backstackEntry.arguments?.getString("conversationId") ?: "0"

            Crossfade(activeConversationStateFlow != null, label = "") { fadeTarget ->
                when(fadeTarget) {
                    true -> ConversationScreen(
                        conversation = activeConversationStateFlow!!,
                        onBack = { NavRoutes.UseNavigate.navigate(navHostController(), NavRoutes.ConversationListings) },
                        onNewMessage = { text, conversationId -> run {
                            coroutineScope.launch {
                                conversationsViewModel.createNewMessage(accessToken(), CreateHiveMessage(message = HiveMessageIn(text, conversationId, 1)))
                            }
                        }}
                    )
                    else -> ConversationScreenPlaceHolder(
                        onBack = { NavRoutes.UseNavigate.navigate(navHostController(), NavRoutes.ConversationListings) },
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ConversationScreenPlaceHolder(
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Welcome to HIVE",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Start chatting with your friends, colleagues, or anyone you'd like. Feel free to create new conversations, send messages, and express yourself.")
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onBack
        ) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back to conversation listings")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Conversations")
        }
    }
}