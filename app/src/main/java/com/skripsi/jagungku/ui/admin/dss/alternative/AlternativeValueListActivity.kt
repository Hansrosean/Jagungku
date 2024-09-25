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
import com.skripsi.jagungku.adapter.AlternativeValueListAdapter
import com.skripsi.jagungku.database.dssmodel.DataFactorEvaluation
import com.skripsi.jagungku.database.dssmodel.DataRank
import com.skripsi.jagungku.databinding.ActivityAlternativeValueListBinding
import com.skripsi.jagungku.ui.admin.dss.subfactor.SubFactorListActivity

class AlternativeValueListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlternativeValueListBinding

    private lateinit var arrayFactorEvaluationList: ArrayList<DataFactorEvaluation>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlternativeValueListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDataAlternatifNilai.layoutManager = LinearLayoutManager(this)
        binding.rvDataAlternatifNilai.setHasFixedSize(true)

        arrayFactorEvaluationList = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_FAKTOR_EVALUASI)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getFactorEvaluationList()
        getAlternativeName()
        sumEvaluationAlternative()

        binding.btnAddTotalAlternativeEvaluation.setOnClickListener {
            addTotalEvaluationAlternative()
        }

        binding.btnSubFaktorListInAlternativeValueList.setOnClickListener {
            startActivity(
                Intent(
                    this@AlternativeValueListActivity,
                    SubFactorListActivity::class.java
                )
            )
        }

        val user = auth.currentUser
        if (user != null) {
            val uid = auth.currentUser?.uid
            when (uid) {
                "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                    binding.btnAddTotalAlternativeEvaluation.visibility= View.VISIBLE
                }
                "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                    binding.btnAddTotalAlternativeEvaluation.visibility = View.VISIBLE
                }
                else -> {
                    binding.btnAddTotalAlternativeEvaluation.visibility = View.GONE
                }
            }
        }
    }

    private fun getAlternativeName() {
        val getNamaAlternatif = intent.getStringExtra("namaAlternatif").toString()
        binding.tvNamaAlternatifNilai.text = getNamaAlternatif
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

                    val getNamaAlternatif = intent.getStringExtra("namaAlternatif").toString()

                    val setAdapter = AlternativeValueListAdapter(arrayFactorEvaluationList)
                    binding.rvDataAlternatifNilai.adapter = setAdapter

                    setAdapter.setOnItemClickListener(object :
                        AlternativeValueListAdapter.OnItemClickListener {
                        override fun onAddItemClick(position: Int) {
                            val user = auth.currentUser
                            if (user != null) {
                                val uid = auth.currentUser?.uid
                                if (uid != "uo12qjBvsJZSLk4cfwZ7XDKoyx43") {
                                    Toast.makeText(
                                        this@AlternativeValueListActivity,
                                        "Hanya Admin yang dapat Mengubah!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {

                                    val intent = Intent(
                                        this@AlternativeValueListActivity,
                                        AddAlternativeValueActivity::class.java
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

                                    intent.putExtra("namaAlternatif", getNamaAlternatif)

                                    startActivity(intent)
                                }
                            }
                        }
                    })
                    setAdapter.notifyDataSetChanged()
                }

                if (arrayFactorEvaluationList.isNotEmpty()) {
                    binding.rvDataAlternatifNilai.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE

                } else {
                    binding.rvDataAlternatifNilai.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun sumEvaluationAlternative() {
        val getNamaAlternatif = intent.getStringExtra("namaAlternatif").toString()

        val db2 = FirebaseDatabase.getInstance().getReference(DATA_ALTERNATIF_EVALUASI)
            .child(getNamaAlternatif)
        db2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var total = 0.0

                if (snapshot.exists()) {
                    for (dataAlternativeEvaluation in snapshot.children) {
                        val evaluation = dataAlternativeEvaluation.child("evaluasiAlternatif")
                            .getValue(Double::class.java)
                        total += evaluation!!
                    }
                    binding.tvTotalEvaluationAlternative.text = total.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addTotalEvaluationAlternative() {
        val idAlternatifRank = db.push().key!!
        val namaAlternatif = binding.tvNamaAlternatifNilai.text.toString()
        val totalEvaluasiAlternatif = binding.tvTotalEvaluationAlternative.text.toString()

        if (totalEvaluasiAlternatif.isEmpty()) {
            binding.tvTotalEvaluationAlternative.error
            Toast.makeText(this, "Masukkan evaluasi terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else if (totalEvaluasiAlternatif.isNotEmpty()) {

            val totalEvaluasiAlternatifDouble = totalEvaluasiAlternatif.toDouble()

            val totalAlternativeEvaluation = DataRank(
                idAlternatifRank,
                namaAlternatif,
                totalEvaluasiAlternatifDouble
            )

            val db3 = FirebaseDatabase.getInstance().getReference(DATA_RANKING)
            db3.child(idAlternatifRank).setValue(totalAlternativeEvaluation).addOnCompleteListener {
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

    companion object {
        const val DATA_FAKTOR_EVALUASI = "data_faktor_evaluasi"
        const val DATA_ALTERNATIF_EVALUASI = "data_alternatif_evaluasi"
        const val DATA_RANKING = "data_ranking"
    }
}