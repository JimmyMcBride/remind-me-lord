package dev.jimmymcbride.remindmelord.data.local.seeder

import android.content.Context
import dev.jimmymcbride.remindmelord.data.entites.VerseEntity
import dev.jimmymcbride.remindmelord.data.local.VerseDao
import dev.jimmymcbride.remindmelord.domain.models.VerseSeed
import kotlinx.serialization.json.Json

class SeedDatabase(private val context: Context, private val dao: VerseDao) {
    suspend fun seedFromJson() {
        val input = context.assets.open("verses.json").bufferedReader().use { it.readText() }
        val verses: List<VerseSeed> = Json.decodeFromString(input)
        dao.insertAll(verses.map { VerseEntity(text = it.text, reference = it.reference, tags = it.tags) })
    }
}
