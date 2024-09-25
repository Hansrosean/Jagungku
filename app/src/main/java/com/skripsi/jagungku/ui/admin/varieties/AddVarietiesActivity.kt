package com.skripsi.jagungku.ui.admin.varieties

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
import com.skripsi.jagungku.database.DataVarieties
import com.skripsi.jagungku.databinding.ActivityAddVarietiesBinding

class AddVarietiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVarietiesBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVarietiesBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseDatabase.getInstance().getReference(DATA_VARIETAS)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        binding.btnAddData.setOnClickListener {
            addDataVarieties()
        }
    }

    private fun addDataVarieties() {
        val idVarieties = db.push().key!!
        val namaVarietas = binding.edtNamaVarietas.text.toString()
        val hargaBeli = binding.edtHargaBeli.text.toString()
        val potensiHasil = binding.edtPotensiHasil.text.toString()
        val usiaTanam = binding.edtUsiaTanam.text.toString()
        val ketinggianTanah = binding.edtKetinggianTanah.text.toString()
        val kekebalanHama = binding.edtKekebalanHama.text.toString()
        val ukuranTongkol = binding.edtUkuranTongkol.text.toString()
        val suhuUdara = binding.edtSuhuUdara.text.toString()
        val phTanah = binding.edtPhTanah.text.toString()

        if (namaVarietas.isEmpty()) {
            binding.edtNamaVarietas.error = "Data tidak boleh kosong"
        } else if (hargaBeli.isEmpty()) {
            binding.edtHargaBeli.error = "Data tidak boleh kosong"
        } else if (potensiHasil.isEmpty()) {
            binding.edtPotensiHasil.error = "Data tidak boleh kosong"
        } else if (usiaTanam.isEmpty()) {
            binding.edtUsiaTanam.error = "Data tidak boleh kosong"
        } else if (ketinggianTanah.isEmpty()) {
            binding.edtKetinggianTanah.error = "Data tidak boleh kosong"
        } else if (kekebalanHama.isEmpty()) {
            binding.edtKekebalanHama.error = "Data tidak boleh kosong"
        } else if (ukuranTongkol.isEmpty()) {
            binding.edtUkuranTongkol.error = "Data tidak boleh kosong"
        } else if (suhuUdara.isEmpty()) {
            binding.edtSuhuUdara.error = "Data tidak boleh kosong"
        } else if (phTanah.isEmpty()) {
            binding.edtPhTanah.error = "Data tidak boleh kosong"
        } else if (namaVarietas.isNotEmpty() || hargaBeli.isNotEmpty() || potensiHasil.isNotEmpty() ||
            usiaTanam.isNotEmpty() || ketinggianTanah.isNotEmpty() || kekebalanHama.isNotEmpty() ||
            ukuranTongkol.isNotEmpty() || suhuUdara.isNotEmpty() || phTanah.isNotEmpty()
        ) {

            val ketinggianTanahDouble = ketinggianTanah.toDouble()
            val suhuUdaraDouble = suhuUdara.toDouble()
            val phTanahDouble = phTanah.toDouble()

            val dataVarieties = DataVarieties(
                idVarieties,
                namaVarietas,
                hargaBeli,
                potensiHasil,
                usiaTanam,
                ketinggianTanahDouble,
                kekebalanHama,
                ukuranTongkol,
                suhuUdaraDouble,
                phTanahDouble
            )

            db.child(idVarieties).setValue(dataVarieties).addOnCompleteListener {
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
        const val DATA_VARIETAS = "data_varietas"
    }
}
