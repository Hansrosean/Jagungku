package com.skripsi.jagungku.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.skripsi.jagungku.R
import com.skripsi.jagungku.databinding.ActivityUserMenuBinding
import com.skripsi.jagungku.ui.LoginActivity
import com.squareup.picasso.Picasso

class UserMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserMenuBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMenuBinding.inflate(layoutInflater)

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

        displayUserProfile()

        binding.btnUpdateProfile.setOnClickListener {
            startActivity(Intent(this@UserMenuActivity, EditUserProfileActivity::class.java))
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

    private fun displayUserProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val displayUsername = currentUser.displayName
            binding.tvDisplayUsername.text = displayUsername

            val displayEmail = currentUser.email
            binding.tvDisplayEmail.text = displayEmail

            // display profile pic
            val displayProfilePic =
                storageReference.child(currentUser.uid + "/profile_picture.jpg")
            displayProfilePic.downloadUrl.addOnSuccessListener { uri ->
                Picasso.get().load(uri).into(binding.imgDisplayProfilePic)
            }
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
                        Toast.makeText(this@UserMenuActivity, "Akun dihapus", Toast.LENGTH_SHORT)
                            .show()
                        startActivity((Intent(this@UserMenuActivity, LoginActivity::class.java)))
                        finish()
                    }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    companion object {
        const val USER_PROFILE = "user_profile/"
    }
}