package com.whisper.websocketguide.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whisper.websocketguide.viewmodels.AuthViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    HiveNavHost(authViewModel = authViewModel)
}