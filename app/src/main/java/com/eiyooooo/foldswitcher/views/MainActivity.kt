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
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.contentMain.mainViewPager
        val adapter = MainViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 4

        val bottomNavigationView: BottomNavigationView = getBottomNavigationView(viewPager)
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).setChecked(true)
            }
        })

        ShizukuHelper.checkShizukuPermission(0)
    }

    private fun getBottomNavigationView(viewPager: ViewPager2): BottomNavigationView {
        val bottomNavigationView = binding.contentMain.bottomNavigation

        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            val itemId = item.itemId
            when (itemId) {
                R.id.nav_home -> {
                    viewPager.currentItem = 0
                    return@setOnItemSelectedListener true
                }

                R.id.nav_manager -> {
                    viewPager.currentItem = 1
                    return@setOnItemSelectedListener true
                }

                R.id.nav_log -> {
                    viewPager.currentItem = 2
                    return@setOnItemSelectedListener true
                }

                R.id.nav_settings -> {
                    viewPager.currentItem = 3
                    return@setOnItemSelectedListener true
                }

                else -> false
            }
        }
        return bottomNavigationView
    }
}