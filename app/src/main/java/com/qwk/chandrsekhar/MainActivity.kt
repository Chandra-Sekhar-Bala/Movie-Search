package com.qwk.chandrsekhar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.qwk.chandrsekhar.R
import com.qwk.chandrsekhar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
       val  binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup navigation
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        // Hide navigation bar while showing details
        navController.addOnDestinationChangedListener{_, dest, _ ->
//            if(dest.id == R.id.movieDetailsFragment){
                supportActionBar?.hide()
//            }
        }
    }
}