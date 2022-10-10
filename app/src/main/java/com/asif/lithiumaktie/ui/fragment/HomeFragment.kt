package com.asif.lithiumaktie.ui.fragment

import android.os.Bundle
import android.text.TextUtils
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
import com.asif.lithiumaktie.model.SubscribeResponse
import com.asif.lithiumaktie.network.SubscribeApi
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
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


class HomeFragment : Fragment()/*, Player.EventListener */ {

    private lateinit var mBinding: FragmentHomeBinding

    private val apiService = SubscribeApi.retrofitService
    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var isFooterCheckbox: String = "0"
    private var hasJetztSubmit: Boolean = false

//    private var formSubmitRunnable: Runnable? = null
//    private var formSubmitHandler: Handler = Handler(Looper.getMainLooper())

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

        lifecycle.addObserver(mBinding.videoView)
/*        val iFramePlayerOptions: IFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .build()*/
        mBinding.videoView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo("Zz5ZXmhJJTs", 0f)
            }
        })

//        mBinding.videoView.enableAutomaticInitialization = false

        /* val dataUrl = "<html>" +
                 "<body>" +
                 "<h2>Video From YouTube</h2>" +
                 "<br>" +
                 "<iframe width=\"560\" height=\"315\" src=\"" + myYouTubeVideoUrl + "\" frameborder=\"0\" allowfullscreen/>" +
                 "</body>" +
                 "</html>"*/


/*        mBinding.videoView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(myYouTubeVideoUrl)
                }
                return true
            }
        }
        mBinding.videoView.settings.javaScriptEnabled = true
        mBinding.videoView.loadUrl(myYouTubeVideoUrl)*/

        /*   playerUtil = ExoPlayerUtil.getInstance(
               requireContext(),
               mBinding.videoView,
               ExoPlayerUtil.LoadingMode.ASSETS,
               this
           )*/

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
        mBinding.tvKaufWebseiteName.setOnClickListener { requireActivity().openUrl(getString(R.string.kauf_webseite_name)) }

        mBinding.headerMain.tv.setOnClickListener {

            if (disableClick()) {
                mBinding.scrollView.post {
                    mBinding.scrollView.scrollTo(0, mBinding.btnStrongBuy1.bottom)
                }
            }
        }

        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              mBinding.scrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
                  override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                     if(disableClick())
                          requireActivity().hideKeyboard(mBinding.root)
                  }

              })
          }*/
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
                        mBinding.edtFooterEmail.error =
                            "Bitte geben Sie eine gültige Email Adresse an."
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
            if (isAdded)
                requireActivity().hideKeyboard(mBinding.root)
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

                                        showAlertDialog(childFragmentManager, response.message) {
                                            enableUI(hasJetztSubmit)
                                        }
                                        /*formSubmitRunnable = Runnable {

                                            if (is_accept == "0") {
                                                mBinding.tvJetztFormSubmit.visibility = View.GONE
                                            } else {
                                                mBinding.tvFooterFormSubmit.visibility = View.GONE
                                            }
                                        }

                                        formSubmitRunnable?.let {
                                            formSubmitHandler.postDelayed(it, 2000)
                                        }*/
                                    }
                                }
                            } else {
                                showAlertDialog(childFragmentManager, response.message()) {
                                    enableUI(hasJetztSubmit)
                                }
                            }
                        }

                        override fun onFailure(call: Call<SubscribeResponse>, t: Throwable) {
                            Timber.e(t.localizedMessage)
                            showAlertDialog(childFragmentManager, t.message.toString()) {
                                enableUI(hasJetztSubmit)
                            }
                        }

                    })
            }
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
            showAlertDialog(childFragmentManager, e.message.toString()) {
                enableUI(hasJetztSubmit)
            }
        }
    }

    private fun enableUI(hasJetztSubmit: Boolean) {
        if (hasJetztSubmit) {
            requireActivity().hideKeyboard(mBinding.edtEmail.rootView)
            mBinding.edtEmail.text.clear()
            mBinding.tvJetztSubmit.isEnabled = true
        } else {
            requireActivity().hideKeyboard(mBinding.edtFooterEmail.rootView)
            mBinding.edtFooterEmail.text.clear()
            mBinding.tvFooterSubmit.isEnabled = true
            mBinding.footerCheckbox.isChecked = false
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

        /* try {
 //            if (Util.SDK_INT <= 23 || playerUtil == null) {
             playerUtil?.initialize(resValue)
             Timber.e(resValue)
 //                playerUtil?.play()
 //            }
         } catch (e: Exception) {
             Timber.e(e.localizedMessage)
         }*/

    }

    override fun onPause() {
        super.onPause()
        if (isAdded)
            requireActivity().hideKeyboard(mBinding.root)

        /* if (Util.SDK_INT <= 23) {
             playerUtil?.release()
         }*/

        /*   formSubmitRunnable?.let {
               formSubmitHandler.removeCallbacks(it)
               formSubmitHandler.removeCallbacksAndMessages(null)
           }*/

    }

    override fun onStop() {
        super.onStop()
        /* if (Util.SDK_INT > 23) {
             playerUtil?.release()
         }*/
    }

/*    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)

    }*/

    /*override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
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
    }*/
}
