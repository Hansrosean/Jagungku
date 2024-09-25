package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.dssmodel.DataAlternative

class AlternativeListAdapter(private val listAlternatif: ArrayList<DataAlternative>) :
    RecyclerView.Adapter<AlternativeListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemAddClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemview: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemview) {
        val tvNamaAlternatif: TextView = itemview.findViewById(R.id.tv_data_nama_alternatif)
        private val imgTambahNilaiAlternatif: ImageView =
            itemview.findViewById(R.id.img_add_alternatif_nilai)
        val tvNomorAlternatif: TextView = itemview.findViewById(R.id.tv_nomor_alternatif)

        init {
            itemview.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }

            imgTambahNilaiAlternatif.setOnClickListener {
                clickListener.onItemAddClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.data_alternative, parent, false)
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