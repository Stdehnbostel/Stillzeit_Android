package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import java.util.UUID

@Dao
interface PumpingDao {
    @Insert
    suspend fun insertFeeding(feeding: FeedingEntity)

    @Insert
    suspend fun insertDetail(detail: PumpingDetailEntity)

    @Transaction
    suspend fun insert(pumping: Pumping) {
        insertFeeding(pumping.feeding)
        insertDetail(pumping.detail)
    }

    @Transaction
    @Query("SELECT * from feeding WHERE mealId = :mealId AND type = 'PUMPING'")
    suspend fun getFeedingsForMeal(mealId: UUID): List<Pumping>
}