package com.example.upraxisexam.presentation.personlist

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.upraxisexam.R
import com.example.upraxisexam.data.util.Resource
import com.example.upraxisexam.databinding.ActivityMainBinding
import com.example.upraxisexam.presentation.persondetails.PersonDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val personListViewModel: PersonListViewModel by viewModels()

    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
                DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        binding.swipeRefreshLayout.setOnRefreshListener {
            personListViewModel.refreshPersons()
        }

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        val personListAdapter = PersonListAdapter(PersonListListener {
//            Log.e("item clicked", it.toString())
            PersonDetailsActivity.start(this, it)
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
                        alertDialog?.dismiss()
                        alertDialog = AlertDialog.Builder(this@MainActivity)
                                .setMessage(this)
                                .setPositiveButton(android.R.string.ok, null)
                                .create()
                        alertDialog?.show()
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

    override fun onDestroy() {
        super.onDestroy()
//        Log.e("MainActivity", "onDestroy")
        alertDialog?.dismiss()
    }
}