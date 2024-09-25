package com.skripsi.jagungku.ui.admin.dss.factor

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.skripsi.jagungku.adapter.SecondFactorListAdapter
import com.skripsi.jagungku.database.dssmodel.DataFactor
import com.skripsi.jagungku.databinding.ActivitySecondFactorListBinding
import com.skripsi.jagungku.ui.admin.dss.subfactor.AddSubFactorActivity

class SecondFactorListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondFactorListBinding

    private lateinit var arrayFactorList: ArrayList<DataFactor>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondFactorListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvSecondDataFaktor.layoutManager = LinearLayoutManager(this)
        binding.rvSecondDataFaktor.setHasFixedSize(true)

        arrayFactorList = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_FAKTOR)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getSecondDataFactorList()
    }

    private fun getSecondDataFactorList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayFactorList.clear()
                if (snapshot.exists()) {
                    for (dataFactor in snapshot.children) {
                        val listFactor = dataFactor.getValue(DataFactor::class.java)
                        arrayFactorList.add(listFactor!!)
                    }

                    val setAdapter = SecondFactorListAdapter(arrayFactorList)
                    binding.rvSecondDataFaktor.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object : SecondFactorListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@SecondFactorListActivity, AddSubFactorActivity::class.java)

                            intent.putExtra("idFaktor", arrayFactorList[position].idFaktor)
                            intent.putExtra("namaFaktor", arrayFactorList[position].namaFaktor)

                            startActivity(intent)
                        }
                    })
                    setAdapter.notifyDataSetChanged()
                }

                if (arrayFactorList.isNotEmpty()) {
                    binding.rvSecondDataFaktor.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE

                } else {
                    binding.rvSecondDataFaktor.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        const val DATA_FAKTOR = "data_faktor"
    }
}