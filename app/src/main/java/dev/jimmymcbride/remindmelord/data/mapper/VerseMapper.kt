package dev.jimmymcbride.remindmelord.data.mapper

import dev.jimmymcbride.remindmelord.data.entites.VerseEntity
import dev.jimmymcbride.remindmelord.domain.models.Verse

fun VerseEntity.toDomain() = Verse(id, text, reference, tags)
fun Verse.toEntity() = VerseEntity(id, text, reference, tags)
