package com.example.upraxisexam.data.repository.personlist

import com.example.upraxisexam.data.database.PersonDatabaseDao
import com.example.upraxisexam.data.database.PersonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonsLocalDataSourceImpl(private val personDatabaseDao: PersonDatabaseDao) : PersonsLocalDataSource {

    override suspend fun addPerson(personEntity: PersonEntity) {
        withContext(Dispatchers.IO) {
            personDatabaseDao.insert(personEntity)
        }
    }

    override suspend fun deleteAllPersons() {
        withContext(Dispatchers.IO) {
            personDatabaseDao.clear()
        }
    }

    override suspend fun fetchPersons(): List<PersonEntity> {
        val personEntities: List<PersonEntity>
        withContext(Dispatchers.IO) {
            personEntities = personDatabaseDao.fetchAllPersons()
        }
        return personEntities
    }
}