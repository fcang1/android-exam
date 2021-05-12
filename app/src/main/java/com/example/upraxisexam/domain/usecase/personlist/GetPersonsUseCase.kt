package com.example.upraxisexam.domain.usecase.personlist

import com.example.upraxisexam.domain.repository.PersonsRepository
import javax.inject.Inject

class GetPersonsUseCase
@Inject
constructor(private val personsRepository: PersonsRepository) {

    operator fun invoke() = personsRepository.personsFlow
}