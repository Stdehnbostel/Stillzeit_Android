package com.example.stillzeit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.UUID

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
        return type.name
    }

    @TypeConverter
    fun stringToMealType(string: String): MealType {
        return MealType.valueOf(string)
    }

    @TypeConverter
    fun uuidToString(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun stringToUuid(string: String?): UUID? {
        return string?.let { UUID.fromString(it) }
    }

    @TypeConverter
    fun bottleTypeToString(type: BottleType): String {
        return type.type
    }

    @TypeConverter
    fun stringToBottleType(string: String): BottleType {
        return when (string) {
            "milk" -> BottleType.MILK
            "formula" -> BottleType.FORMULA
            else -> BottleType.MILK
        }
    }
}

@Database(
    entities = [FeedingEntity::class,
        BreastFeedingDetailEntity::class,
        BottleFeedingDetailEntity::class,
        OtherFeedingDetailEntity::class,
        PumpingDetailEntity::class,
        MealEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedingDao(): FeedingDao
    abstract fun mealDao(): MealDao
    abstract fun breastFeedingDao(): BreastFeedingDao
    abstract fun bottleFeedingDao(): BottleFeedingDao
    abstract fun otherFeedingDao(): OtherFeedingDao
    abstract fun pumpingDao(): PumpingDao

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