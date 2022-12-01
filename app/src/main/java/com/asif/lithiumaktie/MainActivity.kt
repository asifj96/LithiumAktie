package com.asif.lithiumaktie

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.asif.lithiumaktie.common.showToast
import com.asif.lithiumaktie.databinding.ActivityMainBinding
import com.asif.lithiumaktie.utility.PreferenceData.getLanguageCode
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {


    private var doubleBackToExitPressedOnce = false

    private val navHost: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
    }
    private val navController: NavController by lazy {
        navHost.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Companion.activityMainBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, container, false)
        setContentView(Companion.activityMainBinding.root)

        Companion.activityMainBinding.bottomNav.itemIconTintList = null
        Companion.activityMainBinding.bottomNav.setupWithNavController(navController)

        Companion.activityMainBinding.bottomNav.setOnItemSelectedListener { item ->
            val navOption =
                NavOptions.Builder().setPopUpTo(navController.currentDestination?.id!!, true)
                    .build()
            when (item.itemId) {
                R.id.impressumFragment -> {
                    if (navController.currentDestination?.id != R.id.impressumFragment) {
                        navController.navigate(R.id.impressumFragment, null, navOption)
                    }
                }
                R.id.homeFragment -> {
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.navigate(R.id.homeFragment, null, navOption)
                    }
                }
                R.id.datenschutzerklarungFragment -> {
                    if (navController.currentDestination?.id != R.id.datenschutzerklarungFragment) {
                        navController.navigate(R.id.datenschutzerklarungFragment, null, navOption)
                    }
                }
            }
            true
        }
        checkLanguage()
    }
    private fun checkLanguage() {
        val myLocale = Locale(getLanguageCode())
        val res: Resources = this.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }
    override fun onBackPressed() {

        if (navController.currentDestination?.id != R.id.impressumFragment
        ) {
            val navOption = NavOptions.Builder()
                .setPopUpTo(navController.currentDestination?.id!!, true).build()
            navController.navigate(R.id.impressumFragment, null, navOption)
            bottomNav.menu.getItem(0).isChecked = true

        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                finish()
                return
            }
            doubleBackToExitPressedOnce = true
            val str = getString(R.string.click_back_again_to_exit)
            showToast(str)

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    companion object {
        lateinit var activityMainBinding: ActivityMainBinding
    }
}