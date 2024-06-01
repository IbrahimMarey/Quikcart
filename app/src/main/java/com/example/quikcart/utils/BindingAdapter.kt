package com.example.quikcart.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.quikcart.R
import com.example.quikcart.utils.ImageUtils

@BindingAdapter("imageUrl")
fun loadImage(view:ImageView,url:String){
    ImageUtils.loadImage(view,url)
}


@BindingAdapter("imageDrawable")
fun setDrawableImage(image:ImageView,imgId:Int){
    image.setImageResource(imgId)
}