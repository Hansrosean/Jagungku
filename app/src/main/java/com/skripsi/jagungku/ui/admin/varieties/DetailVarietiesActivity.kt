package com.skripsi.jagungku.ui.admin.varieties

import android.content.Intent
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
import com.skripsi.jagungku.databinding.ActivityDetailVarietiesBinding
import com.skripsi.jagungku.ui.admin.varieties.EditVarietiesActivity.Companion.DATA_VARIETAS

class DetailVarietiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailVarietiesBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVarietiesBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        db = FirebaseDatabase.getInstance().getReference(DATA_VARIETAS)

        getDetailVarieties()

        binding.btnEditVarietas.setOnClickListener {
            editVarieties()
        }

        binding.btnHapusVarietas.setOnClickListener {
            hapusVarietas()
        }

        // set view for user
        val user = auth.currentUser
        if (user != null) {
            val uid = auth.currentUser?.uid
            when (uid) {
                "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                    binding.btnEditVarietas.visibility = View.VISIBLE
                    binding.btnHapusVarietas.visibility = View.VISIBLE
                }
                "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                    binding.btnEditVarietas.visibility = View.VISIBLE
                    binding.btnHapusVarietas.visibility = View.VISIBLE
                }
                else -> {
                    binding.btnEditVarietas.visibility = View.GONE
                    binding.btnHapusVarietas.visibility = View.GONE
                }
            }
        }
    }

    private fun hapusVarietas() {
        val getIdVarietas = intent.getStringExtra("idVarietas").toString()
        db.child(getIdVarietas).removeValue()

        Toast.makeText(this, "Data Dihapus", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun editVarieties() {
        val getIdVarietas = intent.getStringExtra("idVarietas")
        val getNamaVarietas = intent.getStringExtra("namaVarietas")
        val getHargaBeli = intent.getStringExtra("hargaBeli")
        val getPotensiHasil = intent.getStringExtra("potensiHasil")
        val getUsiaTanam = intent.getStringExtra("usiaTanam")
        val getKetinggianTanah = intent.getDoubleExtra("ketinggianTanah", 0.0)
        val getKekebalanHama = intent.getStringExtra("kekebalanHama")
        val getUkuranTongkol = intent.getStringExtra("ukuranTongkol")
        val getSuhuUdara = intent.getDoubleExtra("suhuUdara", 0.0)
        val getPhTanah = intent.getDoubleExtra("phTanah", 0.0)

        val intent = Intent(this@DetailVarietiesActivity, EditVarietiesActivity::class.java)

        intent.putExtra("idVarietas", getIdVarietas)
        intent.putExtra("namaVarietas", getNamaVarietas)
        intent.putExtra("hargaBeli", getHargaBeli)
        intent.putExtra("potensiHasil", getPotensiHasil)
        intent.putExtra("usiaTanam", getUsiaTanam)
        intent.putExtra("ketinggianTanah", getKetinggianTanah)
        intent.putExtra("kekebalanHama", getKekebalanHama)
        intent.putExtra("ukuranTongkol", getUkuranTongkol)
        intent.putExtra("suhuUdara", getSuhuUdara)
        intent.putExtra("phTanah", getPhTanah)
        startActivity(intent)
        finish()
    }

    private fun getDetailVarieties() {
        binding.tvDetailNamaVarietas.text = intent.getStringExtra("namaVarietas")
        binding.tvDetailHargaBeli.text = intent.getStringExtra("hargaBeli")
        binding.tvDetailPotensiHasil.text = intent.getStringExtra("potensiHasil")
        binding.tvDetailUsiaTanam.text = intent.getStringExtra("usiaTanam")
        binding.tvDetailKetinggianTanah.text = intent.getDoubleExtra("ketinggianTanah", 0.0).toString()
        binding.tvDetailKekebalanHama.text = intent.getStringExtra("kekebalanHama")
        binding.tvDetailUkuranTongkol.text = intent.getStringExtra("ukuranTongkol")
        binding.tvDetailSuhuUdara.text = intent.getDoubleExtra("suhuUdara", 0.0).toString()
        binding.tvDetailPhTanah.text = intent.getDoubleExtra("phTanah", 0.0).toString()
    }
}