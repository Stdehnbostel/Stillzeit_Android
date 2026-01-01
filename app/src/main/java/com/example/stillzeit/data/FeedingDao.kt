package com.example.stillzeit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi


@Dao
interface FeedingDao {

    @Insert
    suspend fun insert(feeding: Feeding)

    @Query("SELECT * FROM feeding")
    fun getAllFeedings(): Flow<List<Feeding>>
}
