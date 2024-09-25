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
import com.skripsi.jagungku.adapter.FilterPhTanahAdapter
import com.skripsi.jagungku.database.DataVarieties
import com.skripsi.jagungku.databinding.ActivityFilterPhTanahListBinding
import com.skripsi.jagungku.ui.admin.varieties.DetailVarietiesActivity
import com.skripsi.jagungku.ui.searchfilter.FilterKetinggianTanahListActivity.Companion.DATA_VARIETAS

class FilterPhTanahListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterPhTanahListBinding
    private lateinit var phTanahArrayList: ArrayList<DataVarieties>
    private lateinit var filterPhTanahAdapter: FilterPhTanahAdapter
    private lateinit var searchView: SearchView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterPhTanahListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvFilterPhTanah.layoutManager = LinearLayoutManager(this)
        binding.rvFilterPhTanah.setHasFixedSize(true)

        phTanahArrayList = arrayListOf()

        searchView = SearchView(this)

        filterPhTanahAdapter = FilterPhTanahAdapter(phTanahArrayList)

        db = FirebaseDatabase.getInstance().getReference(DATA_VARIETAS)

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        showDataPhTanahFilterList()
    }

    private fun showDataPhTanahFilterList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                phTanahArrayList.clear()
                if (snapshot.exists()) {
                    for (dataVarieties in snapshot.children) {
                        val listDataPhTanahFilter = dataVarieties.getValue(DataVarieties::class.java)
                        phTanahArrayList.add(listDataPhTanahFilter!!)
                    }

                    filterPhTanahAdapter = FilterPhTanahAdapter(phTanahArrayList)
                    binding.rvFilterPhTanah.adapter = filterPhTanahAdapter

                    filterPhTanahAdapter.setOnItemClickListener(object : FilterPhTanahAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FilterPhTanahListActivity, DetailVarietiesActivity::class.java)

                            intent.putExtra("idVarietas", phTanahArrayList[position].idVarietas)
                            intent.putExtra("namaVarietas", phTanahArrayList[position].namaVarietas)
                            intent.putExtra("hargaBeli", phTanahArrayList[position].hargaBeli)
                            intent.putExtra("potensiHasil", phTanahArrayList[position].potensiHasil)
                            intent.putExtra("usiaTanam", phTanahArrayList[position].usiaTanam)
                            intent.putExtra("ketinggianTanah", phTanahArrayList[position].ketinggianTanah)
                            intent.putExtra("kekebalanHama", phTanahArrayList[position].kekebalanHama)
                            intent.putExtra("ukuranTongkol", phTanahArrayList[position].ukuranTongkol)
                            intent.putExtra("suhuUdara", phTanahArrayList[position].suhuUdara)
                            intent.putExtra("phTanah", phTanahArrayList[position].phTanah)
                            startActivity(intent)
                        }
                    })

                    if (phTanahArrayList.size != 0) {
                        val search = searchView.query.toString()
                        if (search.isNotEmpty()) {
                            filterPhTanahAdapter.filter.filter(search)
                        }
                    }

                    filterPhTanahAdapter.notifyDataSetChanged()
                }

                if (phTanahArrayList.isNotEmpty()) {
                    binding.rvFilterPhTanah.visibility = View.VISIBLE
                    binding.tvDataNotFound.visibility = View.GONE
                } else {
                    binding.rvFilterPhTanah.visibility = View.GONE
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
                filterPhTanahAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPhTanahAdapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val DATA_VARIETAS = "data_varietas"
    }
}