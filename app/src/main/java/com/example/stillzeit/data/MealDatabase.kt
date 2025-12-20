package com.example.stillzeit.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BreastFeedingDetail::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breastFeedingDao(): BreastFeedingDetailDao
}