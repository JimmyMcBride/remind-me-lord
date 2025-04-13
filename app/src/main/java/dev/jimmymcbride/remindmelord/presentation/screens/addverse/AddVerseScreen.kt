package dev.jimmymcbride.remindmelord.presentation.screens.addverse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.jimmymcbride.remindmelord.presentation.ui.theme.PADDING_MED
import dev.jimmymcbride.remindmelord.presentation.ui.theme.SPACE_MED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVerseScreen(
    text: String,
    reference: String,
    tags: String,
    onTextChanged: (String) -> Unit,
    onReferenceChanged: (String) -> Unit,
    onTagsChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    uiEvents: Flow<AddVerseViewModel.UiEvent>,
    navigateBack: () -> Unit,
    verseAdded: () -> Unit,
) {


    LaunchedEffect(Unit) {
        uiEvents.collectLatest { event ->
            when (event) {
                is AddVerseViewModel.UiEvent.ShowSnackbar -> {
                    verseAdded()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Add Verse") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(PADDING_MED),
            verticalArrangement = Arrangement.spacedBy(SPACE_MED)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChanged,
                label = { Text("Verse Text") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = reference,
                onValueChange = onReferenceChanged,
                label = { Text("Reference (e.g. John 3:16)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tags,
                onValueChange = onTagsChanged,
                label = { Text("Tags (comma-separated)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onSubmit,
                modifier = Modifier.align(Alignment.End),
                enabled = text.isNotBlank() && reference.isNotBlank()
            ) {
                Text("Add Verse")
            }
        }
    }
}
