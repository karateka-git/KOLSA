package com.example.kolsa.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.kolsa.databinding.ActivityMainBinding
import com.example.kolsa.presentation.util.ext.onDestroyNullable


class MainActivity : AppCompatActivity() {
    private var binding by onDestroyNullable<ActivityMainBinding>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = binding.navHostFragment.getFragment<NavHostFragment>().navController

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController.popBackStack().not())
                        finish()
                }
            }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}
