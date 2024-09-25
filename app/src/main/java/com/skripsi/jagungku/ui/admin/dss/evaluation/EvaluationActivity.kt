package com.skripsi.jagungku.ui.admin.dss.evaluation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skripsi.jagungku.R
import com.skripsi.jagungku.databinding.ActivityEvaluationBinding
import com.skripsi.jagungku.ui.admin.dss.alternative.AlternativeListActivity
import com.skripsi.jagungku.ui.admin.dss.evaluation.factorevaluation.FactorEvaluationListActivity

class EvaluationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEvaluationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluationBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.cvEvaluasiFaktor.setOnClickListener {
            startActivity(Intent(this@EvaluationActivity, FactorEvaluationListActivity::class.java))
        }
        binding.cvEvaluasiAlternatif.setOnClickListener {
            startActivity(Intent(this@EvaluationActivity, AlternativeListActivity::class.java))
        }
    }
}