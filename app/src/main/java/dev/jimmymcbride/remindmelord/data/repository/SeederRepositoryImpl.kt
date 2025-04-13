package dev.jimmymcbride.remindmelord.data.repository

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.jimmymcbride.remindmelord.data.entites.VerseEntity
import dev.jimmymcbride.remindmelord.data.local.VerseDao
import dev.jimmymcbride.remindmelord.domain.models.VerseSeed
import dev.jimmymcbride.remindmelord.domain.repository.SeederRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SeederRepositoryImpl @Inject constructor(
    private val context: Context,
    private val verseDao: VerseDao
) : SeederRepository {
    override suspend fun seedIfNecessary() {
        val currentVersion = PackageInfoCompat.getLongVersionCode(
            context.packageManager.getPackageInfo(context.packageName, 0)
        )

        val lastVersion = getLastVersion()
        val isFirstInstall = verseDao.getVerseCount() == 0

        if (isFirstInstall || currentVersion > lastVersion) {
            seedFromJson()
            setLastVersion(currentVersion)
        }
    }

    private suspend fun getLastVersion(): Long {
        val dataStore = context.dataStore
        return dataStore.data.first()[PreferencesKeys.LAST_VERSION] ?: -1
    }

    private suspend fun setLastVersion(version: Long) {
        val dataStore = context.dataStore
        dataStore.edit { prefs -> prefs[PreferencesKeys.LAST_VERSION] = version }
    }

    private suspend fun seedFromJson() {
        val input = context.assets.open("verses.json").bufferedReader().use { it.readText() }
        val verses: List<VerseSeed> = Json.decodeFromString(input)

        for (verse in verses) {
            val exists = verseDao.findVerse(verse.text, verse.reference)
            if (exists == null) {
                verseDao.insert(
                    VerseEntity(
                        text = verse.text,
                        reference = verse.reference,
                        tags = verse.tags
                    )
                )
            }
        }
    }

    private object PreferencesKeys {
        val LAST_VERSION = longPreferencesKey("last_version_code")
    }

    private val Context.dataStore by preferencesDataStore(name = "app_settings")
}
