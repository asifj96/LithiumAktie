package com.asif.lithiumaktie.common

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.dialog.AlertDialog

fun Context.showToast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun Context.shareApp() {

    try {
        val shareIntent = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT, getString(R.string.app_name)
            )
            putExtra(Intent.EXTRA_TEXT, "https://lithium-aktie.de/")
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }, "Share using")
        startActivity(shareIntent)

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

fun Context.isOnline(): Boolean {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            }
            return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                true
            } else {
                false
            }
        }
    } else {
        val cm =
            this.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
    return false
}

fun Context.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showAlertDialog(
    parentFragmentManager: FragmentManager,
    msg: String,
    onClickCallback: () -> Unit
) {

    val alertDialog =
        AlertDialog {
            onClickCallback.invoke()
        }
    alertDialog.setMsg(msg)
    alertDialog.isCancelable = false
    alertDialog.show(
        parentFragmentManager,
        AlertDialog.TAG
    )
}
