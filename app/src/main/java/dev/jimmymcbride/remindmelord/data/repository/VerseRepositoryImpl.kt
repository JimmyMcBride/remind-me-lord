package dev.jimmymcbride.remindmelord.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import dev.jimmymcbride.remindmelord.data.entites.VerseEntity
import dev.jimmymcbride.remindmelord.data.local.VerseDao
import dev.jimmymcbride.remindmelord.data.mapper.toDomain
import dev.jimmymcbride.remindmelord.domain.models.Verse
import dev.jimmymcbride.remindmelord.domain.repository.VerseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [VerseRepository] that interacts with the local Room database
 * via [VerseDao] to fetch and manage verse data.
 *
 * Maps [VerseEntity] objects from the data layer to [Verse] domain models.
 *
 * @param dao The data access object used to interact with the local database.
 */
class VerseRepositoryImpl(
    private val dao: VerseDao
) : VerseRepository {

    override fun getFilteredPaged(query: String?, tags: List<String>): Flow<PagingData<Verse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                dao.getFilteredPagedRaw(buildVerseQuery(query, tags))
            }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override suspend fun getRandomVerse(): Verse? =
        dao.getRandomVerse()?.toDomain()

    override suspend fun getAllTags(): List<String> {
        return dao.getAllTags()
            .flatMap { it.split(",") }
            .map { it.trim().lowercase() }
            .distinct()
            .sorted()
    }

    override suspend fun getRandomVerseByTag(tag: String): Verse? =
        dao.getRandomVerseByTag(tag)?.toDomain()

    override suspend fun addVerse(verse: Verse) {
        dao.insert(
            VerseEntity(
                text = verse.text.trim(),
                reference = verse.reference.trim(),
                tags = verse.tags.map { it.trim().lowercase() }
            )
        )
    }

    /**
     * Builds a dynamic [SupportSQLiteQuery] that filters verses by search term and tags.
     *
     * The resulting SQL query:
     * - Matches verses where `text` or `reference` contains the [query] string (if not null/blank).
     * - Matches any verses where the `tags` column contains any of the provided [tags] strings.
     *   This is implemented using `LIKE` conditions, assuming tags are stored as a comma-separated string.
     * - Results are ordered by ID in ascending order.
     *
     * This query is safe and parameterized to prevent SQL injection.
     *
     * @param query The optional search string for filtering by text or reference.
     * @param tags A list of tags to filter verses by. Matches any verse containing at least one tag.
     * @return A [SupportSQLiteQuery] object to be passed to a DAO method annotated with [RawQuery].
     */
    private fun buildVerseQuery(query: String?, tags: List<String>): SupportSQLiteQuery {
        val sql = StringBuilder("SELECT * FROM verses WHERE 1=1")
        val args = mutableListOf<Any>()

        // Search filter
        query?.takeIf { it.isNotBlank() }?.let {
            sql.append(" AND (text LIKE ? OR reference LIKE ?)")
            args.add("%$it%")
            args.add("%$it%")
        }

        // Tags filter
        if (tags.isNotEmpty()) {
            sql.append(" AND (")
            sql.append(tags.joinToString(" OR ") {
                "tags LIKE ?"
            })
            tags.forEach { tag -> args.add("%$tag%") }
            sql.append(")")
        }

        sql.append(" ORDER BY id ASC")

        return SimpleSQLiteQuery(sql.toString(), args.toTypedArray())
    }
}
