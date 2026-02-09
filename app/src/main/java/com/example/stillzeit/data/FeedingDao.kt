package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedingDao {

    @Insert
    suspend fun insert(feeding: FeedingEntity)

    @Query("SELECT * FROM feeding")
    fun getAllFeedings(): Flow<List<FeedingEntity>>
}
