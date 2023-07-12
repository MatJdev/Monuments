package com.example.practica5.home.util

import androidx.core.content.ContextCompat
import com.example.practica5.common.util.MonumentsUtil.render
import com.example.practica5.common.util.MonumentsUtil.setVisibleOrGone
import com.example.practica5.home.R
import com.example.practica5.home.databinding.FragmentDetailBinding
import com.example.practica5.home.ui.adapter.PhotoAdapter
import com.google.android.material.button.MaterialButton

object MonumentsUtil {

    fun FragmentDetailBinding.renderMonument(monument: com.example.practica5.model.vo.MonumentVO, adapter: PhotoAdapter) {
        detailToolbar.title = monument.name
        detailViewPager.adapter = adapter
        adapter.submitList(monument.images)
        detailLabelInfo.text = monument.description
        detailLabelCity.text = monument.city
        detailLabelName.text = monument.name
        detailImgFlag.render(monument.countryFlag)
        detailBtnRemove.setVisibleOrGone(monument.isFromMyMonuments)
        detailBtnFavorite.setFavoriteIcon(monument.isFavorite)
    }

    private fun MaterialButton.setFavoriteIcon(isFavorite: Boolean) {
        val iconRes = if (isFavorite) {
            R.drawable.ic__monuments_star_yellow_24
        } else {
            R.drawable.ic__monuments_star_border_24
        }
        icon = ContextCompat.getDrawable(context, iconRes)
    }

}