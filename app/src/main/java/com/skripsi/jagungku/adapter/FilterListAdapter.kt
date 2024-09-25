package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.datastore.preferences.protobuf.StringValue
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.DataVarieties


class FilterListAdapter(
    private val dataVarietasFilter: ArrayList<DataVarieties>,
    private var dataVarietasBackup: ArrayList<DataVarieties> = ArrayList(dataVarietasFilter)
) :
    RecyclerView.Adapter<FilterListAdapter.ViewHolder>(), Filterable {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFilterNamaVarietas: TextView = itemView.findViewById(R.id.tv_filter_nama_varietas)
        val tvFilterKetinggianTanah: TextView =
            itemView.findViewById(R.id.tv_filter_ketinggian_tanah)
        val tvFilterSuhuUdara: TextView = itemView.findViewById(R.id.tv_filter_suhu_udara)
        val tvFilterPhTanah: TextView = itemView.findViewById(R.id.tv_filter_ph_tanah)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.data_filter, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val getDataVarietasFilter = dataVarietasFilter[position]

        holder.tvFilterNamaVarietas.text = getDataVarietasFilter.namaVarietas
        holder.tvFilterKetinggianTanah.text = getDataVarietasFilter.ketinggianTanah.toString()
        holder.tvFilterSuhuUdara.text = getDataVarietasFilter.suhuUdara.toString()
        holder.tvFilterPhTanah.text = getDataVarietasFilter.phTanah.toString()
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
                        val getKetinggianTanah: String = StringValue.of(obj.ketinggianTanah.toString()).toString()
                        if (getKetinggianTanah.toLowerCase().contains(search.toLowerCase())) {
                            filteredData.add(obj)
                        }
                    }

                    if (!filteredData.contains(obj) && obj.suhuUdara != 0.0) {
                        val getSuhuUdara: String = StringValue.of(obj.suhuUdara.toString()).toString()
                        if (getSuhuUdara.toLowerCase().contains(search.toLowerCase())) {
                            filteredData.add(obj)
                        }
                    }

                    if (!filteredData.contains(obj) && obj.phTanah != 0.0) {
                        val getPhTanah: String = StringValue.of(obj.phTanah.toString()).toString()
                        if (getPhTanah.toLowerCase().contains(search.toLowerCase())) {
                            filteredData.add(obj)
                        }
                    }
                }
            }

            val filterResult = FilterResults()
            filterResult.values = filteredData

            return filterResult
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            dataVarietasFilter.clear()
            dataVarietasFilter.addAll(results.values as ArrayList<DataVarieties>)
            notifyDataSetChanged()
        }
    }
}