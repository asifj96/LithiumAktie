package com.asif.lithiumaktie.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.common.getCurrentDate
import com.asif.lithiumaktie.common.hideKeyboard
import com.asif.lithiumaktie.common.openStrongBuy
import com.asif.lithiumaktie.databinding.FragmentDatenschutzerklarungBinding
import kotlinx.android.synthetic.main.header_layout.view.*

class DatenschutzerklarungFragment : Fragment() {

    private lateinit var mBinding: FragmentDatenschutzerklarungBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_datenschutzerklarung,
                container,
                false
            )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (isAdded)
                requireActivity().hideKeyboard(mBinding.root)

            mBinding.headerMain.tvDate.text = "Aktientipp am ${requireContext().getCurrentDate()}"
            mBinding.headerMain.tvStrongBuy.setOnClickListener { requireActivity().openStrongBuy() }

        } catch (e: Exception) {
            e.printStackTrace()
        }
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