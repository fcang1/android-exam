package com.example.upraxisexam.data.repository.personlist

import com.example.upraxisexam.data.database.PersonEntity
import kotlinx.coroutines.flow.Flow

interface PersonsLocalDataSource {

    suspend fun addPersons(personEntities: List<PersonEntity>)
    suspend fun deleteAllPersons()
    fun fetchPersonsFlow(): Flow<List<PersonEntity>>
}
