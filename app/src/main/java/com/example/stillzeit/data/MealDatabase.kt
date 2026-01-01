package com.example.stillzeit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Database(entities = [Feeding::class, BreastFeedingDetail::class, MealInDb::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breastFeedingDao(): FeedingDao
}