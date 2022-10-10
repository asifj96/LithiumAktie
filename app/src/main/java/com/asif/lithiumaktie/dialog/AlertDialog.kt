package com.asif.lithiumaktie.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.common.hideKeyboard
import com.asif.lithiumaktie.databinding.DialogAlertBinding

class AlertDialog(private val onClickCallback: () -> Unit) :
    DialogFragment() {

    companion object {
        const val TAG = "AlertDialog"
    }

    private var msg: String = ""
    private val confirmationBinding: DialogAlertBinding by lazy {
        DialogAlertBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_edit_text)
        dialog!!.setCancelable(false)
        return confirmationBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmationBinding.tvFormMsg.text = msg
        confirmationBinding.tvOk.setOnClickListener {
            onClickCallback.invoke()
            dialog!!.dismiss()
            requireActivity().hideKeyboard(it)
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
//        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setMsg(msg: String) {

        this.msg = msg
    }
}