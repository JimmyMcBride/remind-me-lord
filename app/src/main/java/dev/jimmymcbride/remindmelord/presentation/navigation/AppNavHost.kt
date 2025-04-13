package dev.jimmymcbride.remindmelord.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.jimmymcbride.remindmelord.presentation.screens.addverse.AddVerseScreen
import dev.jimmymcbride.remindmelord.presentation.screens.addverse.AddVerseViewModel
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
                searchQueryState = homeViewModel.searchQuery,
                selectedTagsState = homeViewModel.selectedTags,
                versePagingFlow = homeViewModel.versePagingFlow,
                allTagsState = homeViewModel.allTags,
                getAllTags = homeViewModel::getAllTags,
                navigateToSettingsScreen = { navController.navigate(Routes.Settings.name) },
                navigateToAddVerseScreen = { navController.navigate(Routes.AddVerse.name) },
                onSearchQueryChanged = homeViewModel::onSearchQueryChanged,
                onToggleTag = homeViewModel::onToggleTag,
                currentBackstackEntry = navController.currentBackStackEntry,
            )
        }
        composable(Routes.Settings.name) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                navigateBack = navController::popBackStack
            )
        }
        composable(Routes.AddVerse.name) {
            val addVerseViewModel: AddVerseViewModel = hiltViewModel()
            AddVerseScreen(
                text = addVerseViewModel.text,
                reference = addVerseViewModel.reference,
                tags = addVerseViewModel.tags,
                onTextChanged = addVerseViewModel::onTextChanged,
                onReferenceChanged = addVerseViewModel::onReferenceChanged,
                onTagsChanged = addVerseViewModel::onTagsChanged,
                onSubmit = addVerseViewModel::addVerse,
                uiEvents = addVerseViewModel.uiEvents,
                navigateBack = navController::popBackStack,
                verseAdded = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("verse_added", true)

                    navController.popBackStack()
                }
            )
        }
    }
}
