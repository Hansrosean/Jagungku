package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.dssmodel.DataFactor

class SecondFactorListAdapter(private val secondDataFactor: ArrayList<DataFactor>) :
    RecyclerView.Adapter<SecondFactorListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemview: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemview) {
        val tvNamaFaktor: TextView = itemview.findViewById(R.id.tv_second_data_nama_faktor)

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
            LayoutInflater.from(parent.context).inflate(R.layout.second_data_factor, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataFaktor = secondDataFactor[position]
        holder.tvNamaFaktor.text = dataFaktor.namaFaktor
    }

    override fun getItemCount(): Int {
        return secondDataFactor.size
    }
}