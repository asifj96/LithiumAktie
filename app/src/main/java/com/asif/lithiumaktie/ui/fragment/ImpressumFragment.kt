package com.asif.lithiumaktie.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.common.disableClick
import com.asif.lithiumaktie.common.hideKeyboard
import com.asif.lithiumaktie.databinding.FragmentImpressumBinding
import kotlinx.android.synthetic.main.header_layout.view.*
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ImpressumFragment : Fragment() {

    private lateinit var mBinding: FragmentImpressumBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_impressum, container, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (isAdded)
                requireActivity().hideKeyboard(mBinding.root)

            mBinding.headerMain.tvDate.text = "Aktientipp am ${getCurrentDate()}"
            mBinding.headerMain.tvStrongBuy.setOnClickListener {
                if (disableClick()) {

                    val uri = Uri.parse(getString(R.string.strong_buy_url))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getCurrentDate(): String {

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
//    formatDate = SimpleDateFormat("a MM/dd/yyyy", Locale.getDefault()).format(date!!)
        formatDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(date!!)

        return formatDate
    }

    override fun onPause() {
        super.onPause()
        try {
            if (isAdded)
                requireActivity().hideKeyboard(mBinding.root)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}