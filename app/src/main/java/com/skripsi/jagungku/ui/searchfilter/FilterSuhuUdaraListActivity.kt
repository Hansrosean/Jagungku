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
import com.skripsi.jagungku.adapter.FilterSuhuUdaraAdapter
import com.skripsi.jagungku.database.DataVarieties
import com.skripsi.jagungku.databinding.ActivityFilterSuhuUdaraListBinding
import com.skripsi.jagungku.ui.admin.varieties.DetailVarietiesActivity
import com.skripsi.jagungku.ui.searchfilter.FilterKetinggianTanahListActivity.Companion

class FilterSuhuUdaraListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterSuhuUdaraListBinding
    private lateinit var suhuUdaraArrayList: ArrayList<DataVarieties>
    private lateinit var filterSuhuUdaraAdapter: FilterSuhuUdaraAdapter
    private lateinit var searchView: SearchView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterSuhuUdaraListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvFilterSuhuUdara.layoutManager = LinearLayoutManager(this)
        binding.rvFilterSuhuUdara.setHasFixedSize(true)

        suhuUdaraArrayList = arrayListOf()

        searchView = SearchView(this)

        filterSuhuUdaraAdapter = FilterSuhuUdaraAdapter(suhuUdaraArrayList)

        db = FirebaseDatabase.getInstance().getReference(DATA_VARIETAS)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        showDataSuhuUdaraFilterList()
    }

    private fun showDataSuhuUdaraFilterList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                suhuUdaraArrayList.clear()
                if (snapshot.exists()) {
                    for (dataVarieties in snapshot.children) {
                        val listDataSuhuUdaraFilter =
                            dataVarieties.getValue(DataVarieties::class.java)
                        suhuUdaraArrayList.add(listDataSuhuUdaraFilter!!)
                    }

                    filterSuhuUdaraAdapter = FilterSuhuUdaraAdapter(suhuUdaraArrayList)
                    binding.rvFilterSuhuUdara.adapter = filterSuhuUdaraAdapter

                    filterSuhuUdaraAdapter.setOnItemClickListener(object : FilterSuhuUdaraAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FilterSuhuUdaraListActivity, DetailVarietiesActivity::class.java)

                            intent.putExtra("idVarietas", suhuUdaraArrayList[position].idVarietas)
                            intent.putExtra("namaVarietas", suhuUdaraArrayList[position].namaVarietas)
                            intent.putExtra("hargaBeli", suhuUdaraArrayList[position].hargaBeli)
                            intent.putExtra("potensiHasil", suhuUdaraArrayList[position].potensiHasil)
                            intent.putExtra("usiaTanam", suhuUdaraArrayList[position].usiaTanam)
                            intent.putExtra("ketinggianTanah", suhuUdaraArrayList[position].ketinggianTanah)
                            intent.putExtra("kekebalanHama", suhuUdaraArrayList[position].kekebalanHama)
                            intent.putExtra("ukuranTongkol", suhuUdaraArrayList[position].ukuranTongkol)
                            intent.putExtra("suhuUdara", suhuUdaraArrayList[position].suhuUdara)
                            intent.putExtra("phTanah", suhuUdaraArrayList[position].phTanah)
                            startActivity(intent)
                        }
                    })

                    if (suhuUdaraArrayList.size != 0) {
                        val search = searchView.query.toString()
                        if (search.isNotEmpty()) {
                            filterSuhuUdaraAdapter.filter.filter(search)
                        }
                    }

                    filterSuhuUdaraAdapter.notifyDataSetChanged()
                }

                if (suhuUdaraArrayList.isNotEmpty()) {
                    binding.rvFilterSuhuUdara.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE
                } else {
                    binding.rvFilterSuhuUdara.visibility = View.GONE
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
                filterSuhuUdaraAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterSuhuUdaraAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val DATA_VARIETAS = "data_varietas"
    }
}