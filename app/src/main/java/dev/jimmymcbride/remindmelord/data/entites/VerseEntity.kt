package dev.jimmymcbride.remindmelord.data.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses")
data class VerseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val reference: String,
    val tags: List<String>
)
