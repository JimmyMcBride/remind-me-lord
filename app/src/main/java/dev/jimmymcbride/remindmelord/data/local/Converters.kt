package dev.jimmymcbride.remindmelord.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(value: List<String>) = value.joinToString(",")

    @TypeConverter
    fun toList(value: String) = value.split(",")
}
