package com.asif.lithiumaktie.common

import android.content.Context
import android.widget.Toast
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun Context.showToast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun Context.getCurrentDate(): String {

    val formatDate: String?
    var date: Date? = null
    val getCurrentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
        Date()
    )
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    try {
        date = formatter.parse(getCurrentDate)
        Timber.e("formatted date $date")
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    formatDate = SimpleDateFormat("a MM/dd/yyyy", Locale.getDefault()).format(date!!)

    return formatDate
}