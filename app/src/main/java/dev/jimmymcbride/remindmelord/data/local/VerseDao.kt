package dev.jimmymcbride.remindmelord.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
     * Retrieves all verses from the database.
     *
     * @return A list of all [VerseEntity] entries.
     */
    @Query("SELECT * FROM verses")
    suspend fun getAll(): List<VerseEntity>

    /**
     * Retrieves a single random verse from the database.
     *
     * @return A random [VerseEntity], or null if the table is empty.
     */
    @Query("SELECT * FROM verses ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomVerse(): VerseEntity?

    /**
     * Retrieves a single random verse that contains the given tag.
     *
     * @param tag The tag to filter verses by.
     * @return A random [VerseEntity] matching the tag, or null if no match is found.
     */
    @Query("""
        SELECT * FROM verses 
        WHERE tags LIKE '%' || :tag || '%' 
        ORDER BY RANDOM() 
        LIMIT 1
    """)
    suspend fun getRandomVerseByTag(tag: String): VerseEntity?

    /**
     * Retrieves all verses that contain the given tag.
     *
     * @param tag The tag to filter verses by.
     * @return A list of [VerseEntity] entries that match the tag.
     */
    @Query("""
        SELECT * FROM verses 
        WHERE tags LIKE '%' || :tag || '%'
    """)
    suspend fun getVersesByTag(tag: String): List<VerseEntity>
}
