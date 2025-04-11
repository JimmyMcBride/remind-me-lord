package dev.jimmymcbride.remindmelord.domain.models

data class Verse(
    val id: Int,
    val text: String,
    val reference: String,
    val tags: List<String>
)
