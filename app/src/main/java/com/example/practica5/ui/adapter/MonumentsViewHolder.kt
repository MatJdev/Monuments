package com.example.practica5.ui.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practica5.R
import com.example.practica5.databinding.RowMonumentsMonumentBinding
import com.example.practica5.domain.model.vo.MonumentVO

class MonumentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = RowMonumentsMonumentBinding.bind(view)
    fun bind(monument: MonumentVO, onClickListener: (MonumentVO) -> Unit, onFavoriteClickListener: (MonumentVO) -> Unit) {
        drawMonumentInfo(monument)
        loadMonumentImg(monument)
        initListener(monument, onClickListener, onFavoriteClickListener)
    }

    private fun drawMonumentInfo(monument: MonumentVO) = with(binding) {
        monumentsLabelName.text = monument.name
        monumentsLabelCity.text = monument.city
    }

    private fun loadMonumentImg(monument: MonumentVO) = with(binding) {
        Glide.with(monumentsLabelCity.context).load(monument.images[0].url).into(monumentsImgMain)
        Glide.with(monumentsLabelCity.context).load(monument.countryFlag).into(monumentsImgFlag)
    }

    private fun initListener(monument: MonumentVO, onClickListener: (MonumentVO) -> Unit, onFavoriteClickListener: (MonumentVO) -> Unit) = with(binding) {
        itemView.setOnClickListener {
            onClickListener(monument)
        }

        monumentsBtnFavorite.setOnClickListener {
            onFavoriteClickListener(monument)
            val icon: Int
            if (monumentsBtnFavorite.tag == "unCheck") {
                icon = R.drawable.ic__monuments_star_yellow_24
                monumentsBtnFavorite.tag = "Check"
            } else {
                icon = R.drawable.ic__monuments_star_border_24
                monumentsBtnFavorite.tag = "unCheck"
            }
            monumentsBtnFavorite.icon = ContextCompat.getDrawable(monumentsBtnFavorite.context, icon)
        }
    }
}