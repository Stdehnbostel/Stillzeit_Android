package com.stillzeit.android.model.enums

enum class BreastFeedingSide(val value: String) {
    LEFT("left"),
    RIGHT("right"),
    NONE("");

    companion object {
        fun fromString(value: String): BreastFeedingSide =
            entries.find { it.value == value } ?: NONE
    }
}
