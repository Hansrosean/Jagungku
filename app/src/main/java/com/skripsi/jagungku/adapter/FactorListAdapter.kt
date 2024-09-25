package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.dssmodel.DataFactor


class FactorListAdapter(private val faktorList: ArrayList<DataFactor>) :
    RecyclerView.Adapter<FactorListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemview: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemview) {
        val tvNomorFaktor: TextView = itemview.findViewById(R.id.tv_nomor_faktor)
        val tvKodeFaktor: TextView = itemview.findViewById(R.id.tv_kode_faktor)
        val tvNamaFaktor: TextView = itemview.findViewById(R.id.tv_nama_faktor)
        val tvBobotFaktor: TextView = itemview.findViewById(R.id.tv_bobot_faktor)

        private val imgEditFaktor: ImageView = itemview.findViewById(R.id.img_edit_faktor)

        init {
            imgEditFaktor.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.data_factor, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataFaktor = faktorList[position]
        val setNomor = position + 1

        holder.tvNomorFaktor.text = setNomor.toString()
        holder.tvKodeFaktor.text = dataFaktor.kodeFaktor
        holder.tvNamaFaktor.text = dataFaktor.namaFaktor
        holder.tvBobotFaktor.text = dataFaktor.bobotFaktor.toString()
    }

    override fun getItemCount(): Int {
        return faktorList.size
    }
}