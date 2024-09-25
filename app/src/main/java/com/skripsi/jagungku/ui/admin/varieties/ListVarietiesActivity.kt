package com.skripsi.jagungku.ui.admin.varieties

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.skripsi.jagungku.R
import com.skripsi.jagungku.adapter.ListVarietiesAdapter
import com.skripsi.jagungku.database.DataVarieties
import com.skripsi.jagungku.databinding.ActivityListVarietiesBinding

class ListVarietiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListVarietiesBinding

    private lateinit var listVarieties: ArrayList<DataVarieties>

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListVarietiesBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDataVarietas.layoutManager = LinearLayoutManager(this)
        binding.rvDataVarietas.setHasFixedSize(true)

        listVarieties = arrayListOf()

        db = FirebaseDatabase.getInstance().getReference(DATA_VARIETAS)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        getListDataVarieties()

        binding.fabAddVarieties.setOnClickListener {
            addVarieties()
        }

        // set view for user
        val user = auth.currentUser
        if (user != null) {
            val uid = auth.currentUser?.uid
            when (uid) {
                "uo12qjBvsJZSLk4cfwZ7XDKoyx43" -> {
                    binding.fabAddVarieties.visibility = View.VISIBLE
                }
                "iT0EPm7zOaZSgYzDLo6STitaChn1" -> {
                    binding.fabAddVarieties.visibility = View.VISIBLE
                }
                else -> {
                    binding.fabAddVarieties.visibility = View.GONE
                }
            }
        }
    }

    private fun addVarieties() {
        startActivity(Intent(this, AddVarietiesActivity::class.java))
    }

    private fun getListDataVarieties() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listVarieties.clear()
                if (snapshot.exists()) {
                    for (dataVarieties in snapshot.children) {
                        val listNameVarieties = dataVarieties.getValue(DataVarieties::class.java)
                        listVarieties.add(listNameVarieties!!)
                    }

                    // menggunakan recyclerview firebase ui untuk data realtime
                    val realtimeData = FirebaseRecyclerOptions.Builder<DataVarieties>()
                        .setQuery(db, DataVarieties::class.java)
                        .build()

                    val adapter = ListVarietiesAdapter(realtimeData)

//                    val setAdapter = ListVarietiesAdapter(listVarieties)
                    binding.rvDataVarietas.adapter = adapter
                    adapter.startListening()

                    adapter.setOnItemClickListener(object : ListVarietiesAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@ListVarietiesActivity, DetailVarietiesActivity::class.java)

                            intent.putExtra("idVarietas", listVarieties[position].idVarietas)
                            intent.putExtra("namaVarietas", listVarieties[position].namaVarietas)
                            intent.putExtra("hargaBeli", listVarieties[position].hargaBeli)
                            intent.putExtra("potensiHasil", listVarieties[position].potensiHasil)
                            intent.putExtra("usiaTanam", listVarieties[position].usiaTanam)
                            intent.putExtra("ketinggianTanah", listVarieties[position].ketinggianTanah)
                            intent.putExtra("kekebalanHama", listVarieties[position].kekebalanHama)
                            intent.putExtra("ukuranTongkol", listVarieties[position].ukuranTongkol)
                            intent.putExtra("suhuUdara", listVarieties[position].suhuUdara)
                            intent.putExtra("phTanah", listVarieties[position].phTanah)
                            startActivity(intent)
                        }
                    })
                    adapter.notifyDataSetChanged()
                }

                if (listVarieties.isNotEmpty()) {
                    binding.rvDataVarietas.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE

                } else {
                    binding.rvDataVarietas.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

//    private fun deleteVarieties() {
//        val getUid = Firebase.auth.uid!!
//        val toast = Toast.makeText(this, "Data Dihapus", Toast.LENGTH_SHORT)
//
//        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                db.child(getUid).removeValue()
//                toast.show()
//            }
//        }
//
//        val swipeHelper = ItemTouchHelper(itemTouchHelper)
//        swipeHelper.attachToRecyclerView(mRecyclerView)
//    }

    companion object {
        const val DATA_VARIETAS = "data_varietas"
    }
}