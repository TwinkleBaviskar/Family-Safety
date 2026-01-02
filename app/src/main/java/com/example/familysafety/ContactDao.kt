package com.example.familysafety

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
 @Insert(onConflict = OnConflictStrategy.IGNORE)
 suspend fun insertAll(contacts: List<ContactModel>)

 @Update
 suspend fun updateContact(contact: ContactModel)

 @Delete
 suspend fun deleteContact(contact: ContactModel)

 @Query("SELECT * FROM contacts WHERE isInvited = 1")
 fun getInvitedContacts(): LiveData<List<ContactModel>>

 @Query("SELECT * FROM contacts WHERE isInvited = 0")
 fun getUninvitedContacts(): LiveData<List<ContactModel>>

 // New function to get all contacts synchronously (to check if DB already has contacts)
 @Query("SELECT * FROM contacts")
 fun getAllContactsNow(): List<ContactModel>
}
