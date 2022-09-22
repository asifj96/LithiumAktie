package com.asif.lithiumaktie.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.common.getCurrentDate
import com.asif.lithiumaktie.databinding.FragmentDatenschutzerklarungBinding
import com.asif.lithiumaktie.databinding.FragmentImpressumBinding
import kotlinx.android.synthetic.main.header_layout.view.*

class DatenschutzerklarungFragment : Fragment() {

    private lateinit var mBinding: FragmentDatenschutzerklarungBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_datenschutzerklarung, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.headerMain.tvDate.text = "Aktientipp ${requireContext().getCurrentDate()}"
    }
}