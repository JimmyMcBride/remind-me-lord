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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.jimmymcbride.remindmelord.domain.models.Verse
import dev.jimmymcbride.remindmelord.presentation.ui.theme.PADDING_MED
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    searchQueryState: State<String>,
    selectedTagsState: State<List<String>>,
    versePagingFlow: Flow<PagingData<Verse>>,
    allTagsState: State<List<String>>,
    getAllTags: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    navigateToAddVerseScreen: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onToggleTag: (String) -> Unit,
    currentBackstackEntry: NavBackStackEntry?,
) {
    val searchQuery by searchQueryState
    val selectedTags by selectedTagsState
    val allTags by allTagsState
    val verses = versePagingFlow.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }
    val currentBackStackEntry = remember { currentBackstackEntry }
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    LaunchedEffect(Unit) {
        getAllTags()
    }


    LaunchedEffect(savedStateHandle) {
        val wasVerseAdded = savedStateHandle?.get<Boolean>("verse_added") == true
        if (wasVerseAdded) {
            savedStateHandle.remove<Boolean>("verse_added")
            snackbarHostState.showSnackbar("Verse added successfully!")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Smart tag sorting: selected tags first (in order), then the rest alphabetically
            val tags = buildList {
                addAll(selectedTags)
                addAll(allTags.filterNot { it in selectedTags }.sorted())
            }

            // Tag filter row
            TagFilterRow(
                tags = tags,
                selected = selectedTags.toSet(),
                onToggle = onToggleTag
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(verses.itemCount) { index ->
                    val verse = verses[index]
                    if (verse != null) {
                        VerseItem(verse)
                    }
                }

                when (verses.loadState.append) {
                    is LoadState.Loading -> item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }

                    is LoadState.Error -> item {
                        Text("Error loading more", color = MaterialTheme.colorScheme.error)
                    }

                    else -> {}
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
