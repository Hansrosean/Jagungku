package com.skripsi.jagungku.ui.admin.dss.evaluation.factorevaluation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.dssmodel.DataFactorEvaluation
import com.skripsi.jagungku.database.dssmodel.DataSubFactor
import com.skripsi.jagungku.databinding.ActivityAddFactorEvaluationBinding

class AddFactorEvaluationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFactorEvaluationBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFactorEvaluationBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseDatabase.getInstance().getReference(DATA_FAKTOR_EVALUASI)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        binding.btnAddEvaluationFaktor.setOnClickListener {
            addFaktorEvaluasi()
        }

        val getNamaFaktor = intent.getStringExtra("namaFaktor").toString()
        binding.tvAddNamaFaktorInEvaluation.text = getNamaFaktor

        val getBobotFaktor = intent.getDoubleExtra("bobotFaktor", 0.0).toString()
        binding.edtBobotFaktor.setText(getBobotFaktor)
    }

    private fun addFaktorEvaluasi() {
        val idFaktorEvaluasi = db.push().key!!
        val namaFaktor = binding.tvAddNamaFaktorInEvaluation.text.toString()
        val bobotFaktor = binding.edtBobotFaktor.text.toString()
        val bobotFaktorSum = binding.edtBobotFaktorSum.text.toString()

        if (bobotFaktor.isEmpty()) {
            binding.edtBobotFaktor.error = "Data tidak boleh kosong"
        } else if (bobotFaktorSum.isEmpty()) {
            binding.edtBobotFaktorSum.error = "Data tidak boleh kosong"
        } else if (bobotFaktor.isNotEmpty() || bobotFaktorSum.isNotEmpty()) {
            val bobotFaktorDouble = bobotFaktor.toDouble()
            val bobotFaktorSumDouble = bobotFaktorSum.toDouble()

            val totalEvaluasiBobot = bobotFaktorDouble / bobotFaktorSumDouble

            val dataFactorEvaluation = DataFactorEvaluation(
                idFaktorEvaluasi,
                namaFaktor,
                totalEvaluasiBobot
            )

            db.child(idFaktorEvaluasi).setValue(dataFactorEvaluation).addOnCompleteListener {
                Toast.makeText(this, "Data Ditambahkan", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener { error ->
                Toast.makeText(
                    this,
                    "Gagal Menambah Data, error ${error.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    companion object {
        const val DATA_FAKTOR_EVALUASI = "data_faktor_evaluasi"
    }
}