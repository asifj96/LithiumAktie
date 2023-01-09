package com.asif.lithiumaktie.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding

    private val apiService = SubscribeApi.retrofitService
    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var isFooterCheckbox: String = "0"
    private var hasJetztSubmit: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            lifecycle.addObserver(mBinding.videoView)
            mBinding.videoView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.loadVideo("131AEjj-GGs", 0f)
                }
            })

            MainActivity.activityMainBinding.bottomNav.visibility = View.VISIBLE
            mBinding.headerMain.tvDate.text = "Aktientipp am ${getCurrentDate()}"
            mBinding.tvShareArticle.setOnClickListener {

                if (disableClick()) {

                    requireContext().shareApp()
                }
            }
            mBinding.tvHeaderImageDescription.setOnClickListener {
                if (disableClick()) {
                    val uri = Uri.parse(getString(R.string.header_url))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
            mBinding.headerMain.tvStrongBuy.setOnClickListener {
                if (disableClick()) {
                    val uri = Uri.parse(getString(R.string.strong_buy_url))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
            mBinding.btnStrongBuy.setOnClickListener {
                if (disableClick()) {
                    val uri = Uri.parse(getString(R.string.strong_buy_url))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
            mBinding.btnStrongBuy1.setOnClickListener {
                if (disableClick()) {
                    val uri = Uri.parse(getString(R.string.strong_buy_url))
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
            mBinding.tvKaufWebseiteName.setOnClickListener {
                if (disableClick()) {
                    val uri = Uri.parse("https://arbormetalscorp.com/")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
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
                            mBinding.edtEmail.error =
                                "Bitte geben Sie eine gültige Email Adresse an."
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
        } catch (e: Exception) {
            e.printStackTrace()
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
                                subscribeResponse?.let { genericResponse ->
                                    if (genericResponse.status) {

                                        showAlertDialog(
                                            childFragmentManager,
                                            genericResponse.message
                                        ) {
                                            enableUI(hasJetztSubmit)
                                        }

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

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (isAdded)
            requireActivity().hideKeyboard(mBinding.root)

    }

    override fun onStop() {
        super.onStop()

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
}
