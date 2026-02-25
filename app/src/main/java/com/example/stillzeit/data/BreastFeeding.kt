package com.example.stillzeit.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

enum class Side(val side: String) {
    LEFT("left"),
    RIGHT("right"),
    EMPTY(""),
}

data class BreastFeeding(
    @Embedded
    val feeding: FeedingEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "feedingId"
    )
    val detail: BreastFeedingDetailEntity
) : MealComponent {
    override val description: String
        get() = TODO("Not yet implemented")

    override val id: String get() = feeding.id

    override val intervallTime: Pair<String, String?>
        get() = Pair(detail.start, detail.end)

    override val mealId: UUID
        get() = feeding.mealId

    override val start: String
        get() = detail.start

    override val title: String
        get() = TODO("Not yet implemented")

    override val volume: Int
        get() = TODO("Not yet implemented")

    override fun compareTo(other: MealComponent): Int {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        return if (other is BreastFeeding) {
            feeding.id == other.feeding.id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}