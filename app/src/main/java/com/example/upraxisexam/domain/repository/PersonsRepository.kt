package com.example.upraxisexam.domain.repository

import com.example.upraxisexam.data.util.Resource
import com.example.upraxisexam.domain.model.PersonItem

interface PersonsRepository {

    suspend fun getPersons(): Resource<List<PersonItem>>
    suspend fun refreshPersons(): Resource<List<PersonItem>>
}
