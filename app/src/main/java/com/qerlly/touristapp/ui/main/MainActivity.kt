package com.qerlly.touristapp.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.qerlly.touristapp.services.SettingsService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var settingsService: SettingsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen() }
    }

    @Composable
    fun MainScreen() = MdcTheme {
        val navController = rememberNavController()

        val navState = navController.currentBackStackEntryFlow.collectAsState(null)

        val currentRoute = navState.value?.destination?.route

        val isJoined = settingsService.getTour().collectAsState("")

        Scaffold(
            topBar = { MainAppBar(navController) },
            content = { MainNavGraph(navController, isJoined) },
            bottomBar = {
                if (currentRoute != Destinations.USER_SCREEN && currentRoute != Destinations.FAQ_SCREEN)
                    MainBottomBar(navController)
            }
        )
    }
}