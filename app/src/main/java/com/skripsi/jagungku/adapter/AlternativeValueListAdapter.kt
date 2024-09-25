package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.dssmodel.DataFactor
import com.skripsi.jagungku.database.dssmodel.DataFactorEvaluation

class AlternativeValueListAdapter(private val faktorList: ArrayList<DataFactorEvaluation>) :
    RecyclerView.Adapter<AlternativeValueListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onAddItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemview: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemview) {

        val tvNamaFaktor: TextView = itemview.findViewById(R.id.tv_nama_faktor_evaluasi)

        private val imgTambahNilaiSubFaktorAlternatif: ImageView = itemview.findViewById(R.id.img_add_nilai_alternatif)

        init {
            imgTambahNilaiSubFaktorAlternatif.setOnClickListener {
                clickListener.onAddItemClick(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.data_alternative_value, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataAlternatifNilai = faktorList[position]

        holder.tvNamaFaktor.text = dataAlternatifNilai.namaFaktor
    }

    override fun getItemCount(): Int {
        return faktorList.size
    }
}