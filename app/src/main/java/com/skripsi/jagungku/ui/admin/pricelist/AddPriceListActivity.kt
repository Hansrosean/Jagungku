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
import com.skripsi.jagungku.databinding.ActivityAddPriceListBinding
import com.skripsi.jagungku.ui.admin.pricelist.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddPriceListActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener {

    private lateinit var binding: ActivityAddPriceListBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPriceListBinding.inflate(layoutInflater)

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

        binding.btnAddPriceList.setOnClickListener {
            addPriceList()
        }

        binding.btnTanggalDitambahkan.setOnClickListener(this)
    }

    private fun addPriceList() {
        val idVarieties = db.push().key!!
        val namaVarietas = binding.edtNamaVarietas.text.toString()
        val hargaBeli = binding.edtDaftarHargaBeli.text.toString()
        val hargaJual = binding.edtDaftarHargaJual.text.toString()
        val tanggalDitambahkan = binding.tvLabelTanggalDitambahkan.text.toString()

        if (namaVarietas.isEmpty()) {
            binding.edtNamaVarietas.error = "Data tidak boleh kosong"
        } else if (hargaBeli.isEmpty()) {
            binding.edtDaftarHargaBeli.error = "Data tidak boleh kosong"
        } else if (hargaJual.isEmpty()) {
            binding.edtDaftarHargaJual.error = "Data tidak boleh kosong"
        } else if (tanggalDitambahkan.isEmpty()) {
            binding.tvLabelTanggalDitambahkan.error = "Data tidak boleh kosong"
        } else if (namaVarietas.isNotEmpty() || hargaBeli.isNotEmpty() || hargaJual.isNotEmpty() ||
            tanggalDitambahkan.isNotEmpty()
        ) {
            val dataPriceList = DataPriceList(
                idVarieties,
                namaVarietas,
                hargaBeli,
                hargaJual,
                tanggalDitambahkan
            )

            db.child(idVarieties).setValue(dataPriceList).addOnCompleteListener {
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

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_tanggal_ditambahkan -> {
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
        binding.tvLabelTanggalDitambahkan.text = dateFormat.format(calendar.time)
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        const val DAFTAR_HARGA = "daftar_harga"
    }
}