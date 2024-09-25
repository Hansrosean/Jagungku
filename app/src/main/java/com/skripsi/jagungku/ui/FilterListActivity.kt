package com.skripsi.jagungku.ui

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
import com.skripsi.jagungku.adapter.FilterListAdapter
import com.skripsi.jagungku.database.DataVarieties
import com.skripsi.jagungku.databinding.ActivityFilterListBinding
import com.skripsi.jagungku.ui.admin.varieties.DetailVarietiesActivity

class FilterListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterListBinding
    private lateinit var dataVarietasFilter: ArrayList<DataVarieties>
    private lateinit var filterListAdapter: FilterListAdapter
    private lateinit var searchView: SearchView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvDataVarietasFilter.layoutManager = LinearLayoutManager(this)
        binding.rvDataVarietasFilter.setHasFixedSize(true)

        dataVarietasFilter = arrayListOf()

        searchView = SearchView(this)
        filterListAdapter = FilterListAdapter(dataVarietasFilter)

        db = FirebaseDatabase.getInstance().getReference(DATA_VARIETAS)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        showDataVarietiesFilterList()
    }

    private fun showDataVarietiesFilterList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataVarietasFilter.clear()
                if (snapshot.exists()) {
                    for (dataVarieties in snapshot.children) {
                        val listDataVarietiesFilter = dataVarieties.getValue(DataVarieties::class.java)
                        dataVarietasFilter.add(listDataVarietiesFilter!!)
                    }

                    filterListAdapter = FilterListAdapter(dataVarietasFilter)
                    binding.rvDataVarietasFilter.adapter = filterListAdapter

//                    setAdapter.setOnItemClickListener(object : ListVarietiesAdapter.OnItemClickListener {
//                        override fun onItemClick(position: Int) {
//                            val intent = Intent(this@ListVarietiesActivity, DetailVarietiesActivity::class.java)
//
//                            intent.putExtra("idVarietas", listVarieties[position].idVarietas)
//                            intent.putExtra("namaVarietas", listVarieties[position].namaVarietas)
//                            intent.putExtra("hargaBeli", listVarieties[position].hargaBeli)
//                            intent.putExtra("potensiHasil", listVarieties[position].potensiHasil)
//                            intent.putExtra("usiaTanam", listVarieties[position].usiaTanam)
//                            intent.putExtra("ketinggianTanah", listVarieties[position].ketinggianTanah)
//                            intent.putExtra("kekebalanHama", listVarieties[position].kekebalanHama)
//                            intent.putExtra("ukuranTongkol", listVarieties[position].ukuranTongkol)
//                            intent.putExtra("suhuUdara", listVarieties[position].suhuUdara)
//                            intent.putExtra("phTanah", listVarieties[position].phTanah)
//                            startActivity(intent)
//                        }
//                    })

                    if (dataVarietasFilter.size != 0) {
                        val search = searchView.query.toString()
                        if (search.isNotEmpty()) {
                            filterListAdapter.filter.filter(search)
                        }
                    }

                    filterListAdapter.notifyDataSetChanged()
                }

                if (dataVarietasFilter.isNotEmpty()) {
                    binding.rvDataVarietasFilter.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE
                } else {
                    binding.rvDataVarietasFilter.visibility = View.GONE
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
                filterListAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterListAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val DATA_VARIETAS = "data_varietas"
    }
}