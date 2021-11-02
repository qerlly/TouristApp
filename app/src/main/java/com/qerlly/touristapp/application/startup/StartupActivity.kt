package com.qerlly.touristapp.application.startup

import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.qerlly.touristapp.application.main.MainActivity
import com.qerlly.touristapp.R
import com.qerlly.touristapp.application.startup.StartupViewModel.AuthenticationStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class StartupActivity : AppCompatActivity() {

    private val startupViewModel: StartupViewModel by viewModels()
    private lateinit var mgr: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*lifecycleScope.launchWhenCreated {
            val state = startupViewModel.isUserSignedIn.first { it != AuthenticationStatus.LOADING }
            if (state == AuthenticationStatus.NOT_AUTHENTICATED) {
                setContentView(R.layout.startup_main)
            } else {
                startMainActivity()
            }
        }*/
        startMainActivity()
        mgr = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}