package com.example.upraxisexam.presentation.personlist

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.upraxisexam.BuildConfig
import com.example.upraxisexam.R
import com.example.upraxisexam.data.api.service.PersonsService
import com.example.upraxisexam.data.repository.personlist.PersonsLocalDataSourceImpl
import com.example.upraxisexam.data.repository.personlist.PersonsRemoteDataSourceImpl
import com.example.upraxisexam.data.repository.personlist.PersonsRepositoryImpl
import com.example.upraxisexam.data.util.NetworkConnectionInterceptor
import com.example.upraxisexam.databinding.ActivityMainBinding
import com.example.upraxisexam.domain.usecase.personlist.GetPersonsUseCase
import com.example.upraxisexam.domain.usecase.personlist.RefreshPersonsUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // TODO: Use dependency injection
    lateinit var personListViewModelFactory: PersonListViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // TODO: Use dependency injection
        val networkConnectionInterceptor = NetworkConnectionInterceptor(applicationContext)
        val retrofit =
            Retrofit.Builder()
                .client(OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor).build())
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val personsService = retrofit.create(PersonsService::class.java)
        val personsLocalDataSource = PersonsLocalDataSourceImpl()
        val personsRemoteDataSource = PersonsRemoteDataSourceImpl(personsService)
        val personsRepository = PersonsRepositoryImpl(
            personsLocalDataSource,
            personsRemoteDataSource
        )
        val getPersonsUseCase = GetPersonsUseCase(personsRepository)
        val refreshPersonsUseCase = RefreshPersonsUseCase(personsRepository)
        personListViewModelFactory = PersonListViewModelFactory(
            getPersonsUseCase,
            refreshPersonsUseCase
        )

        val personListViewModel = ViewModelProvider(
            this,
            personListViewModelFactory
        ).get(PersonListViewModel::class.java)

        personListViewModel.resourceLiveData.observe(this) {
            it.data?.run {
                Log.e("MainActivity", "data $this")
            }

            it.message?.run {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage(this)
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show()

                personListViewModel.onShowErrorMessageComplete()
            }
        }
        personListViewModel.getPersons()
    }
}