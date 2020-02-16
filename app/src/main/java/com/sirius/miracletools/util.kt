package com.sirius.miracletools

import android.content.Context
import android.content.pm.ApplicationInfo
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


object Util {
    @JvmStatic
    fun getApplicationName(
        context: Context,
        applicationInfo: ApplicationInfo
    ) = context.packageManager.getApplicationLabel(applicationInfo)

    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.getContext())
            .load(imageUrl).apply(RequestOptions().circleCrop())
            .into(view)
    }
}
