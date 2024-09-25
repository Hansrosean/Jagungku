package com.skripsi.jagungku.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.DataUser


class RegisterActivity : AppCompatActivity() {
    private lateinit var edtUserName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtUserName = findViewById(R.id.edt_user_name)
        edtEmail = findViewById(R.id.edt_user_email)
        edtPassword = findViewById(R.id.edt_user_password)

        db = FirebaseDatabase.getInstance().getReference(DATA_USER)

        btnSignUp = findViewById(R.id.btn_signup)
        btnSignUp.setOnClickListener {
            addDataUser()
        }

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
    }

    private fun addDataUser() {
        val userName = edtUserName.text.toString()
        val userEmail = edtEmail.text.toString()
        val userPassword = edtPassword.text.toString()

        if (userName.isEmpty()) {
            edtUserName.error = "Nama tidak boleh kosong"
        } else if (userEmail.isEmpty()) {
            edtEmail.error = "Email tidak boleh kosong"
        } else if (userPassword.isEmpty()) {
            edtPassword.error = "Password tidak boleh kosong"
        } else if (userName.isNotEmpty() || userEmail.isNotEmpty() || userPassword.isNotEmpty()) {

            val userId = db.push().key!!

            val dataUser = DataUser(userId, userName, userEmail, userPassword)
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        db.ref.child(userId).setValue(dataUser)
                        Log.d(TAG, "addDataUser:success")

                        val setUserProfile = userProfileChangeRequest {
                            displayName = userName
                        }
                        auth.currentUser!!.updateProfile(setUserProfile)
                            .addOnCompleteListener { taskProfile ->
                                if (taskProfile.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        getString(R.string.register_success),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                            }
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthUserCollisionException) {
                            edtEmail.error = getString(R.string.email_already_registered)
                            Toast.makeText(
                                this,
                                getString(R.string.email_already_registered),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    companion object {
        const val DATA_USER = "data_user"
        const val TAG = "RegisterActivity"
    }
}