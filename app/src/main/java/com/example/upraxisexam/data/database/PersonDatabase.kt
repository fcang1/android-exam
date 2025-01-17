package com.example.upraxisexam.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PersonEntity::class], version = 1, exportSchema = false)
abstract class PersonDatabase : RoomDatabase() {

    abstract val personDatabaseDao: PersonDatabaseDao
}