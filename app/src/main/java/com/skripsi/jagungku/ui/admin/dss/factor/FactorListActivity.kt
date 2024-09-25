package com.skripsi.jagungku.ui.admin.dss.factor

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
import com.skripsi.jagungku.adapter.FactorListAdapter
import com.skripsi.jagungku.database.dssmodel.DataFactor
import com.skripsi.jagungku.databinding.ActivityFactorListBinding
import com.skripsi.jagungku.ui.admin.dss.subfactor.SubFactorListActivity

class FactorListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFactorListBinding

    private lateinit var arrayFactorList: ArrayList<DataFactor>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFactorListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDataFaktor.layoutManager = LinearLayoutManager(this)
        binding.rvDataFaktor.setHasFixedSize(true)

        arrayFactorList = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_FAKTOR)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getFactorList()

        getTotalBobotFaktor()

        binding.fabAddFaktor.setOnClickListener {
            startActivity(Intent(this, AddFactorActivity::class.java))
        }

        binding.btnSubFaktorList.setOnClickListener {
            startActivity(Intent(this, SubFactorListActivity::class.java))
        }

        // set view for user
        val user = auth.currentUser
        if (user != null) {
            val uid = auth.currentUser?.uid
            when (uid) {
                "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                    binding.fabAddFaktor.visibility = View.VISIBLE
                }
                "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                    binding.fabAddFaktor.visibility = View.VISIBLE
                }
                else -> {
                    binding.fabAddFaktor.visibility  = View.GONE
                }
            }
        }
    }

    private fun getFactorList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayFactorList.clear()
                if (snapshot.exists()) {
                    for (dataFactor in snapshot.children) {
                        val listFactor = dataFactor.getValue(DataFactor::class.java)
                        arrayFactorList.add(listFactor!!)
                    }

                    val setAdapter = FactorListAdapter(arrayFactorList)
                    binding.rvDataFaktor.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object : FactorListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid
                                if (uid != "uo12qjBvsJZSLk4cfwZ7XDKoyx43") {
                                    Toast.makeText(this@FactorListActivity, "Hanya Admin yang dapat Mengubah!", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    val intent = Intent(this@FactorListActivity, EditFactorActivity::class.java)

                                    intent.putExtra("idFaktor", arrayFactorList[position].idFaktor)
                                    intent.putExtra("kodeFaktor", arrayFactorList[position].kodeFaktor)
                                    intent.putExtra("namaFaktor", arrayFactorList[position].namaFaktor)
                                    intent.putExtra("bobotFaktor", arrayFactorList[position].bobotFaktor)

                                    startActivity(intent)
                                }
                            }
                        }
                    })
                    setAdapter.notifyDataSetChanged()
                }

                if (arrayFactorList.isNotEmpty()) {
                    binding.rvDataFaktor.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE

                } else {
                    binding.rvDataFaktor.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getTotalBobotFaktor() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var total = 0.0

                if (snapshot.exists()) {
                    for (totalBobotFaktor in snapshot.children) {
                        val evaluation = totalBobotFaktor.child("bobotFaktor")
                            .getValue(Double::class.java)
                        total += evaluation!!
                    }
                    binding.tvTotalBobot.text = total.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        const val DATA_FAKTOR ="data_faktor"
    }
}