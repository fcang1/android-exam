package com.example.upraxisexam.data.repository.personlist

import com.example.upraxisexam.data.api.model.response.PersonResponse
import retrofit2.Response

interface PersonsRemoteDataSource {

    suspend fun fetchPersons(): Response<List<PersonResponse>>
}
