package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import java.util.UUID

@Dao
interface BreastFeedingDao {

    @Insert
    suspend fun insertFeeding(feeding: FeedingEntity)

    @Insert
    suspend fun insertDetail(detail: BreastFeedingDetailEntity)

    @Transaction
    suspend fun insert(breastFeeding: BreastFeeding) {
        insertFeeding(breastFeeding.feeding)
        insertDetail(breastFeeding.detail)
    }

    @Transaction
    @Query("SELECT * from feeding WHERE mealId = :mealId AND type = 'BREAST'")
    suspend fun getFeedingsForMeal(mealId: UUID): List<BreastFeeding>
}