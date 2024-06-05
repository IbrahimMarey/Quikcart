package com.example.quikcart.utils

import androidx.room.TypeConverter
import com.example.quikcart.models.entities.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromImage(image: Image?): String? {
        return Gson().toJson(image)
    }

    @TypeConverter
    fun toImage(imageString: String?): Image? {
        return Gson().fromJson(imageString, Image::class.java)
    }

    @TypeConverter
    fun fromImagesItemList(images: List<ImagesItem>?): String? {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun toImagesItemList(imagesString: String?): List<ImagesItem>? {
        val listType = object : TypeToken<List<ImagesItem>>() {}.type
        return Gson().fromJson(imagesString, listType)
    }

    @TypeConverter
    fun fromVariantsItemList(variants: List<VariantsItem>?): String? {
        return Gson().toJson(variants)
    }

    @TypeConverter
    fun toVariantsItemList(variantsString: String?): List<VariantsItem>? {
        val listType = object : TypeToken<List<VariantsItem>>() {}.type
        return Gson().fromJson(variantsString, listType)
    }

    @TypeConverter
    fun fromOptionsItemList(options: List<OptionsItem>?): String? {
        return Gson().toJson(options)
    }

    @TypeConverter
    fun toOptionsItemList(optionsString: String?): List<OptionsItem>? {
        val listType = object : TypeToken<List<OptionsItem>>() {}.type
        return Gson().fromJson(optionsString, listType)
    }

    @TypeConverter
    fun fromAny(templateSuffix: Any?): String? {
        return Gson().toJson(templateSuffix)
    }

    @TypeConverter
    fun toAny(templateSuffixString: String?): Any? {
        return Gson().fromJson(templateSuffixString, Any::class.java)
    }
}