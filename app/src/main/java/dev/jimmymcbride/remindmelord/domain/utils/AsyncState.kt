package dev.jimmymcbride.remindmelord.domain.utils

sealed class AsyncState<out T> {
    data object Idle : AsyncState<Nothing>()
    data object Loading : AsyncState<Nothing>()
    data class Success<T>(val data: T) : AsyncState<T>()
    data class Error(val message: String) : AsyncState<Nothing>()
}
