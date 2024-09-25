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

class FilterSuhuUdaraAdapter(
    private val dataVarietasFilter: ArrayList<DataVarieties>,
    private var dataVarietasBackup: ArrayList<DataVarieties> = ArrayList(dataVarietasFilter)
) : RecyclerView.Adapter<FilterSuhuUdaraAdapter.ViewHolder>(), Filterable {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder (itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvFilterNamaVarietas: TextView = itemView.findViewById(R.id.tv_filter_nama_varietas_in_suhu_udara)
        val tvFilterSuhuUdara: TextView = itemView.findViewById(R.id.tv_filter_suhu_udara_in_suhu_udara)

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
            LayoutInflater.from(parent.context).inflate(R.layout.data_suhu_udara_filter, parent, false)

        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val getSuhuUdaraFilter = dataVarietasFilter[position]

        val setText = getSuhuUdaraFilter.suhuUdara.toString()
        holder.tvFilterNamaVarietas.text = getSuhuUdaraFilter.namaVarietas
        holder.tvFilterSuhuUdara.text = "hingga $setText°C"
    }

    override fun getItemCount(): Int {
        return  dataVarietasFilter.size
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

                    if (!filteredData.contains(obj) && obj.suhuUdara != 0.0) {
                        val getSuhuUdara: String = StringValue.of(obj.suhuUdara.toString()).toString()
                        if (getSuhuUdara.toLowerCase().contains(search.toLowerCase())) {
                            filteredData.add(obj)
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