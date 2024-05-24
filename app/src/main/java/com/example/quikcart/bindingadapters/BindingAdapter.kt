package com.example.quikcart.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.quikcart.ui.utils.ImageUtils

@BindingAdapter("imageUrl")
fun loadImage(view:ImageView,url:String){
    ImageUtils.loadImage(view,url)
}