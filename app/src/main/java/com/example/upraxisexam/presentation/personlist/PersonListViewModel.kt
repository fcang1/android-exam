package com.example.upraxisexam.presentation.personlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upraxisexam.data.util.Resource
import com.example.upraxisexam.domain.model.PersonItem
import com.example.upraxisexam.domain.usecase.personlist.GetPersonsUseCase
import com.example.upraxisexam.domain.usecase.personlist.RefreshPersonsUseCase
import kotlinx.coroutines.launch

class PersonListViewModel(
    private val getPersonsUseCase: GetPersonsUseCase,
    private val refreshPersonsUseCase: RefreshPersonsUseCase
) : ViewModel() {

    private val resourceMutableLiveData = MutableLiveData<Resource<List<PersonItem>>>()
    val resourceLiveData: LiveData<Resource<List<PersonItem>>>
        get() = resourceMutableLiveData

    private var tempPersons = listOf<PersonItem>()

    fun getPersons() {
        viewModelScope.launch {
            resourceMutableLiveData.value = Resource.Loading(tempPersons)
            val resourcePersons = getPersonsUseCase()
            resourcePersons.data?.let {
                tempPersons = it
            }
            resourceMutableLiveData.value = resourcePersons
        }
    }

    fun onShowErrorMessageComplete() {
        resourceMutableLiveData.value = Resource.Success(tempPersons)
    }
}