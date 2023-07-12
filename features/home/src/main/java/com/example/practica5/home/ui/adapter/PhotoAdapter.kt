package com.example.practica5.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.practica5.common.util.MonumentsUtil.render
import com.example.practica5.home.databinding.RowDetailPhotoBinding
import com.example.practica5.model.vo.ImageVO

class PhotoAdapter : ListAdapter<ImageVO, PhotoAdapter.PhotoViewHolder>(PhotoDiffCallback()) {

    inner class PhotoViewHolder(binding: RowDetailPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.detailImgViewPhoto

        fun bind(photo: ImageVO) {
            imageView.render(photo.url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = RowDetailPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }
}

class PhotoDiffCallback : DiffUtil.ItemCallback<ImageVO>() {
    override fun areItemsTheSame(oldItem: ImageVO, newItem: ImageVO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageVO, newItem: ImageVO): Boolean {
        return oldItem == newItem
    }
}