package com.example.practica5.common.util

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

object MonumentsUtil {

    fun ImageView.render(url: String) {
        Glide.with(this.context).load(url).into(this)
    }

    fun hideViews(vararg views: View) {
        views.forEach { it.visibility = View.GONE }
    }
    fun showViews(vararg views: View) {
        views.forEach { it.visibility = View.VISIBLE }
    }

    fun View.setVisibleOrGone(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

}