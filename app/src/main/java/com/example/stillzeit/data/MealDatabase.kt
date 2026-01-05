package com.example.stillzeit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Converters {
    @TypeConverter
    fun sideToString(side: Side): String {
        return side.side
    }
    // sollte eventuell besser Throwen
    @TypeConverter
    fun stringToSide(string: String): Side {
        return when (string) {
            "left" -> Side.Left()
            "right" -> Side.Right()
            // sollte eventuell Thrown, falls string != ""
            else -> Side.Empty()
        }
    }
}

@Database(entities = [Feeding::class, BreastFeedingDetail::class, MealInDb::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breastFeedingDao(): FeedingDao
}