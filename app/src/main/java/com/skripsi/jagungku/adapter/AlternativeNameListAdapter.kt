package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.adapter.AlternativeNameListAdapter.*
import com.skripsi.jagungku.database.dssmodel.DataAlternative

class AlternativeNameListAdapter(private val listAlternatif: ArrayList<DataAlternative>) :
    RecyclerView.Adapter<ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemview: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemview) {
        val tvNamaAlternatif: TextView = itemview.findViewById(R.id.tv_nama_alternatif_in_alternatif_nama)
        val tvNomorAlternatif: TextView = itemview.findViewById(R.id.tv_nomor_alternatif_nama)

        init {
            itemview.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.data_alternative_name, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataAlternatif = listAlternatif[position]

        val nomor = position + 1
        val setRank = "$nomor."

        holder.tvNomorAlternatif.text = setRank
        holder.tvNamaAlternatif.text = dataAlternatif.namaAlternatif
    }

    override fun getItemCount(): Int {
        return listAlternatif.size
    }
}