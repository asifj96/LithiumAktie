package com.asif.lithiumaktie.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.asif.lithiumaktie.MainActivity
import com.asif.lithiumaktie.R
import com.asif.lithiumaktie.common.*
import com.asif.lithiumaktie.databinding.FragmentHomeBinding
import com.asif.lithiumaktie.exo_player.ExoPlayerUtil
import com.asif.lithiumaktie.model.SubscribeResponse
import com.asif.lithiumaktie.network.SubscribeApi
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.header_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class HomeFragment : Fragment(), Player.EventListener {

    private lateinit var mBinding: FragmentHomeBinding
    private val resValue = "assets:///video.mp4"
    private var playerUtil: ExoPlayerUtil? = null
    private val apiService = SubscribeApi.retrofitService
    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var isFooterCheckbox: String = "0"
    private var hasJetztSubmit: Boolean = false

    private var formSubmitRunnable: Runnable? = null
    private var formSubmitHandler: Handler = Handler(Looper.getMainLooper())

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
        mBinding.headerMain.tvStrongBuy.setOnClickListener { requireActivity().openStrongBuy() }
        mBinding.btnStrongBuy.setOnClickListener { requireActivity().openStrongBuy() }
        mBinding.btnStrongBuy1.setOnClickListener { requireActivity().openStrongBuy() }

        mBinding.headerMain.tv.setOnClickListener {

            if (disableClick()) {
                mBinding.scrollView.post {
                    mBinding.scrollView.scrollTo(0, mBinding.btnStrongBuy1.bottom)
                }
            }
        }

        mBinding.tvJetztSubmit.setOnClickListener {
            if (disableClick()) {
                if (requireActivity().isOnline()) {
                    val headerEmail = mBinding.edtEmail.text.trim().toString()
                    if (TextUtils.isEmpty(headerEmail)) {
                        mBinding.edtEmail.requestFocus(R.id.edtEmail)
                        mBinding.edtEmail.error = "Bitte E-Mail-Adresse eingeben."
                    } else if (!headerEmail.matches(emailPattern.toRegex())) {
                        mBinding.edtEmail.requestFocus(R.id.edtEmail)
                        mBinding.edtEmail.error = "Bitte geben Sie eine gültige Email Adresse an."
                    } else {
                        mBinding.tvJetztSubmit.isEnabled = false
                        hasJetztSubmit = true
                        callSubscribeApi(headerEmail, "0", hasJetztSubmit)
                    }
                } else {
                    requireActivity().showToast(getString(R.string.not_connected))
                }
            }
        }
        mBinding.footerCheckbox.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, is_checked: Boolean) {
                isFooterCheckbox = if (is_checked) {
                    "1"
                } else {
                    "0"
                }
            }
        })
        mBinding.tvFooterSubmit.setOnClickListener {
            if (requireActivity().isOnline()) {
                if (disableClick()) {
                    val footerEmail = mBinding.edtFooterEmail.text.trim().toString()
                    if (TextUtils.isEmpty(footerEmail)) {
                        mBinding.edtFooterEmail.requestFocus(R.id.edtFooterEmail)
                        mBinding.edtFooterEmail.error = "Bitte E-Mail-Adresse eingeben."
                    } else if (!footerEmail.matches(emailPattern.toRegex())) {
                        mBinding.edtFooterEmail.requestFocus(R.id.edtFooterEmail)
                        mBinding.edtFooterEmail.error = "Bitte geben Sie eine gültige Email Adresse an."
                    } else if (isFooterCheckbox == "0") {
                        requireActivity().showToast("Bitte Checkbox aktivieren")
                    } else {
                        mBinding.tvFooterSubmit.isEnabled = false
                        hasJetztSubmit = false
                        callSubscribeApi(footerEmail, isFooterCheckbox, hasJetztSubmit)
                    }
                }
            } else {
                requireActivity().showToast(getString(R.string.not_connected))
            }
        }
    }

    private fun callSubscribeApi(email: String, is_accept: String, hasJetztSubmit: Boolean) {
        try {
            CoroutineScope(Dispatchers.Main).launch {

                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("is_accept", is_accept)
                    .build()


                apiService.getSubscribeResponse(requestBody)
                    .enqueue(object : Callback<SubscribeResponse> {
                        override fun onResponse(
                            call: Call<SubscribeResponse>,
                            response: Response<SubscribeResponse>
                        ) {
                            if (response.isSuccessful) {
                                val subscribeResponse: SubscribeResponse? = response.body()
                                subscribeResponse?.let { response ->
                                    if (response.status) {
                                        if (hasJetztSubmit) {
                                            mBinding.edtEmail.text.clear()
                                            mBinding.tvJetztSubmit.isEnabled = true
                                            mBinding.tvJetztFormSubmit.visibility = View.VISIBLE
                                            mBinding.tvJetztFormSubmit.text = response.message
                                        } else {
                                            mBinding.edtFooterEmail.text.clear()
                                            mBinding.tvFooterSubmit.isEnabled = true
                                            mBinding.footerCheckbox.isChecked = false
                                            mBinding.tvFooterFormSubmit.visibility = View.VISIBLE
                                            mBinding.tvFooterFormSubmit.text = response.message
                                        }
                                        formSubmitRunnable = Runnable {

                                            if (is_accept == "0") {
                                                mBinding.tvJetztFormSubmit.visibility = View.GONE
                                            } else {
                                                mBinding.tvFooterFormSubmit.visibility = View.GONE
                                            }
                                        }

                                        formSubmitRunnable?.let {
                                            formSubmitHandler.postDelayed(it, 2000)
                                        }
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<SubscribeResponse>, t: Throwable) {
                            Timber.e(t.localizedMessage)
                        }

                    })
            }
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
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

        formSubmitRunnable?.let {
            formSubmitHandler.removeCallbacks(it)
            formSubmitHandler.removeCallbacksAndMessages(null)
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
