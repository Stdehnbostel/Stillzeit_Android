package com.example.stillzeit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Converters {
    @OptIn(ExperimentalUuidApi::class)
    @TypeConverter
    fun uuidToString(uuid: Uuid): String {
        return uuid.toString()
    }

    @OptIn(ExperimentalUuidApi::class)
    @TypeConverter
    fun stringToUuid(string: String): Uuid {
        return Uuid.parse(string)
    }
}

@Database(entities = [Feeding::class, BreastFeedingDetail::class, MealInDb::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breastFeedingDao(): BreastFeedingDetailDao
}