package com.example.quikcart.utils

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    private val current = LocalDateTime.now().format(formatter)
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateAndTime():String{
        return current
    }
}