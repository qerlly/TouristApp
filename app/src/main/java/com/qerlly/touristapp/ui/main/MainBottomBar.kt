package com.qerlly.touristapp.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.qerlly.touristapp.R

@Composable
fun MainBottomBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 2.dp
    ) {

        val navState = navController.currentBackStackEntryFlow.collectAsState(null)

        val currentRoute = navState.value?.destination?.route

        NavigationItem.values().forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp).padding(2.dp),
                        painter = painterResource(item.icon),
                        contentDescription = stringResource(item.title)
                    ) },
                label = { Text(text = stringResource(item.title)) },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = MaterialTheme.colors.primary,
                onClick = { navController.navigate(item.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                } },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
            )
        }
    }
}

enum class NavigationItem(val route: String, val icon: Int, val title: Int) {
    ROADMAP(Destinations.ROADMAP_SCREEN, R.drawable.track, R.string.roadmap),
    TOURS(Destinations.TOURS_SCREEN, R.drawable.travel, R.string.tours),
    CHAT(Destinations.CHAT_SCREEN, R.drawable.chat, R.string.chat)
}