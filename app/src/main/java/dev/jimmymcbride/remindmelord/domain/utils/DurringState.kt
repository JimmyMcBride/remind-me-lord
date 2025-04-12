package dev.jimmymcbride.remindmelord.domain.utils

import androidx.compose.runtime.Composable

/**
 * Executes a set of callbacks based on the current state of the [AsyncState].
 *
 * Useful for non-Composable logic like logging, data transformation, or triggering side effects.
 *
 * @param success Invoked if the state is [AsyncState.Success], with the result data.
 * @param error Invoked if the state is [AsyncState.Error], with the error message.
 * @param idle Invoked if the state is [AsyncState.Idle].
 * @param loading Invoked if the state is [AsyncState.Loading].
 * @return Returns the same [AsyncState] instance after executing the corresponding callback.
 */
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

/**
 * Executes a set of Composable blocks based on the current state of the [AsyncState].
 *
 * Useful for rendering different UI elements depending on the state of an asynchronous operation.
 *
 * @param success Composable invoked when the state is [AsyncState.Success], passing the result data.
 * @param error Composable invoked when the state is [AsyncState.Error], passing the error message.
 * @param idle Composable invoked when the state is [AsyncState.Idle].
 * @param loading Composable invoked when the state is [AsyncState.Loading].
 */
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
