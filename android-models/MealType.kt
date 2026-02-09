package com.stillzeit.android.model.enums

enum class MealType(val value: String) {
    BREAST("breast"),
    BOTTLE("bottle"),
    OTHER("other"),
    PUMPING("pumping");

    companion object {
        fun fromString(value: String): MealType =
            entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown MealType: $value")
    }
}
