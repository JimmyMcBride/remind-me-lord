package dev.jimmymcbride.remindmelord.domain.utils

/**
 * A sealed class representing the state of an asynchronous operation.
 *
 * This can be used to model UI states such as loading, success, error, and idle
 * in a type-safe way, especially for data fetching or other suspend operations.
 *
 * @param T The type of data being handled when in a [Success] state.
 */
sealed class AsyncState<out T> {
    /**
     * Represents a state where no asynchronous operation is currently running.
     */
    data object Idle : AsyncState<Nothing>()

    /**
     * Represents a state where an asynchronous operation is currently in progress.
     */
    data object Loading : AsyncState<Nothing>()

    /**
     * Represents a successful completion of an asynchronous operation.
     *
     * @param data The result of the successful operation.
     */
    data class Success<T>(val data: T) : AsyncState<T>()

    /**
     * Represents a failed asynchronous operation.
     *
     * @param message A human-readable error message explaining the failure.
     */
    data class Error(val message: String) : AsyncState<Nothing>()
}
