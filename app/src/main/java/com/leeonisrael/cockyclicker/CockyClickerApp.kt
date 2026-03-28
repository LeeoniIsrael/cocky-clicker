package com.leeonisrael.cockyclicker

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.leeonisrael.cockyclicker.ui.screens.GameScreen
import com.leeonisrael.cockyclicker.ui.screens.StatsScreen
import com.leeonisrael.cockyclicker.ui.theme.CockyClickerTheme
import com.leeonisrael.cockyclicker.viewmodel.GameViewModel

sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Game : Screen("game", "Play", { Icon(Icons.Default.PlayArrow, contentDescription = null) })
    object Stats : Screen("stats", "Stats", { Icon(Icons.Default.Info, contentDescription = null) })
}

@Composable
fun CockyClickerApp() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel()

    CockyClickerTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    val items = listOf(Screen.Game, Screen.Stats)

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = screen.icon,
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Game.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Game.route) { GameScreen(viewModel) }
                composable(Screen.Stats.route) { StatsScreen(viewModel) }
            }
        }
    }
}
