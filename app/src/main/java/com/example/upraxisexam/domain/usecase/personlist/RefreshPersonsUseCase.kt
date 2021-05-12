package com.example.upraxisexam.domain.usecase.personlist

import com.example.upraxisexam.domain.repository.PersonsRepository
import javax.inject.Inject

class RefreshPersonsUseCase
@Inject
constructor(private val personsRepository: PersonsRepository) {

    suspend operator fun invoke() = personsRepository.refreshPersons()
}