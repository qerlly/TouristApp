package com.qerlly.touristapp.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.qerlly.touristapp.R

@Composable
fun MainAppBar(navController: NavHostController) {

    val navState = navController.currentBackStackEntryFlow.collectAsState(null)

    TopAppBar(elevation = 0.dp, backgroundColor = Color.Transparent) {
        if (!checkDestination(navState.value?.destination?.route)) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.app_name),
                    tint = MaterialTheme.colors.primary
                )
            }
            Text(
                text = appBarTitle(navState.value?.destination?.route),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 12.dp),
            )
        } else {
            AppBarButtons(navController)
        }
    }
}

@Composable
fun AppBarButtons(navController: NavHostController) {
    Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(start = 12.dp),
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        IconButton(onClick = { navController.navigate(Destinations.USER_SCREEN) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        } }) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_person_24),
                contentDescription = stringResource(R.string.user),
                tint = MaterialTheme.colors.primary
            )
        }
        IconButton(onClick = { navController.navigate(Destinations.FAQ_SCREEN) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        } }) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_contact_support_24),
                contentDescription = stringResource(R.string.faq),
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun appBarTitle(destinationName: String?): String = when(destinationName) {
    Destinations.USER_SCREEN -> stringResource(R.string.user)
    Destinations.FAQ_SCREEN -> stringResource(R.string.faq)
    else -> stringResource(R.string.app_name)
}

private fun checkDestination(destinationName: String?): Boolean =
    destinationName != Destinations.USER_SCREEN && destinationName != Destinations.FAQ_SCREEN