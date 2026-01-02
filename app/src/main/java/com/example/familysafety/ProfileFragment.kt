package com.example.familysafety

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.familysafety.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var isEditing = false
    private val calendar = Calendar.getInstance()

    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            binding.imageProfile.setImageBitmap(bitmap)
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            binding.imageProfile.setImageURI(uri)
        }
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) openCamera()
        else Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProfileData()
        setEditTextsEnabled(false)

        binding.imageProfile.setOnClickListener { showImagePickerDialog() }

        binding.edtdob.setOnClickListener { showDatePicker() }

        binding.btnclick.setOnClickListener {
            if (!isEditing) {
                isEditing = true
                setEditTextsEnabled(true)
                binding.btnclick.text = "Save"
            } else {
                if (validateFields()) {
                    saveProfileData()
                    isEditing = false
                    setEditTextsEnabled(false)
                    binding.btnclick.text = "Edit Profile"
                    Toast.makeText(requireContext(), "Profile saved!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.textPrivacy.setOnClickListener {
            startActivity(Intent(requireContext(), PrivacyActivity::class.java))
        }

        binding.textAccount.setOnClickListener {
            startActivity(Intent(requireContext(), AccountActivity::class.java))
        }

        binding.textNotification.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }

        binding.textDeleteAccount.setOnClickListener {
            startActivity(Intent(requireContext(), DeleteAccountActivity::class.java))
        }
    }

    private fun setEditTextsEnabled(enabled: Boolean) {
        binding.edtusername.isEnabled = enabled
        binding.edtmobilenumber.isEnabled = enabled
        binding.edtdob.isEnabled = enabled
        binding.edtemail.isEnabled = enabled
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> checkCameraPermission()
                    1 -> openGallery()
                    2 -> dialog.dismiss()
                }
            }.show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, y, m, d ->
            calendar.set(y, m, d)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.edtdob.setText(dateFormat.format(calendar.time))
        }, year, month, day).show()
    }

    private fun validateFields(): Boolean {
        val name = binding.edtusername.text.toString().trim()
        val mobile = binding.edtmobilenumber.text.toString().trim()
        val dob = binding.edtdob.text.toString().trim()
        val email = binding.edtemail.text.toString().trim()

        return when {
            name.isEmpty() || mobile.isEmpty() || dob.isEmpty() || email.isEmpty() -> {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
                false
            }
            mobile.length < 10 -> {
                Toast.makeText(requireContext(), "Mobile number too short", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun saveProfileData() {
        val editor = sharedPrefs.edit()
        editor.putString("name", binding.edtusername.text.toString())
        editor.putString("mobile", binding.edtmobilenumber.text.toString())
        editor.putString("dob", binding.edtdob.text.toString())
        editor.putString("email", binding.edtemail.text.toString())
        editor.apply()
    }

    private fun loadProfileData() {
        binding.edtusername.setText(sharedPrefs.getString("name", ""))
        binding.edtmobilenumber.setText(sharedPrefs.getString("mobile", ""))
        binding.edtdob.setText(sharedPrefs.getString("dob", ""))
        binding.edtemail.setText(sharedPrefs.getString("email", ""))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}
