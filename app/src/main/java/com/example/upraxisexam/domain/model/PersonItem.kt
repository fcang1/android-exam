package com.example.upraxisexam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonItem(
    val firstName: String?,
    val lastName: String?,
    val birthday: String?,
    val age: Int?,
    val emailAddress: String?,
    val mobileNo: String?,
    val address: String?,
    val contactPerson: String?,
    val contactPersonPhoneNo: String?
) : Parcelable
