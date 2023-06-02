package com.example.practica5.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.practica5.R
import com.example.practica5.domain.model.vo.MonumentVO

class MonumentsListAdapter(private val onClickListener: (MonumentVO) -> Unit, private val onFavoriteClickListener: (MonumentVO) -> Unit) :
    ListAdapter<MonumentVO, MonumentsViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonumentsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MonumentsViewHolder(
            layoutInflater.inflate(R.layout.row_monuments_monument, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MonumentsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener, onFavoriteClickListener)
    }

    class DiffCallback : DiffUtil.ItemCallback<MonumentVO>() {
        override fun areItemsTheSame(oldItem: MonumentVO, newItem: MonumentVO): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MonumentVO, newItem: MonumentVO): Boolean = oldItem == newItem
    }
}