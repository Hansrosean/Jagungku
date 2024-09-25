package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.dssmodel.DataRank

class RankListAdapter(private val rankList: ArrayList<DataRank>) :
    RecyclerView.Adapter<RankListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemview: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemview) {
        val tvNamaAlternatif: TextView = itemview.findViewById(R.id.tv_nama_alternatif_in_rank)
        val tvTotalEvaluasiAlternatif: TextView = itemview.findViewById(R.id.tv_total_evaluasi_alternatif_in_rank)
        val tvNomorRank: TextView = itemview.findViewById(R.id.tv_nomor_rank)

        init {
            itemview.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.data_rank, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataRank = rankList[position]
        val getRank = position + 1
        val setRank = "Rank $getRank"

        holder.tvNamaAlternatif.text = dataRank.namaAlternatif
        holder.tvTotalEvaluasiAlternatif.text = dataRank.totalEvaluasiAlternatif.toString()
        holder.tvNomorRank.text = setRank
    }

    override fun getItemCount(): Int {
        return rankList.size
    }
}