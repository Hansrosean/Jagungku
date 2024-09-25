package com.skripsi.jagungku.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.skripsi.jagungku.R
import com.skripsi.jagungku.databinding.ActivityMainBinding
import com.skripsi.jagungku.preferences.SettingPreferences
import com.skripsi.jagungku.preferences.dataStore
import com.skripsi.jagungku.ui.admin.dss.DssMenuActivity
import com.skripsi.jagungku.ui.admin.pricelist.PriceListActivity
import com.skripsi.jagungku.ui.admin.varieties.ListVarietiesActivity
import com.skripsi.jagungku.ui.searchfilter.SearchMenuActivity
import com.skripsi.jagungku.ui.user.EditUserProfileActivity
import com.skripsi.jagungku.ui.user.UserMenuActivity
import com.skripsi.jagungku.viewmodel.MainViewModel
import com.skripsi.jagungku.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        databaseReference = FirebaseDatabase.getInstance().getReference(DATA_USER).child("user")

        getUserInfo()

        binding.cvVarieties.setOnClickListener {
            startActivity(Intent(this, ListVarietiesActivity::class.java))
        }
        binding.cvPriceList.setOnClickListener {
            startActivity(Intent(this, PriceListActivity::class.java))
        }
        binding.cvFactor.setOnClickListener {
            startActivity(Intent(this, DssMenuActivity::class.java))
        }
        binding.cvUsers.setOnClickListener {
            startActivity(Intent(this, UserMenuActivity::class.java))
        }
        binding.icSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.cvFilterMenu.setOnClickListener {
            startActivity(Intent(this, SearchMenuActivity::class.java))
        }

        // set view for user
        val user = auth.currentUser
        if (user != null) {
            val uid = auth.currentUser?.uid
            when (uid) {
                "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                    binding.tvTitleMenuVarietas.text = "Kelola Varietas"
                    binding.tvTitleMenuSpk.text = "Kelola SPK"
                    binding.tvTitleMenuDaftarHarga.text = "Kelola \n Daftar Harga"
                    binding.tvTitleMenuAkun.text = "Kelola Akun"
                }
                "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                    binding.tvTitleMenuVarietas.text = "Kelola Varietas"
                    binding.tvTitleMenuSpk.text = "Kelola SPK"
                    binding.tvTitleMenuDaftarHarga.text = "Kelola \n Daftar Harga"
                    binding.tvTitleMenuAkun.text = "Kelola Akun"
                }
                else -> {
                    binding.tvTitleMenuVarietas.text = "Data Varietas"
                    binding.tvTitleMenuSpk.text = "Sistem \n Pendukung Keputusan"
                    binding.tvTitleMenuDaftarHarga.text = "Daftar Harga"
                    binding.tvTitleMenuAkun.text = "Akun"
                }
            }
        }

        getThemeSetting()
    }

    private fun getThemeSetting() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun getUserInfo() {
        val showUsername = auth.currentUser?.displayName

        binding.tvShowLoginUserName.text = showUsername.toString()
    }

    companion object {
        const val DATA_USER = "data_user"
    }

    override fun onBackPressed() {
        super.onBackPressedDispatcher.onBackPressed()
        finishAffinity()
    }
}