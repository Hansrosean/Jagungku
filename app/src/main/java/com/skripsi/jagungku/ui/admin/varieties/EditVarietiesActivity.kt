package com.skripsi.jagungku.ui.admin.varieties

import android.content.Intent
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
import com.skripsi.jagungku.databinding.ActivityEditVarietiesBinding

class EditVarietiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditVarietiesBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditVarietiesBinding.inflate(layoutInflater)

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

        getIntentData()

        val getIdVarietas = intent.getStringExtra("idVarietas").toString()
        binding.btnUpdateData.setOnClickListener {
            setUpdateData(getIdVarietas)
        }

        binding.btnDeleteData.setOnClickListener {
            deleteData()
        }
    }

    private fun getIntentData() {
        binding.edtUpdateNamaVarietas.setText(intent.getStringExtra("namaVarietas").toString())
        binding.edtUpdateHargaBeli.setText(intent.getStringExtra("hargaBeli").toString())
        binding.edtUpdatePotensiHasil.setText(intent.getStringExtra("potensiHasil").toString())
        binding.edtUpdateUsiaTanam.setText(intent.getStringExtra("usiaTanam").toString())
        binding.edtUpdateKetinggianTanah.setText(
            intent.getStringExtra("ketinggianTanah").toString()
        )
        binding.edtUpdateKekebalanHama.setText(intent.getStringExtra("kekebalanHama").toString())
        binding.edtUpdateUkuranTongkol.setText(intent.getStringExtra("ukuranTongkol").toString())
        binding.edtUpdateSuhuUdara.setText(intent.getStringExtra("suhuUdara").toString())
        binding.edtUpdatePhTanah.setText(intent.getStringExtra("phTanah").toString())
    }

    private fun setUpdateData(getId: String) {
        val updateNamaVarietas = binding.edtUpdateNamaVarietas.text.toString()
        val updateHargaBeli = binding.edtUpdateHargaBeli.text.toString()
        val updatePotensiHasil = binding.edtUpdatePotensiHasil.text.toString()
        val updateUsiaTanam = binding.edtUpdateUsiaTanam.text.toString()
        val updateKetinggianTanah = binding.edtUpdateKetinggianTanah.text.toString()
        val updateKekebalanHama = binding.edtUpdateKekebalanHama.text.toString()
        val updateUkuranTongkol = binding.edtUpdateUkuranTongkol.text.toString()
        val updateSuhuUdara = binding.edtUpdateSuhuUdara.text.toString()
        val updatePhTanah = binding.edtUpdatePhTanah.text.toString()

        if (updateNamaVarietas.isEmpty()) {
            binding.edtUpdateNamaVarietas.error = "Data tidak boleh kosong"
        } else if (updateHargaBeli.isEmpty()) {
            binding.edtUpdateHargaBeli.error = "Data tidak boleh kosong"
        } else if (updatePotensiHasil.isEmpty()) {
            binding.edtUpdatePotensiHasil.error = "Data tidak boleh kosong"
        } else if (updateUsiaTanam.isEmpty()) {
            binding.edtUpdateUsiaTanam.error = "Data tidak boleh kosong"
        } else if (updateKetinggianTanah.isEmpty()) {
            binding.edtUpdateKetinggianTanah.error = "Data tidak boleh kosong"
        } else if (updateKekebalanHama.isEmpty()) {
            binding.edtUpdateKekebalanHama.error = "Data tidak boleh kosong"
        } else if (updateUkuranTongkol.isEmpty()) {
            binding.edtUpdateUkuranTongkol.error = "Data tidak boleh kosong"
        } else if (updateSuhuUdara.isEmpty()) {
            binding.edtUpdateSuhuUdara.error = "Data tidak boleh kosong"
        } else if (updatePhTanah.isEmpty()) {
            binding.edtUpdatePhTanah.error = "Data tidak boleh kosong"
        } else if (updateNamaVarietas.isNotEmpty() || updateHargaBeli.isNotEmpty() || updatePotensiHasil.isNotEmpty() ||
            updateUsiaTanam.isNotEmpty() || updateKetinggianTanah.isNotEmpty() || updateKekebalanHama.isNotEmpty() ||
            updateUkuranTongkol.isNotEmpty() || updateSuhuUdara.isNotEmpty() || updatePhTanah.isNotEmpty()
        ) {
            val updateKetinggianTanahDouble = updateKetinggianTanah.toDouble()
            val updateSuhuUdaraDoublehDouble = updateSuhuUdara.toDouble()
            val updateupdatePhTanah = updatePhTanah.toDouble()

            updateVarieties(
                getId,
                updateNamaVarietas,
                updateHargaBeli,
                updatePotensiHasil,
                updateUsiaTanam,
                updateKetinggianTanahDouble,
                updateKekebalanHama,
                updateUkuranTongkol,
                updateSuhuUdaraDoublehDouble,
                updateupdatePhTanah
            )
        }
    }

    private fun updateVarieties(
        id: String,
        varietas: String,
        harga: String,
        potensi: String,
        usia: String,
        ketinggian: Double,
        kekebalan: String,
        ukuran: String,
        suhu: Double,
        phTanah: Double
    ) {
        val dataVarietas = DataVarieties(
            id,
            varietas,
            harga,
            potensi,
            usia,
            ketinggian,
            kekebalan,
            ukuran,
            suhu,
            phTanah
        )
        db.child(id).setValue(dataVarietas)

        Toast.makeText(this, "Data Diubah", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteData() {
        val getIdVarietas = intent.getStringExtra("idVarietas").toString()
        db.child(getIdVarietas).removeValue()

        Toast.makeText(this, "Data Dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val DATA_VARIETAS = "data_varietas"
    }
}