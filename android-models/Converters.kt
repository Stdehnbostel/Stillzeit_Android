package com.stillzeit.android.data.database

import androidx.room.TypeConverter
import com.stillzeit.android.model.enums.BottleType
import com.stillzeit.android.model.enums.BreastFeedingSide
import com.stillzeit.android.model.enums.MealType

class Converters {

    // --- MealType ---

    @TypeConverter
    fun fromMealType(type: MealType): String = type.value

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.fromString(value)

    // --- BreastFeedingSide ---

    @TypeConverter
    fun fromBreastFeedingSide(side: BreastFeedingSide): String = side.value

    @TypeConverter
    fun toBreastFeedingSide(value: String): BreastFeedingSide =
        BreastFeedingSide.fromString(value)

    // --- BottleType ---

    @TypeConverter
    fun fromBottleType(type: BottleType): String = type.value

    @TypeConverter
    fun toBottleType(value: String): BottleType = BottleType.fromString(value)
}
