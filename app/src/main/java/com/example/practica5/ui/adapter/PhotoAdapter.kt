package com.example.practica5.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica5.R
import com.example.practica5.domain.model.vo.ImageVO

class PhotoAdapter(private val photos: List<ImageVO>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.detailImgViewPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_detail_photo, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        // Configurar la imagen en el ImageView
        //holder.imageView.setImageResource(photo.url)
        Glide.with(holder.imageView.context).load(photo.url).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}