package com.example.upraxisexam.domain.repository

import com.example.upraxisexam.data.database.PersonEntity
import com.example.upraxisexam.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface PersonsRepository {

    val personsFlow: Flow<List<PersonEntity>>
    suspend fun refreshPersons(): Resource<Any>
}
