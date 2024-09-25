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
import com.skripsi.jagungku.databinding.ActivityEditFactorEvaluationBinding

class EditFactorEvaluationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFactorEvaluationBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFactorEvaluationBinding.inflate(layoutInflater)

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

        getIntentData()

        val getidFaktorEvaluasi = intent.getStringExtra("idFaktorEvaluasi").toString()
        binding.btnUpdateEvaluationFaktor.setOnClickListener {
            setUpdateFaktorEvaluation(getidFaktorEvaluasi)
        }
        binding.btnDeleteEvaluationFaktor.setOnClickListener {
            deleteFactorEvaluation()
        }
    }

    private fun getIntentData() {
        binding.edtEditNamaFaktorInEvaluationFaktor.setText(intent.getStringExtra("namaFaktor"))
        binding.edtEditBobotFaktorEvaluation.setText(intent.getDoubleExtra("bobotEvaluasiFaktor", 0.0).toString())
    }

    private fun setUpdateFaktorEvaluation(idFaktorEvaluasi: String) {
        val namaFaktor = binding.edtEditNamaFaktorInEvaluationFaktor.text.toString()
        val updateBobotEvaluasiFaktor = binding.edtEditBobotFaktorEvaluation.text.toString()

        if (namaFaktor.isEmpty()) {
            binding.edtEditNamaFaktorInEvaluationFaktor.error = "Data tidak boleh kosong"
        } else if (updateBobotEvaluasiFaktor.isEmpty()) {
            binding.edtEditBobotFaktorEvaluation.error = "Data tidak boleh kosong"
        } else if (namaFaktor.isNotEmpty() || updateBobotEvaluasiFaktor.isNotEmpty()) {

            val updateBobotEvaluasiFaktorDouble = updateBobotEvaluasiFaktor.toDouble()

            updateEvaluasiFaktor(
                idFaktorEvaluasi,
                namaFaktor,
                updateBobotEvaluasiFaktorDouble
            )
        }
    }

    private fun updateEvaluasiFaktor(id: String, namaFaktor: String, bobotEvaluasiFaktor: Double) {
        val updateDataFaktorEvaluation = DataFactorEvaluation(id, namaFaktor, bobotEvaluasiFaktor)

        db.child(id).setValue(updateDataFaktorEvaluation)

        Toast.makeText(this, "Faktor Evaluasi Diubah", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteFactorEvaluation() {
        val getidFaktorEvaluasi = intent.getStringExtra("idFaktorEvaluasi").toString()
        db.child(getidFaktorEvaluasi).removeValue()

        Toast.makeText(this, "Faktor Evaluasi Dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val DATA_FAKTOR_EVALUASI = "data_faktor_evaluasi"
    }
}