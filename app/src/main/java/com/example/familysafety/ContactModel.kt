package com.example.familysafety

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactModel(
    val name: String,
    val phone: String,
    var isInvited: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
