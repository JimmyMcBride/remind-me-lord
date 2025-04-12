package dev.jimmymcbride.remindmelord.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jimmymcbride.remindmelord.domain.repository.VerseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the home screen, managing verse search, tag selection,
 * and paginated verse retrieval using the Paging 3 library.
 *
 * Responsibilities:
 * - Tracks and updates the user's search query.
 * - Maintains a list of all available tags and currently selected tags.
 * - Exposes a [Flow] of [PagingData] containing verses that match the query and selected tags.
 * - Provides functions for toggling tags and updating the search input.
 *
 * This ViewModel is lifecycle-aware and scoped to the Composable navigation graph using Hilt.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val verseRepository: VerseRepository,
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")

    /**
     * The current search query string entered by the user.
     * Used to filter verses by their text or reference fields.
     */
    val searchQuery: State<String> = _searchQuery

    private val _allTags = mutableStateOf<List<String>>(emptyList())

    /**
     * A list of all unique tags found in the verse database.
     * Loaded once and used to display filter chips in the UI.
     */
    val allTags: State<List<String>> = _allTags

    private val _selectedTags = mutableStateOf<List<String>>(emptyList())

    /**
     * The list of tags currently selected by the user, in the order they were selected.
     * Used for both visual display and filtering of paged verse data.
     */
    val selectedTags: State<List<String>> = _selectedTags

    /**
     * Fetches all distinct tags from the database via the [VerseRepository].
     *
     * This is typically called once during ViewModel initialization or screen composition
     * to populate the available tag filter UI.
     */
    fun getAllTags() {
        viewModelScope.launch {
            _allTags.value = verseRepository.getAllTags()
        }
    }

    /**
     * A reactive [Flow] of [PagingData] that emits paged verses filtered
     * by the current search query and selected tags.
     *
     * This flow updates automatically when either the query or tag list changes,
     * and is cached within the [viewModelScope] to prevent reloading on recomposition.
     */
    val versePagingFlow = snapshotFlow { _searchQuery.value to _selectedTags.value }
        .distinctUntilChanged()
        .flatMapLatest { (query, tags) ->
            verseRepository.getFilteredPaged(query.takeIf { it.isNotBlank() }, tags.toList())
        }
        .cachedIn(viewModelScope)

    /**
     * Updates the current search query. Triggers a new paging flow when changed.
     *
     * @param newQuery The new query string entered by the user.
     */
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.updateState { newQuery }
    }

    /**
     * Toggles the selection state of a tag.
     *
     * - If the tag is already selected, it is removed.
     * - If it is not selected, it is added to the end of the list.
     *
     * @param tag The tag string to toggle.
     */
    fun onToggleTag(tag: String) {
        _selectedTags.updateState {
            if (contains(tag)) this - tag else this + tag
        }
    }

    private fun <T> MutableState<T>.updateState(transform: T.() -> T) {
        this.value = this.value.transform()
    }
}
