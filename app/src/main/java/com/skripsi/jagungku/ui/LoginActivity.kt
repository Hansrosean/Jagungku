package com.skripsi.jagungku.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.skripsi.jagungku.R
import com.skripsi.jagungku.ui.RegisterActivity.Companion.TAG


class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvToRegisterActivity: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtEmail = findViewById(R.id.edt_user_email)
        edtPassword = findViewById(R.id.edt_user_password)
        btnLogin = findViewById(R.id.btn_login)
        tvToRegisterActivity = findViewById(R.id.tv_to_register_activity)

        tvToRegisterActivity.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            login()
        }

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
    }

    private fun login() {
        val userEmail = edtEmail.text.toString()
        val userPassword = edtPassword.text.toString()

        if (userEmail.isEmpty()) {
            edtEmail.error = "Email tidak boleh kosong"
        } else if (userPassword.isEmpty()) {
            edtPassword.error = "Password tidak boleh kosong"
        } else if (userEmail.isNotEmpty() || userPassword.isNotEmpty()) {

            auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "loginUser:success")
                        Toast.makeText(
                            this,
                            getString(R.string.login_success),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        val user: FirebaseUser? = auth.currentUser
                        moveToMainActivity(user)
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: Exception) {
                            if (e is FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    this,
                                    "Email atau password salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }
    }

    private fun moveToMainActivity(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
        finishAffinity()
    }
}