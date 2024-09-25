package com.skripsi.jagungku.ui.admin.dss.subfactor

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
import com.skripsi.jagungku.database.dssmodel.DataSubFactor
import com.skripsi.jagungku.databinding.ActivityAddSubFactorBinding


class AddSubFactorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSubFactorBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubFactorBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseDatabase.getInstance().getReference(DATA_SUB_FAKTOR)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        binding.btnAddDataSubFaktor.setOnClickListener {
            addSubFaktor()
        }

        val getNamaFaktor = intent.getStringExtra("namaFaktor").toString()
        binding.tvAddNamaFaktorInSub.text = getNamaFaktor
    }

    private fun addSubFaktor() {
        val idSubFaktor = db.push().key!!
        val namaFaktor = binding.tvAddNamaFaktorInSub.text.toString()
        val subFaktor1 = binding.edtAddNamaSubFaktor1.text.toString()
        val subFaktor2 = binding.edtAddNamaSubFaktor2.text.toString()
        val subFaktor3 = binding.edtAddNamaSubFaktor3.text.toString()
        val nilaiSubFaktor1 = binding.edtAddNilaiSubFaktor1.text.toString()
        val nilaiSubFaktor2 = binding.edtAddNilaiSubFaktor2.text.toString()
        val nilaiSubFaktor3 = binding.edtAddNilaiSubFaktor3.text.toString()

        if (subFaktor1.isEmpty()) {
            binding.edtAddNamaSubFaktor1.error = "Data tidak boleh kosong"
        } else if (subFaktor2.isEmpty()) {
            binding.edtAddNamaSubFaktor2.error = "Data tidak boleh kosong"
        } else if (subFaktor3.isEmpty()) {
            binding.edtAddNamaSubFaktor3.error = "Data tidak boleh kosong"
        } else if (nilaiSubFaktor1.isEmpty()) {
            binding.edtAddNilaiSubFaktor1.error = "Data tidak boleh kosong"
        } else if (nilaiSubFaktor2.isEmpty()) {
            binding.edtAddNilaiSubFaktor2.error = "Data tidak boleh kosong"
        } else if (nilaiSubFaktor3.isEmpty()) {
            binding.edtAddNilaiSubFaktor3.error = "Data tidak boleh kosong"
        } else if (subFaktor1.isNotEmpty() || subFaktor2.isNotEmpty() || subFaktor3.isNotEmpty() ||
            nilaiSubFaktor1.isNotEmpty() || nilaiSubFaktor2.isNotEmpty() || nilaiSubFaktor3.isNotEmpty()
        ) {
            val dataSubFactor = DataSubFactor(
                idSubFaktor,
                namaFaktor,
                subFaktor1,
                subFaktor2,
                subFaktor3,
                nilaiSubFaktor1,
                nilaiSubFaktor2,
                nilaiSubFaktor3
            )

            db.child(idSubFaktor).setValue(dataSubFactor).addOnCompleteListener {
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
        const val DATA_SUB_FAKTOR = "data_sub_faktor"
    }
}