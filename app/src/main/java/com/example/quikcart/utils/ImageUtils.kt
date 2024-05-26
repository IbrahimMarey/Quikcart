package com.example.quikcart.utils

import android.widget.ImageView
import com.example.quikcart.R
import com.squareup.picasso.Picasso

object ImageUtils {
    fun loadImage(imageView: ImageView,url:String){
        Picasso.get().load(url)
            .placeholder(R.drawable.ic_brand)
            .error(R.drawable.ic_brand)
            .into(imageView)
    }
}