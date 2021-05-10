package com.example.upraxisexam.data.repository.personlist

import com.example.upraxisexam.data.database.PersonEntity
import com.example.upraxisexam.data.util.*
import com.example.upraxisexam.domain.model.PersonItem
import com.example.upraxisexam.domain.repository.PersonsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PersonsRepositoryImpl(
    private val personsLocalDataSource: PersonsLocalDataSource,
    private val personsRemoteDataSource: PersonsRemoteDataSource
) : PersonsRepository, SafeAPICall {

    override suspend fun getPersons(): Resource<List<PersonItem>> {
        val personEntities = personsLocalDataSource.fetchPersons()
        if (personEntities.count() > 0) {
            val persons = mutableListOf<PersonItem>()
            for (personEntity in personEntities) {
                persons.add(
                        PersonItem(
                                personEntity.firstName,
                                personEntity.lastName,
                                personEntity.birthday,
                                personEntity.age,
                                personEntity.emailAddress,
                                personEntity.mobileNo,
                                personEntity.address,
                                personEntity.contactPerson,
                                personEntity.contactPersonPhoneNo,
                        )
                )
            }
            return Resource.Success(persons)
        } else {
            return refreshPersons()
        }
    }

    override suspend fun refreshPersons(): Resource<List<PersonItem>> {
        try {
            val personsResponse = callAPI {
                personsRemoteDataSource.fetchPersons()
            }
            personsLocalDataSource.deleteAllPersons()
            val persons = mutableListOf<PersonItem>()
            for (personResponse in personsResponse) {
                val personItem = PersonItem(
                        personResponse.firstName,
                        personResponse.lastName,
                        personResponse.birthday,
                        getAge(personResponse.birthday),
                        personResponse.emailAddress,
                        personResponse.mobileNo,
                        personResponse.address,
                        personResponse.contactPerson,
                        personResponse.contactPersonPhoneNo,
                )
                persons.add(personItem)
                personsLocalDataSource.addPerson(
                        PersonEntity(
                                personItem.firstName,
                                personItem.lastName,
                                personItem.birthday,
                                personItem.age,
                                personItem.emailAddress,
                                personItem.mobileNo,
                                personItem.address,
                                personItem.contactPerson,
                                personItem.contactPersonPhoneNo
                        )
                )
            }
            return Resource.Success(persons)
        } catch (e: NullAPIResponseBodyException) {
            return Resource.Error(e.message.toString())
        } catch (e: UnsuccessfulAPIResponseException) {
            return Resource.Error(e.message.toString())
        } catch (e: NoActiveNetworkException) {
            return Resource.Error(e.message.toString())
        } catch (t: Throwable) {
            return Resource.Error(t.message.toString())
        }
    }

    private fun getAge(birthday: String?): Int? {
        birthday?.let { bDay ->
            val simpleDateFormat = SimpleDateFormat("mm-dd-yyyy", Locale.US)
            simpleDateFormat.parse(bDay)?.let { bDate ->
                return calculateAge(bDate)
            }
        }
        return null
    }

    private fun calculateAge(birthDate: Date): Int {
        val dob = Calendar.getInstance()
        dob.time = birthDate
        val today = Calendar.getInstance()
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }
}