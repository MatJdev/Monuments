package com.example.practica5.home.ui.adapter

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.practica5.common.util.MonumentsConstant.FAVORITE_CHECK
import com.example.practica5.common.util.MonumentsConstant.FAVORITE_UNCHECK
import com.example.practica5.common.util.MonumentsUtil.render
import com.example.practica5.home.R
import com.example.practica5.home.databinding.RowMonumentsMonumentBinding
import com.example.practica5.model.vo.MonumentVO

class MonumentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = RowMonumentsMonumentBinding.bind(view)
    fun bind(
        monument: MonumentVO,
        onClickListener: (MonumentVO) -> Unit,
        onFavoriteClickListener: (MonumentVO) -> Unit
    ) {
        drawMonumentInfo(monument)
        loadMonumentImg(monument)
        initListener(monument, onClickListener, onFavoriteClickListener)
    }

    private fun drawMonumentInfo(monument: MonumentVO) = with(binding) {
        monumentsLabelName.text = monument.name
        monumentsLabelCity.text = monument.city
        if (monument.isFavorite) {
            monumentsBtnFavorite.icon =
                ContextCompat.getDrawable(monumentsBtnFavorite.context, R.drawable.ic__monuments_star_yellow_24)
            monumentsBtnFavorite.tag = FAVORITE_CHECK
        } else {
            monumentsBtnFavorite.icon =
                ContextCompat.getDrawable(monumentsBtnFavorite.context, R.drawable.ic__monuments_star_border_24)
            monumentsBtnFavorite.tag = FAVORITE_UNCHECK
        }
    }

    private fun loadMonumentImg(monument: MonumentVO) = with(binding) {
        monumentsImgMain.render(monument.images.first().url)
        monumentsImgFlag.render(monument.countryFlag)
    }

    private fun initListener(
        monument: MonumentVO,
        onClickListener: (MonumentVO) -> Unit,
        onFavoriteClickListener: (MonumentVO) -> Unit
    ) = with(binding) {
        itemView.setOnClickListener {
            onClickListener(monument)
        }

        monumentsBtnFavorite.setOnClickListener {
            onFavoriteClickListener(monument)
            val icon: Int
            if (monumentsBtnFavorite.tag == FAVORITE_UNCHECK) {
                icon = R.drawable.ic__monuments_star_yellow_24
                monumentsBtnFavorite.tag = FAVORITE_CHECK
            } else {
                icon = R.drawable.ic__monuments_star_border_24
                monumentsBtnFavorite.tag = FAVORITE_UNCHECK
            }
            monumentsBtnFavorite.icon = ContextCompat.getDrawable(monumentsBtnFavorite.context, icon)
            Log.i("TAG", monumentsBtnFavorite.tag.toString())
        }
    }
}