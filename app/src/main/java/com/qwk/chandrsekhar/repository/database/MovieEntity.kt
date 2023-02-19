package com.qwk.chandrsekhar.repository.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "movies")
@TypeConverters(IntListConverter::class)
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val genres: List<Int>,
    val rating: Double,
    val postUrl: String
)

class IntListConverter {
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }
}