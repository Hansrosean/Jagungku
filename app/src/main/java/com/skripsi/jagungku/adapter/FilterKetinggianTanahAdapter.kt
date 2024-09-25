package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.adapter.ListVarietiesAdapter.OnItemClickListener
import com.skripsi.jagungku.database.DataVarieties


class FilterKetinggianTanahAdapter(
    private val dataVarietasFilter: ArrayList<DataVarieties>,
    private var dataVarietasBackup: ArrayList<DataVarieties> = ArrayList(dataVarietasFilter)
) : RecyclerView.Adapter<FilterKetinggianTanahAdapter.ViewHolder>(), Filterable {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvFilterNamaVarietas: TextView =
            itemView.findViewById(R.id.tv_filter_nama_varietas_in_ketinggian_tanah)
        val tvFilterKetinggianTanah: TextView =
            itemView.findViewById(R.id.tv_filter_ketinggian_tanah_in_ketinggian_tanah)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(getAbsoluteAdapterPosition())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.data_ketinggian_filter, parent, false)

        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val getKetinggianTanahFilter = dataVarietasFilter[position]

        val setText = getKetinggianTanahFilter.ketinggianTanah.toString()
        holder.tvFilterNamaVarietas.text = getKetinggianTanahFilter.namaVarietas
        holder.tvFilterKetinggianTanah.text = "hingga $setText m"
    }

    override fun getItemCount(): Int {
        return dataVarietasFilter.size
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val search: String = constraint.toString()
            val filteredData = ArrayList<DataVarieties>()

            if (search.isEmpty()) {
                filteredData.addAll(dataVarietasBackup)
            } else {
                for (obj in dataVarietasBackup) {
                    if (!filteredData.contains(obj) && obj.namaVarietas != null && obj.namaVarietas.toLowerCase()
                            .contains(search.toLowerCase())
                    ) {
                        filteredData.add(obj)
                    }

                    if (!filteredData.contains(obj) && obj.ketinggianTanah != 0.0) {
                        val getKetinggianTanah = obj.ketinggianTanah
                        if (getKetinggianTanah != null && getKetinggianTanah in 0.0..1000.0) {
                            if (getKetinggianTanah.toString().lowercase().contains(search)) {
                                filteredData.add(obj)
                            }
                        }
                    }
                }
            }

            val filterResult = FilterResults()
            filterResult.values = filteredData

            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            dataVarietasFilter.clear()
            dataVarietasFilter.addAll(results.values as ArrayList<DataVarieties>)
            notifyDataSetChanged()
        }
    }
}