package dev.jimmymcbride.remindmelord.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jimmymcbride.remindmelord.domain.repository.SeederRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeederViewModel @Inject constructor(
    private val seederRepository: SeederRepository
) : ViewModel() {
    fun maybeSeedOnLaunch() {
        viewModelScope.launch {
            seederRepository.seedIfNecessary()
        }
    }
}
