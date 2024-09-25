package com.skripsi.jagungku.ui.admin.dss.rank

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
import com.skripsi.jagungku.database.dssmodel.DataRank
import com.skripsi.jagungku.databinding.ActivityEditRankListBinding

class EditRankListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditRankListBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRankListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseDatabase.getInstance().getReference(DATA_RANKING)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getIntentData()

        val getIdAlternatifRank = intent.getStringExtra("idAlternatifRank").toString()
        binding.btnUpdateRank.setOnClickListener {
            setUpdateRank(getIdAlternatifRank)
        }

        binding.btnDeleteRank.setOnClickListener {
            deleteRank()
        }
    }

    private fun getIntentData() {
        binding.edtUpdateNamaAlternatifInRank.setText(intent.getStringExtra("namaAlternatif"))
        binding.edtUpdateTotalEvaluasiAlternatif.setText(
            intent.getDoubleExtra(
                "totalEvaluasiAlternatif",
                0.0
            ).toString()
        )
    }

    private fun setUpdateRank(idAlternatifRank: String) {
        val updateNamaAlternatifRank = binding.edtUpdateNamaAlternatifInRank.text.toString()
        val updateTotalEvaluasiAlternatif = binding.edtUpdateTotalEvaluasiAlternatif.text.toString()

        if (updateNamaAlternatifRank.isEmpty()) {
            binding.edtUpdateNamaAlternatifInRank.error = "Data tidak boleh kosong"
        } else if (updateTotalEvaluasiAlternatif.isEmpty()) {
            binding.edtUpdateTotalEvaluasiAlternatif.error = "Data tidak boleh kosong"
        } else if (updateNamaAlternatifRank.isNotEmpty() || updateTotalEvaluasiAlternatif.isNotEmpty()) {
            val updateTotalEvaluasiAlternatifDouble = updateTotalEvaluasiAlternatif.toDouble()

            updateRank(
                idAlternatifRank,
                updateNamaAlternatifRank,
                updateTotalEvaluasiAlternatifDouble
            )
        }
    }

    private fun updateRank(id: String, nama: String, totalEvaluasi: Double) {
        val updateDataRank = DataRank(id, nama, totalEvaluasi)
        db.child(id).setValue(updateDataRank)

        Toast.makeText(this, "Data Ranking Diubah", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteRank() {
        val getIdAlternatifRank = intent.getStringExtra("idAlternatifRank").toString()
        db.child(getIdAlternatifRank).removeValue()

        Toast.makeText(this, "Data Ranking Dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val DATA_RANKING = "data_ranking"
    }
}