package com.example.practica5.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica5.databinding.RowNewmonumentImageBinding
import com.example.practica5.domain.model.vo.ImageVO

class ImageAdapter : ListAdapter<ImageVO, ImageAdapter.ImageViewHolder>(ImageDiffCallback()) {

    private var onLongClickListener: ((ImageVO) -> Unit)? = null

    fun setOnLongClickListener(listener: (ImageVO) -> Unit) {
        onLongClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowNewmonumentImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(image)
            true
        }
    }

    inner class ImageViewHolder(private val binding: RowNewmonumentImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: ImageVO) {
            Glide.with(binding.root)
                .load(image.url)
                .into(binding.newMonumentImgImages)
        }
    }

    private class ImageDiffCallback : DiffUtil.ItemCallback<ImageVO>() {
        override fun areItemsTheSame(oldItem: ImageVO, newItem: ImageVO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageVO, newItem: ImageVO): Boolean {
            return oldItem == newItem
        }
    }
}