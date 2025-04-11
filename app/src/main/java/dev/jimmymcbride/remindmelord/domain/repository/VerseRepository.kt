package dev.jimmymcbride.remindmelord.domain.repository

import dev.jimmymcbride.remindmelord.domain.models.Verse

interface VerseRepository {
    suspend fun getAll(): List<Verse>
    suspend fun getRandomVerse(): Verse?
    suspend fun getRandomVerseByTag(tag: String): Verse?
    suspend fun getVersesByTag(tag: String): List<Verse>
}
