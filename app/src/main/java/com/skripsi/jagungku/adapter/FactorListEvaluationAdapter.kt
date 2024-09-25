package com.skripsi.jagungku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.jagungku.R
import com.skripsi.jagungku.database.dssmodel.DataFactorEvaluation

class FactorListEvaluationAdapter (private val factorEvaluationList: ArrayList<DataFactorEvaluation>) : RecyclerView.Adapter<FactorListEvaluationAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    class ViewHolder (itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvNamaFaktor : TextView = itemView.findViewById(R.id.tv_nama_faktor_in_factor_evaluation)
        val tvBobotFaktor : TextView = itemView.findViewById(R.id.tv_total_bobot_faktor_in_factor_evaluation)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.data_factor_evaluation, parent, false)
        return  ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataFaktorEvaluasi = factorEvaluationList[position]

        holder.tvNamaFaktor.text = dataFaktorEvaluasi.namaFaktor
        holder.tvBobotFaktor.text = dataFaktorEvaluasi.bobotEvaluasiFaktor.toString()
    }

    override fun getItemCount(): Int {
        return factorEvaluationList.size
    }

}