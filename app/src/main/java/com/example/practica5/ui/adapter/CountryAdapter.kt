package com.example.practica5.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practica5.databinding.RowMonumentsCountryBinding

class CountryAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<String, CountryAdapter.ViewHolder>(CountryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowMonumentsCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = getItem(position)
        holder.bind(country)
    }

    inner class ViewHolder(private val binding: RowMonumentsCountryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(country: String) {
            binding.filterDialogLabelCountry.text = country
            itemView.setOnClickListener {
                onItemClick(country)
            }
        }
    }
}

class CountryDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}