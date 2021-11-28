package com.qerlly.touristapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.qerlly.touristapp.R
import com.qerlly.touristapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = (supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_roadmap,
                R.id.navigation_tour,
                R.id.navigation_chat
            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.setupToolbar(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val fc = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return fc.navController.navigateUp()
    }

    private fun ActivityMainBinding.setupToolbar(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navButtonsContainer.visibility =
                if (destination.id == R.id.navigation_pref ||
                    destination.id == R.id.navigation_faq
                )
                    View.GONE
                else
                    View.VISIBLE
        }

        faqButton.setOnClickListener { navController.navigate(R.id.navigation_faq) }
        prefButton.setOnClickListener { navController.navigate(R.id.navigation_pref) }
    }
}