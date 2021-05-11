package com.example.upraxisexam.presentation.persondetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.upraxisexam.R
import com.example.upraxisexam.data.database.PersonEntity
import com.example.upraxisexam.databinding.ActivityPersonDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class PersonDetailsActivity : AppCompatActivity() {

    companion object {
        private const val PERSON_ENTITY_EXTRA = "person_entity_extra"

        @JvmStatic
        fun start(
            context: Context,
            personEntity: PersonEntity
        ) {
            val starter = Intent(
                context,
                PersonDetailsActivity::class.java
            ).apply {
                putExtra(PERSON_ENTITY_EXTRA, personEntity)
            }
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityPersonDetailsBinding>(
                this,
                R.layout.activity_person_details
            )

        val personEntity = intent.getParcelableExtra<PersonEntity>(PERSON_ENTITY_EXTRA)
        personEntity?.let {
            binding.firstNameTextView.text = it.firstName
            binding.lastNameTextView.text = it.lastName
            binding.birthdayTextView.text = formatDate(it.birthday)
            binding.ageTextView.text = it.age.toString()
            binding.emailAddressTextView.text = it.emailAddress
            binding.mobileNoTextView.text = it.mobileNo
            binding.addressTextView.text = it.address
            binding.contactPersonTextView.text = it.contactPerson
            binding.contactPersonPhoneNoTextView.text = it.contactPersonPhoneNo
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun formatDate(dateString: String?): String {
        dateString?.let {
            val sourceSimpleDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)
            val targetSimpleDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
            sourceSimpleDateFormat.parse(dateString)?.let {
                return targetSimpleDateFormat.format(it)
            }
        }
        return ""
    }
}