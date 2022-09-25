package com.asif.lithiumaktie.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asif.lithiumaktie.MainActivity
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.common.disableClick
import com.asif.lithiumaktie.common.getCurrentDate
import com.asif.lithiumaktie.common.shareApp
import com.asif.lithiumaktie.databinding.FragmentHomeBinding
import com.asif.lithiumaktie.exo_player.ExoPlayerUtil
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.header_layout.view.*
import timber.log.Timber


class HomeFragment : Fragment(), Player.EventListener {

    private lateinit var mBinding: FragmentHomeBinding
    private val resValue = "assets:///video.mp4"
    private var playerUtil: ExoPlayerUtil? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerUtil = ExoPlayerUtil.getInstance(
            requireContext(),
            mBinding.videoView,
            ExoPlayerUtil.LoadingMode.ASSETS,
            this
        )

        MainActivity.activityMainBinding.bottomNav.visibility = View.VISIBLE
        mBinding.headerMain.tvDate.text = "Aktientipp ${requireContext().getCurrentDate()}"

        mBinding.tvShareArticle.setOnClickListener {

            if (disableClick()) {

                requireContext().shareApp()
            }
        }
        mBinding.headerMain.tv.setOnClickListener {

            if (disableClick()) {
                mBinding.scrollView.post {
                    mBinding.scrollView.scrollTo(0, mBinding.btnStrongBuy1.bottom)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        /* if (Util.SDK_INT > 23) {
             playerUtil?.initialize(resValue)
             playerUtil?.play()
             Timber.e(resValue)

         }*/
    }

    override fun onResume() {
        super.onResume()
//        hideSystemUi()

        try {
//            if (Util.SDK_INT <= 23 || playerUtil == null) {
            playerUtil?.initialize(resValue)
            Timber.e(resValue)
//                playerUtil?.play()
//            }
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
        }

    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            playerUtil?.release()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            playerUtil?.release()
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
        Log.d(
            "TAG", "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady
        )
    }
}
