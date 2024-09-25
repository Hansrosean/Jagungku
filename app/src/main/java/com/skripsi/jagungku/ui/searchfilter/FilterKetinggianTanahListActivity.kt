package com.skripsi.jagungku.ui.searchfilter

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import com.skripsi.jagungku.adapter.FilterKetinggianTanahAdapter
import com.skripsi.jagungku.adapter.FilterListAdapter
import com.skripsi.jagungku.database.DataVarieties
import com.skripsi.jagungku.databinding.ActivityFilterKetinggianTanahListBinding
import com.skripsi.jagungku.ui.FilterListActivity
import com.skripsi.jagungku.ui.FilterListActivity.Companion
import com.skripsi.jagungku.ui.admin.varieties.DetailVarietiesActivity

class FilterKetinggianTanahListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterKetinggianTanahListBinding
    private lateinit var ketinggianTanahArrayList: ArrayList<DataVarieties>
    private lateinit var filterKetinggianTanahAdapter: FilterKetinggianTanahAdapter
    private lateinit var searchView: SearchView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFilterKetinggianTanahListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvFilterKetinggianTanah.layoutManager = LinearLayoutManager(this)
        binding.rvFilterKetinggianTanah.setHasFixedSize(true)

        ketinggianTanahArrayList = arrayListOf()

        searchView = SearchView(this)

        filterKetinggianTanahAdapter = FilterKetinggianTanahAdapter(ketinggianTanahArrayList)

        db = FirebaseDatabase.getInstance().getReference(DATA_VARIETAS)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        showDataKetinggianTanahFilterList()
    }

    private fun showDataKetinggianTanahFilterList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ketinggianTanahArrayList.clear()
                if (snapshot.exists()) {
                    for (dataVarieties in snapshot.children) {
                        val listDataKetinggianTanahFilter = dataVarieties.getValue(DataVarieties::class.java)
                        ketinggianTanahArrayList.add(listDataKetinggianTanahFilter!!)
                    }

                    filterKetinggianTanahAdapter = FilterKetinggianTanahAdapter(ketinggianTanahArrayList)
                    binding.rvFilterKetinggianTanah.adapter = filterKetinggianTanahAdapter

//        db.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                ketinggianTanahArrayList.clear()
//                if (snapshot.exists()) {
//                    for (dataVarieties in snapshot.children) {
//                        val listDataKetinggianTanahFilter = dataVarieties.getValue(DataVarieties::class.java)
//                        ketinggianTanahArrayList.add(listDataKetinggianTanahFilter!!)
//                    }
//
//                    filterKetinggianTanahAdapter = FilterKetinggianTanahAdapter(ketinggianTanahArrayList)
//                    binding.rvFilterKetinggianTanah.adapter = filterKetinggianTanahAdapter

                    filterKetinggianTanahAdapter.setOnItemClickListener(object : FilterKetinggianTanahAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FilterKetinggianTanahListActivity, DetailVarietiesActivity::class.java)

                            intent.putExtra("idVarietas", ketinggianTanahArrayList[position].idVarietas)
                            intent.putExtra("namaVarietas", ketinggianTanahArrayList[position].namaVarietas)
                            intent.putExtra("hargaBeli", ketinggianTanahArrayList[position].hargaBeli)
                            intent.putExtra("potensiHasil", ketinggianTanahArrayList[position].potensiHasil)
                            intent.putExtra("usiaTanam", ketinggianTanahArrayList[position].usiaTanam)
                            intent.putExtra("ketinggianTanah", ketinggianTanahArrayList[position].ketinggianTanah)
                            intent.putExtra("kekebalanHama", ketinggianTanahArrayList[position].kekebalanHama)
                            intent.putExtra("ukuranTongkol", ketinggianTanahArrayList[position].ukuranTongkol)
                            intent.putExtra("suhuUdara", ketinggianTanahArrayList[position].suhuUdara)
                            intent.putExtra("phTanah", ketinggianTanahArrayList[position].phTanah)
                            startActivity(intent)
                        }
                    })

                    if (ketinggianTanahArrayList.size != 0) {
                        val search = searchView.query.toString()
                        if (search.isNotEmpty()) {
                            filterKetinggianTanahAdapter.filter.filter(search)
                        }
                    }

                    filterKetinggianTanahAdapter.notifyDataSetChanged()
                }

                if (ketinggianTanahArrayList.isNotEmpty()) {
                    binding.rvFilterKetinggianTanah.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE
                } else {
                    binding.rvFilterKetinggianTanah.visibility = View.GONE
                    binding.tvDataNotFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val menuItem : MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView? = menuItem!!.actionView as SearchView?

        searchView?.queryHint = "Cari varietas..."
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterKetinggianTanahAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterKetinggianTanahAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val DATA_VARIETAS = "data_varietas"
    }
}