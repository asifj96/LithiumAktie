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
import com.asif.lithiumaktie.common.disableClick
import com.asif.lithiumaktie.databinding.FragmentSplashBinding
import timber.log.Timber

class SplashFragment : Fragment() {

    private lateinit var mBinding: FragmentSplashBinding
    private val splashHandler = Handler(Looper.getMainLooper())
    private val splashRunnable = Runnable {
        gotoMainScreen()
    }

    private fun gotoMainScreen() {
        if (disableClick()) {
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.e("onViewCreated")
    }

    private fun startHandler() {
        splashHandler.postDelayed(splashRunnable, 500)
    }

    override fun onResume() {
        super.onResume()
        try {
            startHandler()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            splashHandler.removeCallbacks(splashRunnable)
            splashHandler.removeCallbacksAndMessages(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}