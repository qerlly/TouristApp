package com.qerlly.touristapp.ui.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qerlly.touristapp.R
import com.qerlly.touristapp.ui.main.screens.*

@Composable
fun MainNavGraph(navController: NavHostController, isJoined: State<String>) =
    NavHost(
        navController = navController,
        startDestination = if (isJoined.value.isEmpty()) Destinations.TOURS_SCREEN else Destinations.TOUR_SCREEN
    ) {
        composable(Destinations.USER_SCREEN) { UserScreen() }
        composable(Destinations.TOURS_SCREEN) { if (isJoined.value.isEmpty()) ToursScreen(navController) else TourScreen() }
        composable(Destinations.TOUR_SCREEN) { TourScreen() }
        composable(Destinations.CHAT_SCREEN) { if (isJoined.value.isEmpty()) ShowErrorToast() else ChatScreen() }
        composable(Destinations.FAQ_SCREEN) { FaqScreen() }
    }

@Composable
fun ShowErrorToast() {
    Toast.makeText(LocalContext.current, R.string.need_tour_chat, Toast.LENGTH_SHORT).show()
}

object Destinations {
    const val USER_SCREEN = "user"
    const val TOURS_SCREEN = "tours"
    const val TOUR_SCREEN = "tour"
    const val ROADMAP_SCREEN = "roadmap"
    const val CHAT_SCREEN = "chat"
    const val FAQ_SCREEN = "faq"
}