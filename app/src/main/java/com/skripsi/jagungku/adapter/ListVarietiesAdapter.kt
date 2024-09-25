package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.DataVarieties


class ListVarietiesAdapter(listVarieties: FirebaseRecyclerOptions<DataVarieties>) :
    FirebaseRecyclerAdapter<DataVarieties, ListVarietiesAdapter.ViewHolder>(listVarieties) {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        val tvNamaVarietas: TextView = itemView.findViewById(R.id.tv_data_nama_varietas)
        val tvNomorVarietas: TextView = itemView.findViewById(R.id.tv_nomor_varietas)

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
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.data_varieties, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: DataVarieties) {
//        val currentVarieties = DataVarieties
        val nomor = position + 1
        val setRank = "$nomor."

        holder.tvNomorVarietas.text = setRank
        holder.tvNamaVarietas.text = model.namaVarietas
    }

//    override fun getItemCount(): Int {
//        return listVarieties.size
//    }
}

/*
* adapter ini  menggunakan firebase ui adapter (FirebaseRecyclerAdapter) untuk data realtime
* */