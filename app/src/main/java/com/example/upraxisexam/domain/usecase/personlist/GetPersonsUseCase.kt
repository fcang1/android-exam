package com.example.upraxisexam.domain.usecase.personlist

import com.example.upraxisexam.domain.repository.PersonsRepository

class GetPersonsUseCase(private val personsRepository: PersonsRepository) {

    suspend operator fun invoke() = personsRepository.getPersons()
}