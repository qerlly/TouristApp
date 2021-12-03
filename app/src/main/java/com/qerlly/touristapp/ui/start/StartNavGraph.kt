package com.qerlly.touristapp.ui.start

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qerlly.touristapp.ui.start.screens.ForgotPasswordScreen
import com.qerlly.touristapp.ui.start.screens.LoginScreen
import com.qerlly.touristapp.ui.start.screens.RegistrationScreen

@Composable
fun StartNavGraph(
    controller: NavHostController,
    viewModel: StartViewModel,
    login: (String, String) -> Unit,
    register: (String, String) -> Unit,
    forgot: (String) -> Unit
) = NavHost(modifier = Modifier.padding(16.dp), navController = controller, startDestination = Destinations.LOGIN_SCREEN) {
    composable(Destinations.LOGIN_SCREEN) {
        viewModel.clearFlags()
        LoginScreen(controller, viewModel, login)
    }
    composable(Destinations.REGISTRATION_SCREEN) {
        viewModel.clearFlags()
        RegistrationScreen(controller, viewModel, register)
    }
    composable(Destinations.FORGOT_SCREEN) {
        viewModel.clearFlags()
        ForgotPasswordScreen(controller, viewModel, forgot)
    }
}

object Destinations {
    const val LOGIN_SCREEN = "login"
    const val REGISTRATION_SCREEN = "registration"
    const val FORGOT_SCREEN = "forgot_password"
}