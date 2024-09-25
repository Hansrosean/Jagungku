package com.skripsi.jagungku.ui.user

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.skripsi.jagungku.R
import com.skripsi.jagungku.databinding.ActivityEditUserProfileBinding
import com.skripsi.jagungku.ui.LoginActivity
import com.squareup.picasso.Picasso

class EditUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserProfileBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserProfileBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        storageReference = FirebaseStorage.getInstance().getReference(USER_PROFILE)

        displayProfile()

        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }

        binding.imgUpdateProfilePic.setOnClickListener {
            selectImage()
        }

        binding.btnDeleteAccount.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                val uid = auth.currentUser?.uid
                if (uid == "uo12qjBvsJZSLk4cfwZ7XDKoyx43") {
                    Toast.makeText(this, "Akun admin tidak dapat dihapus!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    deleteAccount()
                }
            }
        }
    }

    private fun selectImage() {
        val openGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(openGallery, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val imageUri: Uri = data?.data!!
                binding.imgUpdateProfilePic.setImageURI(imageUri)

                AlertDialog.Builder(this)
                    .setTitle("Unggah Foto Profil?")
                    .setMessage("Foto profil Anda akan diganti")
                    .setPositiveButton("Unggah") { _, _ ->
                        uploadImage(imageUri)
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }
    }

    // update profile pic
    private fun uploadImage(imageUri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        val fileRef: StorageReference =
            storageReference.child(currentUser!!.uid + "/profile_picture.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(this, "Upload Gambar Berhasil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayProfile() {
        // display username and email
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val displayUsername = currentUser.displayName
            binding.edtUpdateUsername.setText(displayUsername)

            val displayEmail = currentUser.email
            binding.edtUpdateEmail.setText(displayEmail)
        }

        // display profile pic
        val displayProfilePic = storageReference.child(currentUser!!.uid + "/profile_picture.jpg")
        displayProfilePic.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(binding.imgUpdateProfilePic)
        }
    }

    private fun updateProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        // update username
        val username = binding.edtUpdateUsername.text.toString()

        val updateUsername = UserProfileChangeRequest.Builder().apply {
            displayName = username
        }.build()

        currentUser?.updateProfile(updateUsername)

        // update email & password
        val email = binding.edtUpdateEmail.text.toString()
        val password = binding.edtUpdatePassword.text.toString()

        // get old email & password
        val oldEmail = auth.currentUser?.email.toString()
        val oldPassword = binding.edtOldPassword.text.toString()

        val credentials = EmailAuthProvider.getCredential(oldEmail, oldPassword)

        currentUser?.reauthenticate(credentials)?.addOnSuccessListener {
            currentUser.updateEmail(email)
            currentUser.updatePassword(password)
            Toast.makeText(
                this@EditUserProfileActivity,
                "Profil Berhasil Diubah",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun deleteAccount() {
        val currentUser = FirebaseAuth.getInstance().currentUser!!
        AlertDialog.Builder(this)
            .setTitle("Hapus Akun?")
            .setMessage("Akun tidak dapat dikembalikan")
            .setPositiveButton("Ya") { _, _ ->
                currentUser.delete()
                    .addOnSuccessListener {
                        Toast.makeText(this@EditUserProfileActivity, "Akun dihapus", Toast.LENGTH_SHORT)
                            .show()
                        startActivity((Intent(this@EditUserProfileActivity, LoginActivity::class.java)))
                        finish()
                    }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    companion object {
        const val REQUEST_CODE = 1000
        const val USER_PROFILE = "user_profile/"
    }
}