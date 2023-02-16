package com.qwk.chandrsekhar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.qwk.chandrsekhar.R
import com.qwk.chandrsekhar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val  binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup navigation
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

    }
}