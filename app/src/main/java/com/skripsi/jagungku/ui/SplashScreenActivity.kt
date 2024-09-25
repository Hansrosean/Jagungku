package com.skripsi.jagungku.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.skripsi.jagungku.R
import com.skripsi.jagungku.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed ({
            // check if user not logged in or not
                val user = auth.currentUser
                if (user != null) {
                    startActivity(Intent(this, MainActivity::class.java))

                    Toast.makeText(this, "Selamat Datang", Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))

                    Toast.makeText(this, "Login Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                }
            finish()
        }, 3000L)
    }
}