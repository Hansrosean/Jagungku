package com.skripsi.jagungku.ui.admin.dss.evaluation.factorevaluation

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
import com.skripsi.jagungku.adapter.FactorListEvaluationAdapter
import com.skripsi.jagungku.database.dssmodel.DataFactorEvaluation
import com.skripsi.jagungku.databinding.ActivityFactorEvaluationListBinding
import com.skripsi.jagungku.ui.admin.dss.factor.ThirdFactorListActivity

class FactorEvaluationListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFactorEvaluationListBinding

    private lateinit var arrayFactorEvaluationList: ArrayList<DataFactorEvaluation>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFactorEvaluationListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvFactorEvaluationList.layoutManager = LinearLayoutManager(this)
        binding.rvFactorEvaluationList.setHasFixedSize(true)

        arrayFactorEvaluationList = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_FAKTOR_EVALUASI)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getFactorEvaluationList()

        binding.btnThirdFactorList.setOnClickListener {
            startActivity(
                Intent(
                    this@FactorEvaluationListActivity,
                    ThirdFactorListActivity::class.java
                )
            )
        }
    }

    private fun getFactorEvaluationList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayFactorEvaluationList.clear()
                if (snapshot.exists()) {
                    for (dataFactor in snapshot.children) {
                        val listFactorEvaluation =
                            dataFactor.getValue(DataFactorEvaluation::class.java)
                        arrayFactorEvaluationList.add(listFactorEvaluation!!)
                    }

                    val setAdapter = FactorListEvaluationAdapter(arrayFactorEvaluationList)
                    binding.rvFactorEvaluationList.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object :
                        FactorListEvaluationAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid

                                when (uid) {
                                    "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                                        val intent = Intent(
                                            this@FactorEvaluationListActivity,
                                            EditFactorEvaluationActivity::class.java
                                        )

                                        intent.putExtra(
                                            "idFaktorEvaluasi",
                                            arrayFactorEvaluationList[position].idFaktorEvaluasi
                                        )
                                        intent.putExtra(
                                            "namaFaktor",
                                            arrayFactorEvaluationList[position].namaFaktor
                                        )
                                        intent.putExtra(
                                            "bobotEvaluasiFaktor",
                                            arrayFactorEvaluationList[position].bobotEvaluasiFaktor
                                        )

                                        startActivity(intent)
                                    }

                                    "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                                        val intent = Intent(
                                            this@FactorEvaluationListActivity,
                                            EditFactorEvaluationActivity::class.java
                                        )

                                        intent.putExtra(
                                            "idFaktorEvaluasi",
                                            arrayFactorEvaluationList[position].idFaktorEvaluasi
                                        )
                                        intent.putExtra(
                                            "namaFaktor",
                                            arrayFactorEvaluationList[position].namaFaktor
                                        )
                                        intent.putExtra(
                                            "bobotEvaluasiFaktor",
                                            arrayFactorEvaluationList[position].bobotEvaluasiFaktor
                                        )

                                        startActivity(intent)
                                    }

                                    else -> {
                                        Toast.makeText(
                                            this@FactorEvaluationListActivity,
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

                if (arrayFactorEvaluationList.isNotEmpty()) {
                    binding.rvFactorEvaluationList.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE

                } else {
                    binding.rvFactorEvaluationList.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        const val DATA_FAKTOR_EVALUASI = "data_faktor_evaluasi"
    }
}