package dev.jimmymcbride.remindmelord.domain.utils

import androidx.compose.runtime.Composable

fun <T> AsyncState<T>.duringState(
    success: (T) -> Unit = {},
    error: (String) -> Unit = {},
    idle: () -> Unit = {},
    loading: () -> Unit = {},
): AsyncState<T> {
    when (this) {
        is AsyncState.Success -> success(this.data)
        is AsyncState.Error -> error(this.message)
        is AsyncState.Idle -> idle()
        is AsyncState.Loading -> loading()
    }
    return this
}

@Composable
fun <T> AsyncState<T>.DuringComposableState(
    success: @Composable (T) -> Unit = {},
    error: @Composable (String) -> Unit = {},
    idle: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = {},
) {
    when (this) {
        is AsyncState.Success -> success(this.data)
        is AsyncState.Error -> error(this.message)
        is AsyncState.Idle -> idle()
        is AsyncState.Loading -> loading()
    }
}
