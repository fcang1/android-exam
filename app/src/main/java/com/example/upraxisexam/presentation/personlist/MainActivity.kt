package com.example.upraxisexam.presentation.personlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.upraxisexam.BuildConfig
import com.example.upraxisexam.R
import com.example.upraxisexam.data.api.service.PersonsService
import com.example.upraxisexam.data.database.PersonDatabase
import com.example.upraxisexam.data.repository.personlist.PersonsLocalDataSourceImpl
import com.example.upraxisexam.data.repository.personlist.PersonsRemoteDataSourceImpl
import com.example.upraxisexam.data.repository.personlist.PersonsRepositoryImpl
import com.example.upraxisexam.data.util.NetworkConnectionInterceptor
import com.example.upraxisexam.data.util.Resource
import com.example.upraxisexam.databinding.ActivityMainBinding
import com.example.upraxisexam.domain.usecase.personlist.GetPersonsUseCase
import com.example.upraxisexam.domain.usecase.personlist.RefreshPersonsUseCase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // TODO: Use dependency injection
    lateinit var personListViewModel: PersonListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
                DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // TODO: Use dependency injection
        val personDatabaseDao = PersonDatabase.getInstance(applicationContext).personDatabaseDao
        val personsLocalDataSource = PersonsLocalDataSourceImpl(personDatabaseDao)
        val networkConnectionInterceptor = NetworkConnectionInterceptor(applicationContext)
        val retrofit =
                Retrofit.Builder()
                        .client(OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor).build())
                        .baseUrl(BuildConfig.API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
        val personsService = retrofit.create(PersonsService::class.java)
        val personsRemoteDataSource = PersonsRemoteDataSourceImpl(personsService)
        val personsRepository = PersonsRepositoryImpl(
                personsLocalDataSource,
                personsRemoteDataSource
        )
        val getPersonsUseCase = GetPersonsUseCase(personsRepository)
        val refreshPersonsUseCase = RefreshPersonsUseCase(personsRepository)
        val personListViewModelFactory = PersonListViewModelFactory(
                getPersonsUseCase,
                refreshPersonsUseCase
        )
        personListViewModel = ViewModelProvider(
                this,
                personListViewModelFactory
        ).get(PersonListViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        binding.swipeRefreshLayout.setOnRefreshListener {
            personListViewModel.refreshPersons()
        }

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        val personListAdapter = PersonListAdapter(PersonListListener {
//            Log.e("item clicked", it.toString())
        })
        binding.recyclerView.adapter = personListAdapter

        personListViewModel.resourceLiveData.observe(this) {
            when (it) {
                is Resource.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.errorMessageLayout.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Resource.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.errorMessageLayout.visibility = View.VISIBLE
                    it.message?.run {
                        AlertDialog.Builder(this@MainActivity)
                                .setMessage(this)
                                .setPositiveButton(android.R.string.ok, null)
                                .create()
                                .show()
//                        personListViewModel.onShowErrorMessageComplete()
                    }
                }
            }
        }

        personListViewModel.personEntitiesLiveData.observe(this) {
//            Log.e("personEntities", it.toString())
//            Log.e("personEntities count", it.count().toString())
            personListAdapter.submitList(it)
            if (it.isEmpty()) {
                personListViewModel.attemptToGetPersons()
                binding.recyclerView.visibility = View.GONE
            } else {
                personListViewModel.attemptedToGetPersons()
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }
}