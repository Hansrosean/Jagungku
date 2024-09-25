package com.skripsi.jagungku.ui

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.skripsi.jagungku.R
import com.skripsi.jagungku.databinding.ActivitySettingsBinding
import com.skripsi.jagungku.preferences.SettingPreferences
import com.skripsi.jagungku.preferences.dataStore
import com.skripsi.jagungku.viewmodel.SettingViewModel
import com.skripsi.jagungku.viewmodel.ViewModelFactory

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        switchDarkMode()


        binding.cvLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun switchDarkMode() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDarkMode.isChecked = false
            }
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun logoutUser() {
        AlertDialog.Builder(this)
            .setTitle("Logout?")
            .setMessage("Anda yakin ingin logout dari aplikasi")
            .setPositiveButton("Ya") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this@SettingsActivity, "Berhasil Logout", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("Batal", null)
            .show()

    }
}