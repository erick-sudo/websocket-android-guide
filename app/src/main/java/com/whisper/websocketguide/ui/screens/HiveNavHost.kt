package com.whisper.websocketguide.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.whisper.websocketguide.models.LoginUIState
import com.whisper.websocketguide.viewmodels.AuthViewModel

@Composable
fun HiveNavHost(
    authViewModel: AuthViewModel
) {

    val loginUIState = authViewModel.loginUIStateFlow.collectAsState(LoginUIState())

    val navHostController = rememberNavController()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Crossfade(targetState = loginUIState, label = "Auth cross fade") { loginState ->
                when(loginState.value.currentUser) {
                    null -> Login(authViewModel = authViewModel)
                    else -> Home(authViewModel = authViewModel)
                }
            }
        }
    }
}