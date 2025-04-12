package dev.jimmymcbride.remindmelord.data.local.seeder

import android.content.Context
import dev.jimmymcbride.remindmelord.data.entites.VerseEntity
import dev.jimmymcbride.remindmelord.data.local.VerseDao
import dev.jimmymcbride.remindmelord.domain.models.VerseSeed
import kotlinx.serialization.json.Json

/**
 * Utility class responsible for seeding the local database with verses from a bundled JSON asset.
 *
 * This is typically used to populate the app's Room database the first time it runs,
 * using a predefined JSON file (`verses.json`) stored in the assets folder.
 *
 * @param context Used to access the assets directory.
 * @param dao The [VerseDao] used to insert verse data into the database.
 */
class SeedDatabase(private val context: Context, private val dao: VerseDao) {
    /**
     * Reads verse data from the `verses.json` file in the app's assets directory,
     * deserializes it into a list of [VerseSeed] objects, converts them to [VerseEntity]s,
     * and inserts them into the Room database via [VerseDao].
     *
     * This function is `suspend` because it performs file I/O and database operations.
     */
    suspend fun seedFromJson() {
        val input = context.assets.open("verses.json").bufferedReader().use { it.readText() }
        val verses: List<VerseSeed> = Json.decodeFromString(input)
        dao.insertAll(verses.map {
            VerseEntity(
                text = it.text,
                reference = it.reference,
                tags = it.tags
            )
        })
    }
}
