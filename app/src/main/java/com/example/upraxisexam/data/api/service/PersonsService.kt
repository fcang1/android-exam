package com.example.upraxisexam.data.api.service

import com.example.upraxisexam.data.api.model.response.PersonResponse
import retrofit2.Response
import retrofit2.http.GET

interface PersonsService {

    @GET("persons/")
    suspend fun fetchPersons(): Response<List<PersonResponse>>
}