package com.skripsi.jagungku.ui.admin.dss

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skripsi.jagungku.R
import com.skripsi.jagungku.databinding.ActivityDssMenuBinding
import com.skripsi.jagungku.ui.admin.dss.alternative.AlternativeNameListActivity
import com.skripsi.jagungku.ui.admin.dss.evaluation.EvaluationActivity
import com.skripsi.jagungku.ui.admin.dss.factor.FactorListActivity
import com.skripsi.jagungku.ui.admin.dss.rank.RankListActivity

class DssMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDssMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDssMenuBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.cvDataFaktor.setOnClickListener {
            startActivity(Intent(this, FactorListActivity::class.java))
        }

        binding.cvDataAlternatif.setOnClickListener {
            startActivity(Intent(this, AlternativeNameListActivity::class.java))
        }

        binding.cvDataPerhitungan.setOnClickListener {
            startActivity(Intent(this@DssMenuActivity, EvaluationActivity::class.java))
        }

        binding.cvDataRanking.setOnClickListener {
            startActivity(Intent(this@DssMenuActivity, RankListActivity::class.java))
        }
    }
}