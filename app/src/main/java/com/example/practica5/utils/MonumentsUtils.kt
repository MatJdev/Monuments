package com.example.practica5.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.practica5.R
import com.example.practica5.databinding.FragmentDetailBinding
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.ui.adapter.PhotoAdapter
import com.google.android.material.button.MaterialButton
import java.io.IOException
import java.util.Locale

object MonumentsUtils {
    fun getCountryFromLocation(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        var country: String? = null

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                country = addresses[0].countryName
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return country
    }

    fun getCountryCodeFromLocation(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        if (!addresses.isNullOrEmpty()) {
            return addresses[0].countryCode
        }

        return null
    }

    fun FragmentDetailBinding.renderMonument(monument: MonumentVO, adapter: PhotoAdapter) {
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

    fun ImageView.render(url: String) {
        Glide.with(this.context).load(url).into(this)
    }

    fun hideViews(vararg views: View) {
        views.forEach { it.visibility = View.GONE }
    }
    fun showViews(vararg views: View) {
        views.forEach { it.visibility = View.VISIBLE }
    }

    private fun View.setVisibleOrGone(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
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