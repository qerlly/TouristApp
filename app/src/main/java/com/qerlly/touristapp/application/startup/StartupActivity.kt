package com.qerlly.touristapp.application.startup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.qerlly.touristapp.R
import com.qerlly.touristapp.application.main.MainActivity
import com.qerlly.touristapp.databinding.StartupMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartupActivity : AppCompatActivity() {

    private lateinit var binding: StartupMainBinding

    private val startupViewModel: StartupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
           /* val state = startupViewModel.isUserSignedIn.first { it != AuthenticationStatus.LOADING }
            if (state == AuthenticationStatus.NOT_AUTHENTICATED) {*/
            if(true) {
                inflateUI()
            } else {
                startMainActivity()
            }
        }
    }

    private fun inflateUI() {
        binding = StartupMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        val fc = supportFragmentManager.findFragmentById(R.id.startup_fragment_host) as NavHostFragment
        return fc.navController.navigateUp()
    }

    fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}