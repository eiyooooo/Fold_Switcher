package com.eiyooooo.foldswitcher.views

import android.os.Bundle
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.eiyooooo.foldswitcher.adapters.MainViewPagerAdapter
import com.eiyooooo.foldswitcher.databinding.ActivityMainBinding
import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel
import rikka.recyclerview.addEdgeSpacing
import rikka.recyclerview.addItemSpacing
import rikka.recyclerview.fixEdgeEffect

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainModel by viewModels<MainActivityViewModel>()
    private val adapter by lazy { MainViewPagerAdapter(mainModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainModel.shizukuStatus.observe(this) {
            adapter.updateData(it)
        }
        adapter.updateData(mainModel.shizukuStatus.value)

        val recyclerView = binding.contentMain.list
        recyclerView.adapter = adapter
        recyclerView.fixEdgeEffect()
        recyclerView.addItemSpacing(top = 4f, bottom = 4f, unit = TypedValue.COMPLEX_UNIT_DIP)
        recyclerView.addEdgeSpacing(top = 4f, bottom = 4f, left = 16f, right = 16f, unit = TypedValue.COMPLEX_UNIT_DIP)

        mainModel.addShizukuListener()
    }

    override fun onResume() {
        super.onResume()
        mainModel.checkShizukuPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainModel.removeShizukuListener()
    }
}
