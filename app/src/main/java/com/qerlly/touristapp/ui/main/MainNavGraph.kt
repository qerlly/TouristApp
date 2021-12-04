package com.qerlly.touristapp.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qerlly.touristapp.ui.main.screens.FaqScreen
import com.qerlly.touristapp.ui.main.screens.ToursScreen
import com.qerlly.touristapp.ui.main.screens.UserScreen

@Composable
fun MainNavGraph(navController: NavHostController) =
    NavHost(
        navController = navController,
        startDestination = Destinations.TOURS_SCREEN
    ) {
        composable(Destinations.USER_SCREEN) { UserScreen() }
        composable(Destinations.CHAT_SCREEN) { }
        composable(Destinations.TOURS_SCREEN) { ToursScreen() }
        composable(Destinations.TOUR_SCREEN) { }
        composable(Destinations.ROADMAP_SCREEN) { }
        composable(Destinations.CHAT_SCREEN) { }
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