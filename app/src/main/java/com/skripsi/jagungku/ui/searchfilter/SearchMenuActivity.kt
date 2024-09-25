package com.skripsi.jagungku.ui.searchfilter

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skripsi.jagungku.R
import com.skripsi.jagungku.databinding.ActivitySearchMenuBinding

class SearchMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMenuBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.cvMenuKetinggianTanah.setOnClickListener {
            startActivity(Intent(this, FilterKetinggianTanahListActivity::class.java))
        }

        binding.cvMenuSuhuUdara.setOnClickListener {
            startActivity(Intent(this, FilterSuhuUdaraListActivity::class.java))
        }

        binding.cvMenuPhTanah.setOnClickListener {
            startActivity(Intent(this, FilterPhTanahListActivity::class.java))
        }
    }
}