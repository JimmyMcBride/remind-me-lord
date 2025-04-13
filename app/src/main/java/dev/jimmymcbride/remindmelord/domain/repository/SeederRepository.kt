package dev.jimmymcbride.remindmelord.domain.repository

/**
 * Repository responsible for managing the initial and update-based seeding
 * of the verse database using bundled JSON assets.
 *
 * This includes logic to:
 * - Detect first-time installs or app updates
 * - Seed the Room database with verses from `verses.json`
 * - Store and compare the app version code using DataStore
 */
interface SeederRepository {

    /**
     * Seeds the verse database only if the app has just been installed
     * or has been updated since the last launch.
     */
    suspend fun seedIfNecessary()
}
