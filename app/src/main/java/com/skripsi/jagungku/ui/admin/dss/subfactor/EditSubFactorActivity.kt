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
import com.skripsi.jagungku.databinding.ActivityEditSubFactorBinding

class EditSubFactorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSubFactorBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSubFactorBinding.inflate(layoutInflater)

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

        getIntentData()

        val getIdSubFaktor = intent.getStringExtra("idSubFaktor").toString()
        binding.btnUpdateDataSubFaktor.setOnClickListener {
            setUpdateSubFaktor(getIdSubFaktor)
        }

        binding.btnDeleteDataSubFaktor.setOnClickListener {
            deleteSubFaktor()
        }
    }

    private fun getIntentData() {
        binding.tvUpdateNamaFaktorInSub.text = intent.getStringExtra("namaFaktor")
        binding.edtUpdateNamaSubFaktor1.setText(intent.getStringExtra("subFaktor1"))
        binding.edtUpdateNamaSubFaktor2.setText(intent.getStringExtra("subFaktor2"))
        binding.edtUpdateNamaSubFaktor3.setText(intent.getStringExtra("subFaktor3"))
        binding.edtUpdateNilaiSubFaktor1.setText(intent.getStringExtra("nilaiSubFaktor1"))
        binding.edtUpdateNilaiSubFaktor2.setText(intent.getStringExtra("nilaiSubFaktor2"))
        binding.edtUpdateNilaiSubFaktor3.setText(intent.getStringExtra("nilaiSubFaktor3"))
    }

    private fun setUpdateSubFaktor(idSubFaktor: String) {
        val namaFaktor = binding.tvUpdateNamaFaktorInSub.text.toString()
        val updateNamaSubfaktor1 = binding.edtUpdateNamaSubFaktor1.text.toString()
        val updateNamaSubfaktor2 = binding.edtUpdateNamaSubFaktor2.text.toString()
        val updateNamaSubfaktor3 = binding.edtUpdateNamaSubFaktor3.text.toString()
        val updateNilaiSubFaktor1 = binding.edtUpdateNilaiSubFaktor1.text.toString()
        val updateNilaiSubFaktor2 = binding.edtUpdateNilaiSubFaktor2.text.toString()
        val updateNilaiSubFaktor3 = binding.edtUpdateNilaiSubFaktor3.text.toString()

        if (updateNamaSubfaktor1.isEmpty()) {
            binding.edtUpdateNamaSubFaktor1.error = "Data tidak boleh kosong"
        } else if (updateNamaSubfaktor2.isEmpty()) {
            binding.edtUpdateNamaSubFaktor2.error = "Data tidak boleh kosong"
        } else if (updateNamaSubfaktor3.isEmpty()) {
            binding.edtUpdateNamaSubFaktor3.error = "Data tidak boleh kosong"
        } else if (updateNilaiSubFaktor1.isEmpty()) {
            binding.edtUpdateNilaiSubFaktor1.error = "Data tidak boleh kosong"
        } else if (updateNilaiSubFaktor2.isEmpty()) {
            binding.edtUpdateNilaiSubFaktor2.error = "Data tidak boleh kosong"
        } else if (updateNilaiSubFaktor3.isEmpty()) {
            binding.edtUpdateNilaiSubFaktor3.error = "Data tidak boleh kosong"
        } else if (updateNamaSubfaktor1.isNotEmpty() || updateNamaSubfaktor2.isNotEmpty() ||
            updateNamaSubfaktor3.isNotEmpty() || updateNilaiSubFaktor1.isNotEmpty() ||
            updateNilaiSubFaktor2.isNotEmpty() || updateNilaiSubFaktor3.isNotEmpty()
        ) {
            updateSubFaktor(
                idSubFaktor,
                namaFaktor,
                updateNamaSubfaktor1,
                updateNamaSubfaktor2,
                updateNamaSubfaktor3,
                updateNilaiSubFaktor1,
                updateNilaiSubFaktor2,
                updateNilaiSubFaktor3
            )
        }
    }

    private fun updateSubFaktor(
        id: String,
        namaFaktor: String,
        subFaktor1: String,
        subFaktor2: String,
        subFaktor3: String,
        nilaiSubFaktor1: String,
        nilaiSubFaktor2: String,
        nilaiSubFaktor3: String
    ) {
        val updateDataSubFaktor = DataSubFactor(
            id,
            namaFaktor,
            subFaktor1,
            subFaktor2,
            subFaktor3,
            nilaiSubFaktor1,
            nilaiSubFaktor2,
            nilaiSubFaktor3
        )
        db.child(id).setValue(updateDataSubFaktor)

        Toast.makeText(this, "Sub Faktor Diubah", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteSubFaktor() {
        val getIdSubFaktor = intent.getStringExtra("idSubFaktor").toString()
        db.child(getIdSubFaktor).removeValue()

        Toast.makeText(this, "Sub Faktor Dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val DATA_SUB_FAKTOR = "data_sub_faktor"
    }
}