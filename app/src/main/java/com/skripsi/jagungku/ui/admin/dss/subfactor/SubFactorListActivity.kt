package com.skripsi.jagungku.ui.admin.dss.subfactor

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
import com.skripsi.jagungku.adapter.SubFactorListAdapter
import com.skripsi.jagungku.database.dssmodel.DataSubFactor
import com.skripsi.jagungku.databinding.ActivitySubFactorListBinding
import com.skripsi.jagungku.ui.admin.dss.factor.SecondFactorListActivity

class SubFactorListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubFactorListBinding

    private lateinit var arraySubFactorList: ArrayList<DataSubFactor>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubFactorListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDataSubFaktor.layoutManager = LinearLayoutManager(this)
        binding.rvDataSubFaktor.setHasFixedSize(true)

        arraySubFactorList = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_SUB_FAKTOR)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getSubFactorList()

        binding.btnAddSubFaktor.setOnClickListener {
            startActivity(Intent(this, SecondFactorListActivity::class.java))
        }

        val user = auth.currentUser
        if (user != null) {
            val uid = auth.currentUser?.uid
            when (uid) {
                "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                    binding.btnAddSubFaktor.visibility = View.VISIBLE
                }
                "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                    binding.btnAddSubFaktor.visibility = View.VISIBLE
                }
                else -> {
                    binding.btnAddSubFaktor.visibility = View.GONE
                }
            }
        }
    }

    private fun getSubFactorList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arraySubFactorList.clear()
                if (snapshot.exists()) {
                    for (dataSubFactor in snapshot.children) {
                        val listSubFactor = dataSubFactor.getValue(DataSubFactor::class.java)
                        arraySubFactorList.add(listSubFactor!!)
                    }

                    val setAdapter = SubFactorListAdapter(arraySubFactorList)
                    binding.rvDataSubFaktor.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object :
                        SubFactorListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid
                                if (uid != "uo12qjBvsJZSLk4cfwZ7XDKoyx43") {
                                    Toast.makeText(
                                        this@SubFactorListActivity,
                                        "Hanya Admin yang dapat Mengubah!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {

                                    val intent = Intent(
                                        this@SubFactorListActivity,
                                        EditSubFactorActivity::class.java
                                    )

                                    intent.putExtra(
                                        "idSubFaktor",
                                        arraySubFactorList[position].idSubFaktor
                                    )
                                    intent.putExtra(
                                        "namaFaktor",
                                        arraySubFactorList[position].namaFaktor
                                    )
                                    intent.putExtra(
                                        "subFaktor1",
                                        arraySubFactorList[position].subFaktor1
                                    )
                                    intent.putExtra(
                                        "subFaktor2",
                                        arraySubFactorList[position].subFaktor2
                                    )
                                    intent.putExtra(
                                        "subFaktor3",
                                        arraySubFactorList[position].subFaktor3
                                    )
                                    intent.putExtra(
                                        "nilaiSubFaktor1",
                                        arraySubFactorList[position].nilaiSubFaktor1
                                    )
                                    intent.putExtra(
                                        "nilaiSubFaktor2",
                                        arraySubFactorList[position].nilaiSubFaktor2
                                    )
                                    intent.putExtra(
                                        "nilaiSubFaktor3",
                                        arraySubFactorList[position].nilaiSubFaktor3
                                    )

                                    startActivity(intent)
                                }
                            }
                        }
                    })
                    setAdapter.notifyDataSetChanged()
                }

                if (arraySubFactorList.isNotEmpty()) {
                    binding.rvDataSubFaktor.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE

                } else {
                    binding.rvDataSubFaktor.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        const val DATA_SUB_FAKTOR = "data_sub_faktor"
    }
}