package com.example.upraxisexam.domain.usecase.personlist

import com.example.upraxisexam.domain.repository.PersonsRepository

class GetPersonsUseCase(private val personsRepository: PersonsRepository) {

    operator fun invoke() = personsRepository.personsFlow
}