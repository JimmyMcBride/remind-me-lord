package dev.jimmymcbride.remindmelord.presentation.screens.addverse

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jimmymcbride.remindmelord.domain.models.Verse
import dev.jimmymcbride.remindmelord.domain.repository.VerseRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddVerseViewModel @Inject constructor(
    private val verseRepository: VerseRepository
) : ViewModel() {

    var text by mutableStateOf("")
        private set

    var reference by mutableStateOf("")
        private set

    var tags by mutableStateOf("")
        private set

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    sealed class UiEvent {
        object ShowSnackbar : UiEvent()
    }

    fun onTextChanged(newText: String) {
        text = newText
    }

    fun onReferenceChanged(newRef: String) {
        reference = newRef
    }

    fun onTagsChanged(newTags: String) {
        tags = newTags
    }

    fun addVerse() {
        if (text.isBlank() || reference.isBlank()) return

        viewModelScope.launch {
            verseRepository.addVerse(
                Verse(
                    text = text.trim(),
                    reference = reference.trim(),
                    tags = tags.split(",").map { it.trim().lowercase() }.filter { it.isNotEmpty() }
                )
            )
            _uiEvents.emit(UiEvent.ShowSnackbar)
        }
    }
}
