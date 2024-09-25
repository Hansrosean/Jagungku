package com.skripsi.jagungku.ui.admin.dss.alternative

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
import com.skripsi.jagungku.database.dssmodel.DataAlternativeEvaluation
import com.skripsi.jagungku.databinding.ActivityAddAlternativeValueBinding

class AddAlternativeValueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlternativeValueBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlternativeValueBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getNamaAlternatif = intent.getStringExtra("namaAlternatif").toString()
        binding.tvLabelAddNamaAlternativeInAlternativeValue.text = getNamaAlternatif

        db = FirebaseDatabase.getInstance().getReference(DATA_ALTERNATIF_EVALUASI)
            .child(getNamaAlternatif)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        binding.btnAddAlternativeValue.setOnClickListener {
            addAlternativeValue()
        }

        val getNamaFaktor = intent.getStringExtra("namaFaktor").toString()
        binding.tvAddNamaFaktorInAlternativeValue.text = getNamaFaktor

        val getBobotEvaluasiFaktor = intent.getDoubleExtra("bobotEvaluasiFaktor", 0.0)
        binding.edtBobotFaktorEvaluasi.setText(getBobotEvaluasiFaktor.toString())
    }

    private fun addAlternativeValue() {
        val idAlternatifEvaluasi = db.push().key!!
        val namaAlternatif = binding.tvLabelAddNamaAlternativeInAlternativeValue.text.toString()
        val bobotFaktorEvaluasi = binding.edtBobotFaktorEvaluasi.text.toString()
        val nilaiAlternatif = binding.edtAlternativeValue.text.toString()

        if (bobotFaktorEvaluasi.isEmpty()) {
            binding.edtBobotFaktorEvaluasi.error = "Data tidak boleh kosong"
        } else if (nilaiAlternatif.isEmpty()) {
            binding.edtAlternativeValue.error = "Data tidak boleh kosong"
        } else if (bobotFaktorEvaluasi.isNotEmpty() || nilaiAlternatif.isNotEmpty()) {
            val bobotFaktorEvaluasiDouble = bobotFaktorEvaluasi.toDouble()
            val nilaiAlternatifDouble = nilaiAlternatif.toDouble()

            val evaluasiAlternatif = bobotFaktorEvaluasiDouble * nilaiAlternatifDouble

            val dataAlternativeEvaluation = DataAlternativeEvaluation(
                idAlternatifEvaluasi,
                namaAlternatif,
                evaluasiAlternatif
            )

            db.child(idAlternatifEvaluasi).setValue(dataAlternativeEvaluation)
                .addOnCompleteListener {
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
        const val DATA_ALTERNATIF_EVALUASI = "data_alternatif_evaluasi"
    }
}