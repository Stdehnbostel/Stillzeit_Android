package com.example.stillzeit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
            "left" -> Side.LEFT
            "right" -> Side.RIGHT
            // sollte eventuell Thrown, falls string != ""
            else -> Side.EMPTY
        }
    }

    @TypeConverter
    fun mealTypeToString(type: MealType): String {
        return type.toString()
    }
}

@Database(entities = [FeedingEntity::class, BreastFeedingDetailEntity::class, MealEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breastFeedingDao(): FeedingDao
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stillzeit_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}