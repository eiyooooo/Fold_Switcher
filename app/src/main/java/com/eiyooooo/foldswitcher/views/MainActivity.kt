package com.eiyooooo.foldswitcher.views

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.adapters.MainViewPagerAdapter
import com.eiyooooo.foldswitcher.databinding.ActivityMainBinding
import com.eiyooooo.foldswitcher.helpers.ShizukuHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter by lazy { MainViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.contentMain.mainViewPager
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 4

        val isCompactScreen = resources.configuration.screenWidthDp < 600
        if (isCompactScreen) setupBottomNavigationView(viewPager)
        else setupNavigationRail(viewPager)

        ShizukuHelper.checkShizukuPermission(0)
    }

    private fun setupBottomNavigationView(viewPager: ViewPager2) {
        val bottomNavigationView = binding.contentMain.bottomNavigation
        if (bottomNavigationView == null) {
            setupNavigationRail(viewPager)
            return
        }

        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> viewPager.currentItem = 0
                R.id.nav_manager -> viewPager.currentItem = 1
                R.id.nav_log -> viewPager.currentItem = 2
                R.id.nav_settings -> viewPager.currentItem = 3
            }
            true
        }

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
    }

    private fun setupNavigationRail(viewPager: ViewPager2) {
        val navigationRailView = binding.contentMain.navigationRail
        if (navigationRailView == null) {
            setupBottomNavigationView(viewPager)
            return
        }

        navigationRailView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> viewPager.currentItem = 0
                R.id.nav_manager -> viewPager.currentItem = 1
                R.id.nav_log -> viewPager.currentItem = 2
                R.id.nav_settings -> viewPager.currentItem = 3
            }
            true
        }

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                navigationRailView.menu.getItem(position).isChecked = true
            }
        })
    }
}
