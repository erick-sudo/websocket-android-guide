package com.whisper.websocketguide.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.whisper.websocketguide.ui.screens.NavRoutes

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {

    val backStackEntry by navHostController.currentBackStackEntryAsState()

    val navItems = listOf(
        NavRoutes.ConversationListings to Icons.Outlined.Person,
        NavRoutes.ConversationListings to Icons.Outlined.Notifications,
        NavRoutes.ConversationListings to Icons.Outlined.Favorite,
        NavRoutes.ConversationListings to Icons.Outlined.Call
    )

    NavigationBar(
        modifier = Modifier.then(modifier)
    ) {
        navItems.forEach { (route, imageVector) ->
            NavigationBarItem(
                selected = backStackEntry?.destination?.route == route.route,
                onClick = { NavRoutes.UseNavigate.navigate(navHostController, route) },
                icon = {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = route.route,
                    )
                }
            )
        }
    }
}
