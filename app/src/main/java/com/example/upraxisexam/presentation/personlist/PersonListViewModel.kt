package com.example.upraxisexam.presentation.personlist

import androidx.lifecycle.*
import com.example.upraxisexam.data.database.PersonEntity
import com.example.upraxisexam.data.util.Resource
import com.example.upraxisexam.domain.usecase.personlist.GetPersonsUseCase
import com.example.upraxisexam.domain.usecase.personlist.RefreshPersonsUseCase
import kotlinx.coroutines.launch

class PersonListViewModel(
    getPersonsUseCase: GetPersonsUseCase,
    private val refreshPersonsUseCase: RefreshPersonsUseCase
) : ViewModel() {

    val personEntitiesLiveData: LiveData<List<PersonEntity>> = getPersonsUseCase().asLiveData()

    private val resourceMutableLiveData = MutableLiveData<Resource<Any>>()
    val resourceLiveData: LiveData<Resource<Any>>
        get() = resourceMutableLiveData

    private var initialGetPersonFinished = false

    fun attemptToGetPersons() {
        if (!initialGetPersonFinished) {
            refreshPersons()
        }
        attemptedToGetPersons()
    }

    fun attemptedToGetPersons() {
        initialGetPersonFinished = true
    }

    fun onShowErrorMessageComplete() {
        resourceMutableLiveData.value = Resource.Success(Any())
    }

    fun refreshPersons() {
        viewModelScope.launch {
            resourceMutableLiveData.value = Resource.Loading()
            val resource = refreshPersonsUseCase()
            resourceMutableLiveData.value = resource
        }
    }
}