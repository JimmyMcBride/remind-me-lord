package dev.jimmymcbride.remindmelord.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.jimmymcbride.remindmelord.data.entites.VerseEntity

@Database(entities = [VerseEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class VerseDatabase : RoomDatabase() {
    abstract fun verseDao(): VerseDao
}
