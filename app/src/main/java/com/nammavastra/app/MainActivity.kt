package com.nammavastra.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nammavastra.app.utils.NetworkMonitor
import com.nammavastra.app.utils.SnackbarHelper

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var networkMonitor: NetworkMonitor
    private var wasOffline = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
        
        // Premium Bottom Navigation Scale Animation
        bottomNav.setOnItemSelectedListener { item ->
            val view = bottomNav.findViewById<View>(item.itemId)
            view?.let {
                android.animation.ObjectAnimator.ofPropertyValuesHolder(
                    it,
                    android.animation.PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f, 1f),
                    android.animation.PropertyValuesHolder.ofFloat("scaleY", 1f, 1.2f, 1f)
                ).apply {
                    duration = 300
                    start()
                }
            }
            androidx.navigation.ui.NavigationUI.onNavDestinationSelected(item, navController)
            true
        }
        
        // Setup Network Monitoring
        networkMonitor = NetworkMonitor(this)
        val rootView = findViewById<View>(android.R.id.content)
        
        networkMonitor.observe(this) { isConnected ->
            if (!isConnected) {
                wasOffline = true
                SnackbarHelper.showOfflineSnackbar(rootView)
            } else if (wasOffline) {
                wasOffline = false
                SnackbarHelper.showOnlineSnackbar(rootView)
            }
        }
    }
}
