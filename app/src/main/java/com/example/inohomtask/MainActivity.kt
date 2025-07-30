package com.example.inohomtask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.inohomtask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeSystemBarsTransparent()
        setupNavController()
    }

    /**
     * Makes the status bar and navigation bar fully transparent.
     */
    private fun makeSystemBarsTransparent() {
        window.statusBarColor = getColor(R.color.black)
        window.navigationBarColor = getColor(R.color.black)
    }

    /**
     * Initializes the NavController from the NavHostFragment.
     * Required for handling in-app navigation.
     */
    private fun setupNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
    }
}