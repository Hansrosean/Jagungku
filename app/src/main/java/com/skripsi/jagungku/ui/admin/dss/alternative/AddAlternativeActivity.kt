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
import com.skripsi.jagungku.database.dssmodel.DataAlternative
import com.skripsi.jagungku.databinding.ActivityAddAlternativeBinding

class AddAlternativeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlternativeBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlternativeBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseDatabase.getInstance().getReference(DATA_ALTERNATIF)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        binding.btnAddAlternatif.setOnClickListener {
            addAlternatif()
        }
    }

    private fun addAlternatif() {
        val idAlternatif = db.push().key!!
        val namaAlternatif = binding.edtAddNamaAlternatif.text.toString()

        if (namaAlternatif.isEmpty()) {
            binding.edtAddNamaAlternatif.error = "Data tidak boleh kosong"
        } else if (namaAlternatif.isNotEmpty()) {
            val dataAlternatif = DataAlternative(
                idAlternatif,
                namaAlternatif,
            )

            db.child(idAlternatif).setValue(dataAlternatif).addOnCompleteListener {
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
        const val DATA_ALTERNATIF = "data_alternatif"
    }
}