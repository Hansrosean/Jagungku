package com.skripsi.jagungku.ui.admin.pricelist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.skripsi.jagungku.R
import com.skripsi.jagungku.adapter.PriceListAdapter
import com.skripsi.jagungku.database.DataPriceList
import com.skripsi.jagungku.databinding.ActivityPriceListBinding
import com.skripsi.jagungku.ui.admin.dss.rank.EditRankListActivity

class PriceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPriceListBinding

    private lateinit var arrayPriceList: ArrayList<DataPriceList>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPriceListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDaftarHarga.layoutManager = LinearLayoutManager(this)
        binding.rvDaftarHarga.setHasFixedSize(true)

        arrayPriceList = arrayListOf()
        db = FirebaseDatabase.getInstance().getReference(DAFTAR_HARGA)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getListPriceList()

        binding.fabAddDaftarHarga.setOnClickListener {
            addPriceList()
        }

        val user = auth.currentUser
        if (user != null) {
            val uid = auth.currentUser?.uid
            when (uid) {
                "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                    binding.fabAddDaftarHarga.visibility = View.VISIBLE
                }
                "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                    binding.fabAddDaftarHarga.visibility = View.VISIBLE
                }
                else -> {
                    binding.fabAddDaftarHarga.visibility = View.GONE
                }
            }
        }
    }

    private fun addPriceList() {
        startActivity(Intent(this, AddPriceListActivity::class.java))
    }

    private fun getListPriceList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayPriceList.clear()
                if (snapshot.exists()) {
                    for (priceList in snapshot.children) {
                        val dataPriceList = priceList.getValue(DataPriceList::class.java)
                        arrayPriceList.add(dataPriceList!!)
                    }

                    val setAdapter = PriceListAdapter(arrayPriceList)
                    binding.rvDaftarHarga.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object :
                        PriceListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid
                                if (uid != "uo12qjBvsJZSLk4cfwZ7XDKoyx43") {
                                    Toast.makeText(
                                        this@PriceListActivity,
                                        "Hanya Admin yang dapat Mengubah!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    val intent = Intent(
                                        this@PriceListActivity,
                                        EditPriceListActivity::class.java
                                    )

                                    intent.putExtra(
                                        "idVarietas",
                                        arrayPriceList[position].idVarietas
                                    )
                                    intent.putExtra(
                                        "namaVarietas",
                                        arrayPriceList[position].namaVarietas
                                    )
                                    intent.putExtra("hargaBeli", arrayPriceList[position].hargaBeli)
                                    intent.putExtra("hargaJual", arrayPriceList[position].hargaJual)
                                    intent.putExtra(
                                        "tanggalDitambahkan",
                                        arrayPriceList[position].tanggalDitambahkan
                                    )

                                    startActivity(intent)
                                }
                            }
                        }

                        override fun onEditItem(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid

                                when (uid) {
                                    "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                                        val intent =
                                            Intent(
                                                this@PriceListActivity,
                                                EditPriceListActivity::class.java
                                            )

                                        intent.putExtra(
                                            "idVarietas",
                                            arrayPriceList[position].idVarietas
                                        )
                                        intent.putExtra(
                                            "namaVarietas",
                                            arrayPriceList[position].namaVarietas
                                        )
                                        intent.putExtra("hargaBeli", arrayPriceList[position].hargaBeli)
                                        intent.putExtra("hargaJual", arrayPriceList[position].hargaJual)
                                        intent.putExtra(
                                            "tanggalDitambahkan",
                                            arrayPriceList[position].tanggalDitambahkan
                                        )

                                        startActivity(intent)
                                    }

                                    "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                                        val intent =
                                            Intent(
                                                this@PriceListActivity,
                                                EditPriceListActivity::class.java
                                            )

                                        intent.putExtra(
                                            "idVarietas",
                                            arrayPriceList[position].idVarietas
                                        )
                                        intent.putExtra(
                                            "namaVarietas",
                                            arrayPriceList[position].namaVarietas
                                        )
                                        intent.putExtra("hargaBeli", arrayPriceList[position].hargaBeli)
                                        intent.putExtra("hargaJual", arrayPriceList[position].hargaJual)
                                        intent.putExtra(
                                            "tanggalDitambahkan",
                                            arrayPriceList[position].tanggalDitambahkan
                                        )

                                        startActivity(intent)
                                    }

                                    else -> {
                                        Toast.makeText(
                                            this@PriceListActivity,
                                            "Hanya Admin yang dapat Mengubah!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        }
                    })
                    setAdapter.notifyDataSetChanged()
                }
                if (arrayPriceList.isNotEmpty()) {
                    binding.rvDaftarHarga.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE
                } else {
                    binding.rvDaftarHarga.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    companion object {
        const val DAFTAR_HARGA = "daftar_harga"
    }
}