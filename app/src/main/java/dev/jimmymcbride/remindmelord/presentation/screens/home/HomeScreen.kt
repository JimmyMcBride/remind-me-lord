package dev.jimmymcbride.remindmelord.presentation.screens.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jimmymcbride.remindmelord.domain.models.Verse
import dev.jimmymcbride.remindmelord.presentation.ui.theme.PADDING_MED

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: State<VerseUiState>,
    populateVerses: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    navigateToAddVerseScreen: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onToggleTag: (String) -> Unit,
) {
    val state by uiState

    LaunchedEffect(Unit) {
        populateVerses()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Remind Me, Lord") },
                actions = {
                    IconButton(onClick = navigateToSettingsScreen) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToAddVerseScreen() }) {
                Icon(Icons.Default.Add, contentDescription = "Add verse")
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(PADDING_MED)
        ) {
            // Search bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Tag filter row
            TagFilterRow(
                tags = getAllTagsFromVerses(state.allVerses),
                selected = state.selectedTags,
                onToggle = onToggleTag
            )

            Spacer(Modifier.height(16.dp))

            // Verse list
            LazyColumn {
                items(state.filteredVerses) { verse ->
                    VerseItem(verse)
                }
            }
        }
    }
}

@Composable
fun TagFilterRow(
    tags: List<String>,
    selected: Set<String>,
    onToggle: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tags) { tag ->
            FilterChip(
                selected = tag in selected,
                onClick = { onToggle(tag) },
                label = { Text(tag) }
            )
        }
    }
}

@Composable
fun VerseItem(verse: Verse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = verse.reference, style = MaterialTheme.typography.titleLarge)
            Text(text = verse.text, style = MaterialTheme.typography.bodyLarge)
            if (verse.tags.isNotEmpty()) {
                Text(
                    text = verse.tags.joinToString(", "),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

fun getAllTagsFromVerses(verses: List<Verse>): List<String> {
    return verses.flatMap { it.tags }
        .distinct()
        .sorted()
}
