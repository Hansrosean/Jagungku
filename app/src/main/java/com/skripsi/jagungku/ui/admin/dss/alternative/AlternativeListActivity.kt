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
import com.skripsi.jagungku.adapter.AlternativeListAdapter
import com.skripsi.jagungku.database.dssmodel.DataAlternative
import com.skripsi.jagungku.databinding.ActivityAlternativeListBinding

class AlternativeListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAlternativeListBinding

    private lateinit var arrayAlternativeList: ArrayList<DataAlternative>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlternativeListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDataAlternatif.layoutManager = LinearLayoutManager(this)
        binding.rvDataAlternatif.setHasFixedSize(true)

        arrayAlternativeList = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_ALTERNATIF)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getAlternativeList()
    }

    private fun getAlternativeList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayAlternativeList.clear()
                if (snapshot.exists()) {
                    for (dataAlternative in snapshot.children) {
                        val listAlternative = dataAlternative.getValue(DataAlternative::class.java)
                        arrayAlternativeList.add(listAlternative!!)
                    }

                    val setAdapter = AlternativeListAdapter(arrayAlternativeList)
                    binding.rvDataAlternatif.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object : AlternativeListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid

                                when (uid) {
                                    "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                                        val intent = Intent(
                                            this@AlternativeListActivity,
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
                                    "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                                        val intent = Intent(
                                            this@AlternativeListActivity,
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
                                    else -> {
                                        Toast.makeText(this@AlternativeListActivity, "Hanya Admin & Kepala yang dapat Mengubah!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                        }

                        override fun onItemAddClick(position: Int) {
                            val intent = Intent(this@AlternativeListActivity, AlternativeValueListActivity::class.java)

                            intent.putExtra("idAlternatif", arrayAlternativeList[position].idAlternatif)
                            intent.putExtra("namaAlternatif", arrayAlternativeList[position].namaAlternatif)

                            startActivity(intent)
                        }
                    })
                    setAdapter.notifyDataSetChanged()
                }

                if (arrayAlternativeList.isNotEmpty()) {
                    binding.rvDataAlternatif.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE
                } else {
                    binding.rvDataAlternatif.visibility = View.GONE
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