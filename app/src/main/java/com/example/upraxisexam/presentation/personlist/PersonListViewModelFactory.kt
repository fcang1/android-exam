package com.example.upraxisexam.presentation.personlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.upraxisexam.domain.usecase.personlist.GetPersonsUseCase
import com.example.upraxisexam.domain.usecase.personlist.RefreshPersonsUseCase

class PersonListViewModelFactory(
    private val getPersonsUseCase: GetPersonsUseCase,
    private val refreshPersonsUseCase: RefreshPersonsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonListViewModel::class.java)) {
            return PersonListViewModel(
                getPersonsUseCase,
                refreshPersonsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}