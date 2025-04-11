package dev.jimmymcbride.remindmelord.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class VerseSeed(
    val text: String,
    val reference: String,
    val tags: List<String>
)
