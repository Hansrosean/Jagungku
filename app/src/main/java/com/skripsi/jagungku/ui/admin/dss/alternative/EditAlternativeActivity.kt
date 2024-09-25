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
import com.skripsi.jagungku.databinding.ActivityEditAlternativeBinding

class EditAlternativeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAlternativeBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAlternativeBinding.inflate(layoutInflater)

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

        getIntentData()

        val getIdAlternatif = intent.getStringExtra("idAlternatif").toString()
        binding.btnUpdateAlternatif.setOnClickListener {
            setUpdateAlternatif(getIdAlternatif)
        }

        binding.btnDeleteAlternatif.setOnClickListener {
            deleteAlternatif()
        }
    }

    private fun getIntentData() {
        binding.edtUpdateNamaAlternatif.setText(intent.getStringExtra("namaAlternatif"))
    }

    private fun setUpdateAlternatif(idAlternatif: String) {
        val updateNamaAlternatif = binding.edtUpdateNamaAlternatif.text.toString()

        if (updateNamaAlternatif.isEmpty()) {
            binding.edtUpdateNamaAlternatif.error = "Data tidak boleh kosong"
        } else if (updateNamaAlternatif.isNotEmpty()) {
            updateAlternatif(
                idAlternatif,
                updateNamaAlternatif
            )
        }
    }

    private fun updateAlternatif(
        id: String,
        nama: String
    ) {
        val updateDataAlternatif = DataAlternative(id, nama)
        db.child(id).setValue(updateDataAlternatif)

        Toast.makeText(this, "Alternatif Diubah", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteAlternatif() {
        val getIdAlternatif = intent.getStringExtra("idAlternatif").toString()
        db.child(getIdAlternatif).removeValue()

        Toast.makeText(this, "Faktor Dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val DATA_ALTERNATIF = "data_alternatif"
    }
}