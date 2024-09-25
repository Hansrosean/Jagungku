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
import com.skripsi.jagungku.databinding.ActivityEditFactorBinding

class EditFactorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFactorBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFactorBinding.inflate(layoutInflater)

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

        getIntentData()

        val getIdFaktor = intent.getStringExtra("idFaktor").toString()
        binding.btnUpdateFaktor.setOnClickListener {
            setUpdateFaktor(getIdFaktor)
        }

        binding.btnDeleteFaktor.setOnClickListener {
            deleteFaktor()
        }
    }

    private fun getIntentData() {
        binding.edtUpdateKodeFaktor.setText(intent.getStringExtra("kodeFaktor").toString())
        binding.edtUpdateNamaFaktor.setText(intent.getStringExtra("namaFaktor").toString())
        binding.edtUpdateBobotFaktor.setText(intent.getDoubleExtra("bobotFaktor", 0.0).toString())
    }

    private fun setUpdateFaktor(idFaktor: String) {
        val updateKodeFaktor = binding.edtUpdateKodeFaktor.text.toString()
        val updateNamaFaktor = binding.edtUpdateNamaFaktor.text.toString()
        val updateBobotFaktor = binding.edtUpdateBobotFaktor.text.toString()

        if (updateKodeFaktor.isEmpty()) {
            binding.edtUpdateKodeFaktor.error = "Data tidak boleh kosong"
        } else if (updateNamaFaktor.isEmpty()) {
            binding.edtUpdateNamaFaktor.error = "Data tidak boleh kosong"
        } else if (updateBobotFaktor.isEmpty()) {
            binding.edtUpdateBobotFaktor.error = "Data tidak boleh kosong"
        } else if (updateKodeFaktor.isNotEmpty() || updateNamaFaktor.isNotEmpty() || updateBobotFaktor.isNotEmpty()) {

            val updateBobotFaktorDouble = updateBobotFaktor.toDouble()

            updateFaktor(
                idFaktor,
                updateKodeFaktor,
                updateNamaFaktor,
                updateBobotFaktorDouble
            )
        }
    }

    private fun updateFaktor(
        id: String,
        faktor: String,
        nama: String,
        bobot: Double,
    ) {
        val updateDataFaktor = DataFactor(id, faktor, nama, bobot)
        db.child(id).setValue(updateDataFaktor)

        Toast.makeText(this, "Faktor Diubah", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteFaktor() {
        val getIdFaktor = intent.getStringExtra("idFaktor").toString()
        db.child(getIdFaktor).removeValue()

        Toast.makeText(this, "Faktor Dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val DATA_FAKTOR = "data_faktor"
    }
}