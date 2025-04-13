package dev.jimmymcbride.remindmelord.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import dev.jimmymcbride.remindmelord.data.entites.VerseEntity

@Dao
interface VerseDao {
    /**
     * Inserts a list of verses into the database.
     * If a verse already exists, it will be replaced.
     *
     * @param verses The list of [VerseEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(verses: List<VerseEntity>)

    /**
     * Executes a raw SQL query for paginated retrieval of [VerseEntity]s.
     *
     * Used in conjunction with [SimpleSQLiteQuery] to dynamically construct a query
     * that applies optional search filtering (text/reference) and optional tag-based filtering.
     *
     * The result is emitted as a [PagingSource] for use with Paging 3.
     *
     * @param query A [SupportSQLiteQuery] containing the final SQL statement and arguments.
     * @return A [PagingSource] of verses that match the criteria.
     */
    @RawQuery(observedEntities = [VerseEntity::class])
    fun getFilteredPagedRaw(query: SupportSQLiteQuery): PagingSource<Int, VerseEntity>

    /**
     * Retrieves a single random verse from the database.
     *
     * @return A random [VerseEntity], or null if the table is empty.
     */
    @Query("SELECT * FROM verses ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomVerse(): VerseEntity?

    /**
     * Retrieves the raw `tags` field from all verses in the database.
     *
     * Each entry in the returned list is a comma-separated string of tags from a single verse.
     * It is up to the caller to split, normalize, and deduplicate the tags.
     *
     * @return A list of comma-separated tag strings from all verses.
     */
    @Query("SELECT tags FROM verses")
    suspend fun getAllTags(): List<String>

    /**
     * Retrieves a single random verse that contains the given tag.
     *
     * @param tag The tag to filter verses by.
     * @return A random [VerseEntity] matching the tag, or null if no match is found.
     */
    @Query(
        """
            SELECT * FROM verses
            WHERE tags LIKE '%' || :tag || '%'
            ORDER BY RANDOM()
            LIMIT 1
        """
    )
    suspend fun getRandomVerseByTag(tag: String): VerseEntity?

    /**
     * Returns the total number of verses currently stored in the database.
     *
     * This can be used to determine if the database has already been seeded
     * or if it is a fresh install with no existing verse data.
     *
     * @return The number of [VerseEntity] records in the `verses` table.
     */
    @Query("SELECT COUNT(*) FROM verses")
    suspend fun getVerseCount(): Int

    /**
     * Inserts a single [VerseEntity] into the database.
     *
     * If a verse with the same primary key already exists, it will be replaced.
     * This method is typically used when adding a new verse during seeding or user input.
     *
     * @param verse The [VerseEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verse: VerseEntity)

    /**
     * Finds a verse in the database that exactly matches the given `text` and `reference`.
     *
     * This is used during seeding to check if a verse already exists,
     * so that duplicates are not inserted across app updates.
     *
     * @param text The verse content to match.
     * @param reference The verse reference to match (e.g., "John 3:16").
     * @return The matching [VerseEntity], or null if no match is found.
     */
    @Query("SELECT * FROM verses WHERE text = :text AND reference = :reference LIMIT 1")
    suspend fun findVerse(text: String, reference: String): VerseEntity?
}
