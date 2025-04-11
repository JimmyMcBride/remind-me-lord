package dev.jimmymcbride.remindmelord.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.jimmymcbride.remindmelord.presentation.screens.home.HomeScreen
import dev.jimmymcbride.remindmelord.presentation.screens.home.HomeViewModel
import dev.jimmymcbride.remindmelord.presentation.screens.settings.SettingsScreen
import dev.jimmymcbride.remindmelord.presentation.screens.settings.SettingsViewModel

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.name
    ) {
        composable(Routes.Home.name) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                uiState = homeViewModel.uiState,
                populateVerses = homeViewModel::populateVerses,
                navigateToSettingsScreen = { navController.navigate(Routes.Settings.name) },
                navigateToAddVerseScreen = { navController.navigate(Routes.Settings.name) },
                onSearchQueryChanged = homeViewModel::onSearchQueryChanged,
                onToggleTag = homeViewModel::onToggleTag,
            )
        }
        composable(Routes.Settings.name) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
