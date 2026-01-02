package com.example.familysafety

import MemberAdapter
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familysafety.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactList: MutableList<ContactModel>
    private lateinit var memberAdapter: MemberAdapter
    private lateinit var inviteAdapter: InviteAdapter
    private lateinit var database: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
        setupRecyclerViews()
        observeContacts()
        checkPermissionsAndLoadContacts()
    }

    private fun setupRecyclerViews() {
        memberAdapter = MemberAdapter(mutableListOf()) { contact ->
            removeInvitedContact(contact)
        }

        inviteAdapter = InviteAdapter(emptyList()) { contact ->
            inviteContact(contact)
        }

        binding.recyclerMember.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMember.adapter = memberAdapter

        binding.recyclerInvite.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerInvite.adapter = inviteAdapter
    }

    private fun observeContacts() {
        database.contactDao().getInvitedContacts().observe(viewLifecycleOwner) {
            memberAdapter.updateList(it.toMutableList())
        }

        database.contactDao().getUninvitedContacts().observe(viewLifecycleOwner) {
            inviteAdapter.updateList(it)
        }
    }

    private fun inviteContact(contact: ContactModel) {
        contact.isInvited = true
        lifecycleScope.launch(Dispatchers.IO) {
            database.contactDao().updateContact(contact)
        }
    }

    private fun removeInvitedContact(contact: ContactModel) {
        contact.isInvited = false
        lifecycleScope.launch(Dispatchers.IO) {
            database.contactDao().updateContact(contact)
        }
    }

    private fun checkPermissionsAndLoadContacts() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                100
            )
        } else {
            loadDeviceContacts()
        }
    }

    private fun loadDeviceContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val existingContacts = database.contactDao().getAllContactsNow()

            val existingNumbers = existingContacts.map { it.phone }.toSet()

            val contactsFromDevice = mutableListOf<ContactModel>()
            val cursor = requireContext().contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null
            )
            cursor?.use {
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex) ?: "Unknown"
                    val number = it.getString(numberIndex) ?: ""
                    // Add only if number not already in DB
                    if (!existingNumbers.contains(number)) {
                        contactsFromDevice.add(ContactModel(name, number))
                    }
                }
            }

            if (contactsFromDevice.isNotEmpty()) {
                database.contactDao().insertAll(contactsFromDevice)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
