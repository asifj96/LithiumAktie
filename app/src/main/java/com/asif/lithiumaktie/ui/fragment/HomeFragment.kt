package com.asif.lithiumaktie.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.databinding.FragmentHomeBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.MimeTypes


class HomeFragment : Fragment(), Player.Listener {

    private lateinit var mBinding: FragmentHomeBinding
    var simpleExoplayer: ExoPlayer? = null
    var isVideoEnded = false
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
        initPlayerView()

        simpleExoplayer?.addListener(this)

    }

    private fun initPlayerView() {
        simpleExoplayer = ExoPlayer.Builder(requireContext()).build()
        simpleExoplayer?.also {
            mBinding.exoplayer.player = it
            val mediaItem = MediaItem.Builder()
                .setUri("https://www.youtube.com/shorts/hHz8kUb1g2o")
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()
            it.setMediaItem(mediaItem)
            it.prepare()
            it.addListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: HAHAHAH")
        if (simpleExoplayer == null)
            initPlayerView()
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart: HAHAHAH")
        if (simpleExoplayer == null)
            initPlayerView()
    }

    @SuppressLint("LogNotTimber")
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                Log.d(
                    "TAG",
                    "onPlaybackStateChanged:  STATE_BUFFERING"
                )
//                viewBinding.pbLoadVideo.visibility = View.VISIBLE
            }
            Player.STATE_READY -> {
                Log.d(
                    "TAG",
                    "onPlaybackStateChanged:  STATE_READY"
                )
//                viewBinding.pbLoadVideo.visibility = View.INVISIBLE
            }
            Player.STATE_ENDED -> {
                isVideoEnded = true
                simpleExoplayer?.pause()
                simpleExoplayer?.seekTo(0)
//                viewBinding.ivPlayVideo.setImageResource(R.drawable.ic_play_icon)
                Log.d(
                    "TAG",
                    "onPlaybackStateChanged:  STATE_ENDED"
                )
            }
            Player.STATE_IDLE -> {
                Log.d(
                    "TAG",
                    "onPlaybackStateChanged:  STATE_IDLE"
                )
            }
        }

    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPause: HAHAHAH")
        simpleExoplayer?.run { releasePlayer() }
//        viewBinding.ivPlayVideo.setImageResource(R.drawable.ic_play_icon)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy: HAHAHAH")
        simpleExoplayer?.run { releasePlayer() }
    }

    fun releasePlayer() {
        simpleExoplayer?.run {
            stop()
            release()
        }
        simpleExoplayer = null
    }
   /* private fun playPauseVideo(view: ImageView) {
        simpleExoplayer?.let {
            if (it.playWhenReady) {
                view.setImageResource(R.drawable.ic_play_icon)
            } else {
                view.setImageResource(R.drawable.ic_pause_icon)
            }
            if (isVideoEnded) {
                it.seekTo(0)
            }
            it.playWhenReady = !it.playWhenReady
        }
    }*/
}