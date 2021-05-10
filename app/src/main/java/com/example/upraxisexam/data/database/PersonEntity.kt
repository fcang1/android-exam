package com.example.upraxisexam.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
        @ColumnInfo(name = "first_name") val firstName: String?,
        @ColumnInfo(name = "last_name") val lastName: String?,
        @ColumnInfo(name = "birthday") val birthday: String?,
        @ColumnInfo(name = "age") val age: Int?,
        @ColumnInfo(name = "email_address") val emailAddress: String?,
        @ColumnInfo(name = "mobile_no") val mobileNo: String?,
        @ColumnInfo(name = "address") val address: String?,
        @ColumnInfo(name = "contact_person") val contactPerson: String?,
        @ColumnInfo(name = "contact_person_phone_no") val contactPersonPhoneNo: String?,
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
)