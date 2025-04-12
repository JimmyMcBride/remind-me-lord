package dev.jimmymcbride.remindmelord.domain.models

/**
 * Domain model representing a Bible verse used throughout the app's business and UI layers.
 *
 * This is the app-facing representation of a verse, typically mapped from [VerseEntity].
 *
 * @property id The unique identifier for the verse.
 * @property text The content of the verse.
 * @property reference The book, chapter, and verse reference (e.g., "Romans 8:28").
 * @property tags A list of tags used to categorize or filter the verse.
 */
data class Verse(
    val id: Int,
    val text: String,
    val reference: String,
    val tags: List<String>
)
