package dev.jimmymcbride.remindmelord.data.repository

import dev.jimmymcbride.remindmelord.data.local.VerseDao
import dev.jimmymcbride.remindmelord.data.mapper.toDomain
import dev.jimmymcbride.remindmelord.data.entites.VerseEntity
import dev.jimmymcbride.remindmelord.domain.models.Verse
import dev.jimmymcbride.remindmelord.domain.repository.VerseRepository

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
    /**
     * Retrieves all verses from the local database and maps them to domain models.
     *
     * @return A list of all verses as [Verse].
     */
    override suspend fun getAll(): List<Verse> =
        dao.getAll().map { it.toDomain() }

    /**
     * Retrieves a single random verse from the local database.
     *
     * @return A random [Verse], or null if the database is empty.
     */
    override suspend fun getRandomVerse(): Verse? =
        dao.getRandomVerse()?.toDomain()

    /**
     * Retrieves a random verse that matches the given tag.
     *
     * @param tag The tag used to filter verses.
     * @return A random [Verse] that contains the tag, or null if no match is found.
     */
    override suspend fun getRandomVerseByTag(tag: String): Verse? =
        dao.getRandomVerseByTag(tag)?.toDomain()

    /**
     * Retrieves all verses that match the given tag.
     *
     * @param tag The tag used to filter verses.
     * @return A list of [Verse] that match the tag.
     */
    override suspend fun getVersesByTag(tag: String): List<Verse> =
        dao.getVersesByTag(tag).map { it.toDomain() }
}
