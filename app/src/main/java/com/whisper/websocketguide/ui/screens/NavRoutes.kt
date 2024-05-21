package com.whisper.websocketguide.ui.screens

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

sealed class NavRoutes(
    val route: String,
    val label: String
) {
    data object ConversationListings: NavRoutes("conversation-listings", "Conversations")

    data object ConversationScreen: NavRoutes("conversation-screen", "Conversation Screen")

    object UseNavigate {
        fun navigate(
            navHostController: NavHostController,
            destination: String
        ) {
            navHostController.navigate(destination) {
                navigationStateRestoration(navHostController)
            }
        }

        fun navigate(
            navHostController: NavHostController,
            destination: NavRoutes
        ) {
            navHostController.navigate(destination.route) {
                navigationStateRestoration(navHostController)
            }
        }

        private fun NavOptionsBuilder.navigationStateRestoration(navHostController: NavHostController) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}