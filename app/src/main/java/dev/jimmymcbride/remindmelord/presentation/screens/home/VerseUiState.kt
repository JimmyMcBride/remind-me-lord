package dev.jimmymcbride.remindmelord.presentation.screens.home

import dev.jimmymcbride.remindmelord.domain.models.Verse

data class VerseUiState(
    val allVerses: List<Verse> = emptyList(),
    val searchQuery: String = "",
    val selectedTags: Set<String> = emptySet(),
    val filteredVerses: List<Verse> = emptyList()
)
