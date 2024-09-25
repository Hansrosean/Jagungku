package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.dssmodel.DataSubFactor

class SubFactorListAdapter(private val listSubFactor: ArrayList<DataSubFactor>) :
    RecyclerView.Adapter<SubFactorListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemview: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemview) {

        val tvNamaFaktor: TextView = itemview.findViewById(R.id.tv_nama_faktor_in_sub)
        val tvSubFaktor1: TextView = itemview.findViewById(R.id.tv_nama_sub_faktor_1)
        val tvSubFaktor2: TextView = itemview.findViewById(R.id.tv_nama_sub_faktor_2)
        val tvSubFaktor3: TextView = itemview.findViewById(R.id.tv_nama_sub_faktor_3)
        val tvNilaiSubFaktor1: TextView = itemview.findViewById(R.id.tv_nilai_sub_faktor_1)
        val tvNilaiSubFaktor2: TextView = itemview.findViewById(R.id.tv_nilai_sub_faktor_2)
        val tvNilaiSubFaktor3: TextView = itemview.findViewById(R.id.tv_nilai_sub_faktor_3)

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
            LayoutInflater.from(parent.context).inflate(R.layout.data_sub_factor, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataSubFaktor = listSubFactor[position]

        holder.tvNamaFaktor.text = dataSubFaktor.namaFaktor
        holder.tvSubFaktor1.text = dataSubFaktor.subFaktor1
        holder.tvSubFaktor2.text = dataSubFaktor.subFaktor2
        holder.tvSubFaktor3.text = dataSubFaktor.subFaktor3
        holder.tvNilaiSubFaktor1.text = dataSubFaktor.nilaiSubFaktor1
        holder.tvNilaiSubFaktor2.text = dataSubFaktor.nilaiSubFaktor2
        holder.tvNilaiSubFaktor3.text = dataSubFaktor.nilaiSubFaktor3
    }

    override fun getItemCount(): Int {
        return listSubFactor.size
    }
}