package com.asif.lithiumaktie.common

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import com.asif.lithiumaktie.R
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

fun Context.shareApp() {

    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, getString(R.string.app_name)
        )

        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://lithium-aktie.de/")
        startActivity(
            Intent.createChooser(
                shareIntent,
                "Share using "
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private var mLastClickTime: Long = 0

fun disableClick(): Boolean {

    if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
        return false
    }
    mLastClickTime = SystemClock.elapsedRealtime()
    return true
}
