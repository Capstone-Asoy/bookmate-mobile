package com.example.bookmate.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookmate.R
import com.example.bookmate.databinding.ActivityMainBinding
import com.example.bookmate.ui.welcome.WelcomeActivity
import com.example.bookmate.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        viewModel = obtainViewModel(this@MainActivity)
        checkIsLogin()
        setupBottomNav()
    }

    private fun checkIsLogin() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupBottomNav() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val radius = resources.getDimension(R.dimen.bottom_navigation_radius)
                outline.setRoundRect(0, 0, view.width, (view.height + radius).toInt(), radius)
            }
        }
        navView.clipToOutline = false
        navView.elevation = 20f

        navView.setupWithNavController(navController)
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(this@MainActivity)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finishAffinity()
    }
}