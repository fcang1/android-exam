package com.example.upraxisexam.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PersonDatabaseDao {

    @Insert
    suspend fun insert(personEntity: PersonEntity)

    @Query("DELETE FROM persons")
    suspend fun clear()

    @Query("SELECT * FROM persons ORDER BY id ASC")
    suspend fun fetchAllPersons(): List<PersonEntity>
}