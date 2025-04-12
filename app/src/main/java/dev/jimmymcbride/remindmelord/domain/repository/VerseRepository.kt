package dev.jimmymcbride.remindmelord.domain.repository

import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import dev.jimmymcbride.remindmelord.domain.models.Verse
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing and querying Bible verses from a local data source.
 *
 * This abstraction allows higher-level components (like ViewModels) to interact with
 * verse data without needing to know the implementation details of the database or paging.
 */
interface VerseRepository {
    /**
     * Returns a [Flow] of paginated [Verse]s that match the provided search query and tag filters.
     *
     * This function uses Room's Paging 3 integration with a raw SQL query via [SimpleSQLiteQuery],
     * allowing dynamic filtering based on user input.
     *
     * - If [query] is null or blank, all verses are included without search filtering.
     * - If [tags] is empty, all verses are included without tag filtering.
     * - Otherwise, only verses that match the search and contain at least one matching tag are returned.
     *
     * @param query The optional search query to match against verse `text` or `reference`.
     * @param tags A list of tag strings to match against the verse's comma-separated tag values.
     * @return A [Flow] that emits [PagingData] of [Verse]s matching the filters.
     */
    fun getFilteredPaged(query: String?, tags: List<String>): Flow<PagingData<Verse>>

    /**
     * Retrieves all unique tags from the verse database.
     *
     * This method pulls all `tags` fields from the database (which are stored as
     * comma-separated strings), splits them into individual tags, trims whitespace,
     * and returns a distinct, alphabetically sorted list.
     *
     * Useful for building the full list of filterable tags shown in the UI.
     *
     * @return A sorted list of distinct tags found across all verses.
     */
    suspend fun getAllTags(): List<String>

    /**
     * Retrieves a single random verse from the local database.
     *
     * @return A random [Verse], or null if the database is empty.
     */
    suspend fun getRandomVerse(): Verse?

    /**
     * Retrieves a random verse that matches the given tag.
     *
     * @param tag The tag used to filter verses.
     * @return A random [Verse] that contains the tag, or null if no match is found.
     */
    suspend fun getRandomVerseByTag(tag: String): Verse?
}
