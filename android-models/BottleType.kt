package com.stillzeit.android.model.enums

enum class BottleType(val value: String) {
    MILK("milk"),
    FORMULA("formula");

    companion object {
        fun fromString(value: String): BottleType =
            entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown BottleType: $value")
    }
}
