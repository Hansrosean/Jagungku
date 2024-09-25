package com.skripsi.jagungku.ui.admin.dss.factor

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
import com.skripsi.jagungku.database.dssmodel.DataFactor
import com.skripsi.jagungku.databinding.ActivityAddFactorBinding

class AddFactorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFactorBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFactorBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseDatabase.getInstance().getReference(DATA_FAKTOR)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        binding.btnAddFaktor.setOnClickListener {
            addFactorAndFactorEvaluation()
        }
    }

    private fun addFactorAndFactorEvaluation() {
        val idFaktor = db.push().key!!
        val kodeFaktor = binding.edtAddKodeFaktor.text.toString()
        val namaFaktor = binding.edtAddNamaFaktor.text.toString()
        val bobotFaktor = binding.edtAddBobotFaktor.text.toString()

        if (kodeFaktor.isEmpty()) {
            binding.edtAddKodeFaktor.error = "Data tidak boleh kosong"
        } else if (namaFaktor.isEmpty()) {
            binding.edtAddNamaFaktor.error = "Data tidak boleh kosong"
        } else if (bobotFaktor.isEmpty()) {
            binding.edtAddBobotFaktor.error = "Data tidak boleh kosong"
        } else if (kodeFaktor.isNotEmpty() || namaFaktor.isNotEmpty() || bobotFaktor.isNotEmpty()) {
            val bobotFaktorDouble = bobotFaktor.toDouble()

            val dataFaktor = DataFactor(
                idFaktor,
                kodeFaktor,
                namaFaktor,
                bobotFaktorDouble
            )

            db.child(idFaktor).setValue(dataFaktor).addOnCompleteListener {
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
        const val DATA_FAKTOR = "data_faktor"
    }
}