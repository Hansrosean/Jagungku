package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.DataPriceList

class PriceListAdapter (private val listPriceList: ArrayList<DataPriceList>) : RecyclerView.Adapter<PriceListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onEditItem(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        val tvNamaVarietas: TextView = itemView.findViewById(R.id.tv_daftar_harga_nama_varietas)
        val tvHargaBeli: TextView = itemView.findViewById(R.id.tv_item_daftar_harga_beli)
        val tvHargaJual: TextView = itemView.findViewById(R.id.tv_item_daftar_harga_jual)
        val tvTanggalDitambahkan: TextView = itemView.findViewById(R.id.tv_item_tanggal_ditambahkan)

        private val imgEditDaftarHarga: ImageView = itemView.findViewById(R.id.img_edit_daftar_harga)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
            imgEditDaftarHarga.setOnClickListener {
                clickListener.onEditItem(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.data_pricelist, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPriceList = listPriceList[position]
        holder.tvNamaVarietas.text = currentPriceList.namaVarietas
        holder.tvHargaBeli.text = "Rp. " + currentPriceList.hargaBeli
        holder.tvHargaJual.text = "Rp. " + currentPriceList.hargaJual
        holder.tvTanggalDitambahkan.text = currentPriceList.tanggalDitambahkan
    }

    override fun getItemCount(): Int {
        return listPriceList.size
    }
}