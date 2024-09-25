package com.skripsi.jagungku.ui.admin.pricelist

import android.os.Bundle
import android.view.View
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
import com.skripsi.jagungku.database.DataPriceList
import com.skripsi.jagungku.databinding.ActivityEditPriceListBinding
import com.skripsi.jagungku.ui.admin.pricelist.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditPriceListActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener {

    private lateinit var binding: ActivityEditPriceListBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPriceListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseDatabase.getInstance().getReference(DAFTAR_HARGA)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getIntentData()

        binding.btnUpdateTanggalDitambahkan.setOnClickListener(this)

        val getIdVarietas = intent.getStringExtra("idVarietas").toString()
        binding.btnUpdateData.setOnClickListener {
            setUpdateDaftarHarga(getIdVarietas)
        }

        binding.btnDeleteData.setOnClickListener {
            deleteDaftarHarga()
        }
    }

    private fun getIntentData() {
        binding.edtUpdateNamaVarietas.setText(intent.getStringExtra("namaVarietas").toString())
        binding.edtUpdateDaftarHargaBeli.setText(intent.getStringExtra("hargaBeli").toString())
        binding.edtUpdateDaftarHargaJual.setText(intent.getStringExtra("hargaJual").toString())
        binding.tvUpdateLabelTanggalDitambahkan.text =
            intent.getStringExtra("tanggalDitambahkan").toString()
    }

    private fun setUpdateDaftarHarga(getId: String) {
        val updateNamaVarietas = binding.edtUpdateNamaVarietas.text.toString()
        val updateDaftarHargaBeli = binding.edtUpdateDaftarHargaBeli.text.toString()
        val updateDaftarHargaJual = binding.edtUpdateDaftarHargaJual.text.toString()
        val updateTanggalDitambahkan = binding.tvUpdateLabelTanggalDitambahkan.text.toString()

        if (updateNamaVarietas.isEmpty()) {
            binding.edtUpdateNamaVarietas.error = "Data tidak boleh kosong"
        } else if (updateDaftarHargaBeli.isEmpty()) {
            binding.edtUpdateDaftarHargaBeli.error = "Data tidak boleh kosong"
        } else if (updateDaftarHargaJual.isEmpty()) {
            binding.edtUpdateDaftarHargaJual.error = "Data tidak boleh kosong"
        } else if (updateTanggalDitambahkan.isEmpty()) {
            binding.tvUpdateLabelTanggalDitambahkan.error = "Data tidak boleh kosong"
        } else if (updateNamaVarietas.isNotEmpty() || updateDaftarHargaBeli.isNotEmpty() ||
            updateDaftarHargaJual.isNotEmpty() || updateTanggalDitambahkan.isNotEmpty()
        ) {
            updateDaftarHarga(
                getId,
                binding.edtUpdateNamaVarietas.text.toString(),
                binding.edtUpdateDaftarHargaBeli.text.toString(),
                binding.edtUpdateDaftarHargaJual.text.toString(),
                binding.tvUpdateLabelTanggalDitambahkan.text.toString()
            )

        }
    }

    private fun updateDaftarHarga(
        id: String,
        varietas: String,
        hargaBeli: String,
        hargaJual: String,
        tanggalDitambahkan: String
    ) {
        val daftarHarga = DataPriceList(id, varietas, hargaBeli, hargaJual, tanggalDitambahkan)
        db.child(id).setValue(daftarHarga)


        Toast.makeText(this, "Daftar Harga Diubah", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteDaftarHarga() {
        val getIdVarietas = intent.getStringExtra("idVarietas").toString()
        db.child(getIdVarietas).removeValue()

        Toast.makeText(this, "Daftar Harga Dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_update_tanggal_ditambahkan -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val myLocal = Locale("in", "ID")

        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", myLocal)

        // Set text dari textview
        binding.tvUpdateLabelTanggalDitambahkan.text = dateFormat.format(calendar.time)
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        const val DAFTAR_HARGA = "daftar_harga"
    }
}