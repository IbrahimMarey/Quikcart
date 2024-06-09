package com.example.quikcart.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.quikcart.R
import com.example.quikcart.utils.ImageUtils
import com.google.android.material.slider.Slider

@BindingAdapter("imageUrl")
fun loadImage(view:ImageView,url:String){
    ImageUtils.loadImage(view,url)
}


@BindingAdapter("imageDrawable")
fun setDrawableImage(image:ImageView,imgId:Int){
    image.setImageResource(imgId)
}

@BindingAdapter("app:valueFrom")
fun setValueFrom(slider: Slider, value: Int?) {
    value?.let {
        slider.valueFrom = it.toFloat()
    }
}

@BindingAdapter("app:valueTo")
fun setValueTo(slider: Slider, value: Int?) {
    value?.let {
        slider.valueTo = it.toFloat()
    }
}

@BindingAdapter("app:sliderValue")
fun setSliderValue(slider: Slider, value: Int?) {
    value?.let {
        slider.value = it.toFloat()
    }
}

@BindingAdapter("onChangeListener")
fun setOnSliderChangeListener(slider: Slider,listener:Slider.OnChangeListener){
    slider.addOnChangeListener(listener)
}