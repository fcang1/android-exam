package com.example.upraxisexam.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(personEntities: List<PersonEntity>)

    @Query("DELETE FROM persons")
    suspend fun clear()

    @Query("SELECT * FROM persons ORDER BY id ASC")
    fun fetchPersonEntitiesFlow(): Flow<List<PersonEntity>>
}