package com.whisper.websocketguide.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.whisper.websocketguide.viewmodels.AuthViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = viewModel()
) {

    val loginUIState by authViewModel.loginUIStateFlow.collectAsState()

    val navHostController = rememberNavController()

    Scaffold(
//        bottomBar = {
//            BottomNavigationBar(
//                modifier = Modifier
//                    .padding(horizontal = 10.dp),
//                navHostController = navHostController
//            )
//        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Crossfade(targetState = loginUIState.currentUser, label = "Account cross fade") { hiveCurrentUser ->
                when(hiveCurrentUser == null || loginUIState.accessToken == null ) {
                    true -> Login(
                        authViewModel = authViewModel,
                        navHostController = navHostController
                    )
                    else -> HiveNavHost(
                        navHostController = { navHostController },
                        accessToken = { loginUIState.accessToken!! }
                    )
                }
            }
        }
    }
}