package com.example.upraxisexam.data.repository.personlist

import com.example.upraxisexam.data.database.PersonEntity

interface PersonsLocalDataSource {

    suspend fun addPerson(personEntity: PersonEntity)
    suspend fun deleteAllPersons()
    suspend fun fetchPersons(): List<PersonEntity>
}
