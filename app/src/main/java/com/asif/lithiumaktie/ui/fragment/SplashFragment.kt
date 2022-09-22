package com.asif.lithiumaktie.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.databinding.FragmentSplashBinding
import timber.log.Timber

class SplashFragment : Fragment() {

    private lateinit var mBinding: FragmentSplashBinding
    private val splashHandler = Handler(Looper.getMainLooper())
    private val splashRunnable = Runnable {
        gotoMainScreen()
    }

    private fun gotoMainScreen() {
        findNavController().navigate(R.id.action_splashFragment_to_impressumFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.e("onViewCreated")
    }

    private fun startHandler() {
        splashHandler.postDelayed(splashRunnable, 2000)
    }

    override fun onResume() {
        super.onResume()
        startHandler()
    }

    override fun onPause() {
        splashHandler.removeCallbacks(splashRunnable)
        super.onPause()
    }
}