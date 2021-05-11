package com.example.upraxisexam.data.repository.personlist

import com.example.upraxisexam.data.database.PersonEntity
import com.example.upraxisexam.data.util.*
import com.example.upraxisexam.domain.repository.PersonsRepository
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class PersonsRepositoryImpl(
    private val personsLocalDataSource: PersonsLocalDataSource,
    private val personsRemoteDataSource: PersonsRemoteDataSource
) : PersonsRepository, SafeAPICall {

    override val personsFlow: Flow<List<PersonEntity>>
        get() = personsLocalDataSource.fetchPersonsFlow()

    override suspend fun refreshPersons(): Resource<Any> {
        try {
            val personsResponse = callAPI {
                personsRemoteDataSource.fetchPersons()
            }
            personsLocalDataSource.deleteAllPersons()
            val personEntities = mutableListOf<PersonEntity>()
            for (personResponse in personsResponse) {
                personEntities.add(PersonEntity(
                    personResponse.firstName,
                    personResponse.lastName,
                    personResponse.birthday,
                    getAge(personResponse.birthday),
                    personResponse.emailAddress,
                    personResponse.mobileNo,
                    personResponse.address,
                    personResponse.contactPerson,
                    personResponse.contactPersonPhoneNo,
                ))
            }
            personsLocalDataSource.addPersons(personEntities)
            return Resource.Success(Any())
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
            val simpleDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)
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