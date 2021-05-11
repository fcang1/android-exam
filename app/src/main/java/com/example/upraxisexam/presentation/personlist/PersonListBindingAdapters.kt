package com.example.upraxisexam.presentation.personlist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.upraxisexam.R
import com.example.upraxisexam.data.database.PersonEntity

@BindingAdapter("fullNameText")
fun TextView.setFullNameText(personEntity: PersonEntity) {
    text = context.getString(R.string.person_full_name, personEntity.firstName, personEntity.lastName)
}

@BindingAdapter("mobileNoText")
fun TextView.setMobileNoText(personEntity: PersonEntity) {
    text = personEntity.mobileNo
}