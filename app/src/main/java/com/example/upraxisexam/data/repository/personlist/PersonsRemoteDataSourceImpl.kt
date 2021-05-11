package com.example.upraxisexam.data.repository.personlist

import com.example.upraxisexam.data.api.model.response.PersonResponse
import com.example.upraxisexam.data.api.service.PersonsService
import retrofit2.Response

class PersonsRemoteDataSourceImpl(private val personsService: PersonsService) :
    PersonsRemoteDataSource {

    override suspend fun fetchPersons(): Response<List<PersonResponse>> {
        return personsService.fetchPersons()
    }
}