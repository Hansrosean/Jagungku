package com.skripsi.jagungku.ui.admin.dss.alternative

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
import com.skripsi.jagungku.adapter.AlternativeNameListAdapter
import com.skripsi.jagungku.database.dssmodel.DataAlternative
import com.skripsi.jagungku.databinding.ActivityAlternativeNameListBinding

class AlternativeNameListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlternativeNameListBinding

    private lateinit var arrayAlternativeList: ArrayList<DataAlternative>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlternativeNameListBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDataAlternatifInAlternatifNama.layoutManager = LinearLayoutManager(this)
        binding.rvDataAlternatifInAlternatifNama.setHasFixedSize(true)

        arrayAlternativeList = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_ALTERNATIF)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getAlternativeListName()

        binding.btnAddAlternatifInAlternatifNama.setOnClickListener {
            startActivity(Intent(this, AddAlternativeActivity::class.java))
        }

        val user = auth.currentUser
        if (user != null) {
            val uid = auth.currentUser?.uid
            when (uid) {
                "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                    binding.btnAddAlternatifInAlternatifNama.visibility = View.VISIBLE
                }
                "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                    binding.btnAddAlternatifInAlternatifNama.visibility = View.VISIBLE
                }
                else -> {
                    binding.btnAddAlternatifInAlternatifNama.visibility = View.GONE
                }
            }
        }
    }

    private fun getAlternativeListName() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayAlternativeList.clear()
                if (snapshot.exists()) {
                    for (dataAlternative in snapshot.children) {
                        val listAlternative = dataAlternative.getValue(DataAlternative::class.java)
                        arrayAlternativeList.add(listAlternative!!)
                    }

                    val setAdapter = AlternativeNameListAdapter(arrayAlternativeList)
                    binding.rvDataAlternatifInAlternatifNama.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object :
                        AlternativeNameListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid
                                if (uid != "uo12qjBvsJZSLk4cfwZ7XDKoyx43") {
                                    Toast.makeText(
                                        this@AlternativeNameListActivity,
                                        "Hanya Admin yang dapat Mengubah!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    val intent = Intent(
                                        this@AlternativeNameListActivity,
                                        EditAlternativeActivity::class.java
                                    )

                                    intent.putExtra(
                                        "idAlternatif",
                                        arrayAlternativeList[position].idAlternatif
                                    )
                                    intent.putExtra(
                                        "namaAlternatif",
                                        arrayAlternativeList[position].namaAlternatif
                                    )

                                    startActivity(intent)
                                }
                            }
                        }
                    })
                    setAdapter.notifyDataSetChanged()
                }

                if (arrayAlternativeList.isNotEmpty()) {
                    binding.rvDataAlternatifInAlternatifNama.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE
                } else {
                    binding.rvDataAlternatifInAlternatifNama.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        const val DATA_ALTERNATIF = "data_alternatif"
    }
}