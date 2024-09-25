package com.skripsi.jagungku.ui.admin.dss.rank

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
import com.skripsi.jagungku.adapter.RankListAdapter
import com.skripsi.jagungku.database.dssmodel.DataRank
import com.skripsi.jagungku.databinding.ActivityRankListBinding

class RankListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankListBinding

    private lateinit var rankList: ArrayList<DataRank>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDataRank.layoutManager = LinearLayoutManager(this)
        binding.rvDataRank.setHasFixedSize(true)

        rankList = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_RANKING)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getRankList()
    }

    private fun getRankList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rankList.clear()
                if (snapshot.exists()) {
                    for (dataRank in snapshot.children) {
                        val dataListRank = dataRank.getValue(DataRank::class.java)
                        rankList.add(dataListRank!!)
                    }

                    rankList.sortByDescending { it.totalEvaluasiAlternatif }

                    val setAdapter = RankListAdapter(rankList)
                    binding.rvDataRank.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object : RankListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid

                                when (uid) {
                                    "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                                        val intent = Intent(this@RankListActivity, EditRankListActivity::class.java)

                                        intent.putExtra("idAlternatifRank", rankList[position].idAlternatifRank)
                                        intent.putExtra("namaAlternatif", rankList[position].namaAlternatif)
                                        intent.putExtra("totalEvaluasiAlternatif", rankList[position].totalEvaluasiAlternatif)

                                        startActivity(intent)
                                    }
                                    "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                                        val intent = Intent(this@RankListActivity, EditRankListActivity::class.java)

                                        intent.putExtra("idAlternatifRank", rankList[position].idAlternatifRank)
                                        intent.putExtra("namaAlternatif", rankList[position].namaAlternatif)
                                        intent.putExtra("totalEvaluasiAlternatif", rankList[position].totalEvaluasiAlternatif)

                                        startActivity(intent)
                                    }
                                    else -> {
                                        Toast.makeText(this@RankListActivity, "Hanya Admin yang dapat Mengubah!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                        }
                    })

                    setAdapter.notifyDataSetChanged()
                }

                if (rankList.isNotEmpty()) {
                    binding.rvDataRank.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE

                } else {
                    binding.rvDataRank.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    companion object {
        const val DATA_RANKING = "data_ranking"
    }
}