package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import java.util.UUID

@Dao
interface BottleFeedingDao {
    @Insert
    suspend fun insertFeeding(feeding: FeedingEntity)

    @Insert
    suspend fun insertDetail(detail: BottleFeedingDetailEntity)

    @Transaction
    suspend fun insert(bottleFeeding: BottleFeeding) {
        insertFeeding(bottleFeeding.feeding)
        insertDetail(bottleFeeding.detail)
    }

    @Transaction
    @Query("SELECT * from feeding WHERE mealId = :mealId")
    suspend fun getFeedingsForMeal(mealId: UUID): List<BottleFeeding>
}