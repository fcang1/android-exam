package com.example.upraxisexam.data.repository.personlist

import com.example.upraxisexam.data.database.PersonDatabaseDao
import com.example.upraxisexam.data.database.PersonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PersonsLocalDataSourceImpl(private val personDatabaseDao: PersonDatabaseDao) : PersonsLocalDataSource {

    override suspend fun addPersons(personEntities: List<PersonEntity>) {
        withContext(Dispatchers.IO) {
            personDatabaseDao.insertAll(personEntities)
        }
    }

    override suspend fun deleteAllPersons() {
        withContext(Dispatchers.IO) {
            personDatabaseDao.clear()
        }
    }

    override fun fetchPersonsFlow(): Flow<List<PersonEntity>> {
        return personDatabaseDao.fetchPersonEntitiesFlow()
    }
}