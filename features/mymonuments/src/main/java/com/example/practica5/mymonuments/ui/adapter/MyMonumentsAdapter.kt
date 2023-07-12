package com.example.practica5.mymonuments.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practica5.common.util.MonumentsUtil.render
import com.example.practica5.model.vo.MonumentVO
import com.example.practica5.mymonuments.databinding.RowMymonumentsMonumentBinding

class MyMonumentsAdapter(private val onClickListener: (MonumentVO) -> Unit) :
    ListAdapter<MonumentVO, MyMonumentsAdapter.MyMonumentsViewHolder>(DiffCallback()) {

    inner class MyMonumentsViewHolder(private val binding: RowMymonumentsMonumentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(monument: MonumentVO, onClickListener: (MonumentVO) -> Unit) {
            with(binding) {
                myMonumentsLabelMonumentName.text = monument.name
                myMonumentsLabelMonumentDesc.text = monument.description
                myMonumentsImgMain.render(monument.images.first().url)
            }
            itemView.setOnClickListener {
                onClickListener(monument)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMonumentsViewHolder {
        val binding = RowMymonumentsMonumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyMonumentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyMonumentsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    class DiffCallback : DiffUtil.ItemCallback<MonumentVO>() {
        override fun areItemsTheSame(oldItem: MonumentVO, newItem: MonumentVO): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MonumentVO, newItem: MonumentVO): Boolean = oldItem == newItem
    }
}