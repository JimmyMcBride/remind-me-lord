package dev.jimmymcbride.remindmelord.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jimmymcbride.remindmelord.data.local.seeder.SeedDatabase
import dev.jimmymcbride.remindmelord.domain.models.Verse
import dev.jimmymcbride.remindmelord.domain.repository.VerseRepository
import dev.jimmymcbride.remindmelord.domain.utils.AsyncState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val verseRepository: VerseRepository,
    private val seedDatabase: SeedDatabase
) : ViewModel() {
    private val _seedingState = mutableStateOf<AsyncState<String>>(AsyncState.Idle)
    val seedingState: State<AsyncState<String>> = _seedingState

    private val _uiState = mutableStateOf(VerseUiState())
    val uiState: State<VerseUiState> = _uiState

    fun seedVerseDatabase() {
        viewModelScope.launch {
            _seedingState.value = AsyncState.Loading
            try {
                seedDatabase.seedFromJson()
                _seedingState.value = AsyncState.Success("It is finished.")
            } catch (e: Exception) {
                _seedingState.value = AsyncState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun populateVerses() {
        viewModelScope.launch {
            val verses = verseRepository.getAll()
            _uiState.updateState {
                copy(allVerses = verses, filteredVerses = verses)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.updateState {
            copy(
                searchQuery = query,
                filteredVerses = applyFilters(
                    this.allVerses,
                    query,
                    this.selectedTags
                )
            )
        }
    }

    fun onToggleTag(tag: String) {
        _uiState.updateState {
            val newTags = selectedTags.toMutableSet().apply {
                if (contains(tag)) remove(tag) else add(tag)
            }
            copy(
                selectedTags = newTags,
                filteredVerses = applyFilters(
                    allVerses,
                    searchQuery,
                    newTags
                )
            )
        }
    }

    private fun applyFilters(
        verses: List<Verse>,
        query: String,
        selectedTags: Set<String>
    ): List<Verse> {
        return verses.filter { verse ->
            val matchesQuery = verse.text.contains(query, ignoreCase = true) ||
                    verse.reference.contains(query, ignoreCase = true)

            val matchesTags = selectedTags.isEmpty() || selectedTags.any { it in verse.tags }

            matchesQuery && matchesTags
        }
    }

    private fun <T> MutableState<T>.updateState(transform: T.() -> T) {
        this.value = this.value.transform()
    }
}
