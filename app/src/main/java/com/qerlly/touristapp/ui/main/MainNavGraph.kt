package com.qerlly.touristapp.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qerlly.touristapp.ui.main.screens.*

@Composable
fun MainNavGraph(navController: NavHostController, isJoined: State<String>) =
    NavHost(
        navController = navController,
        startDestination = if (isJoined.value.isEmpty()) Destinations.TOURS_SCREEN else Destinations.TOUR_SCREEN
    ) {
        composable(Destinations.USER_SCREEN) { UserScreen() }
        composable(Destinations.CHAT_SCREEN) { ChatScreen() }
        composable(Destinations.TOURS_SCREEN) { if (isJoined.value.isEmpty()) ToursScreen(navController) else TourScreen(navController) }
        composable(Destinations.TOUR_SCREEN) { TourScreen(navController) }
        composable(Destinations.CHAT_SCREEN) { ChatScreen() }
        composable(Destinations.FAQ_SCREEN) { FaqScreen() }
    }

object Destinations {
    const val USER_SCREEN = "user"
    const val TOURS_SCREEN = "tours"
    const val TOUR_SCREEN = "tour"
    const val ROADMAP_SCREEN = "roadmap"
    const val CHAT_SCREEN = "chat"
    const val FAQ_SCREEN = "faq"
}