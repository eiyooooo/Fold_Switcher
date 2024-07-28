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

    private val mainModel: MainActivityViewModel by viewModels()
    private val adapter by lazy { MainViewPagerAdapter(this, mainModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (mainModel.useShizukuExecutor) {
            mainModel.shizukuStatus.observe(this) {
                adapter.updateData(it)
            }
            adapter.updateData(mainModel.shizukuStatus.value)
        } else {
            adapter.updateData()
        }

        mainModel.executor.value.currentState.observe(this) {
            if (mainModel.useShizukuExecutor) adapter.updateData(mainModel.shizukuStatus.value)
            else adapter.updateData()
        }

        val recyclerView = binding.contentMain.list
        recyclerView.adapter = adapter
        recyclerView.fixEdgeEffect()
        recyclerView.addItemSpacing(top = 4f, bottom = 4f, unit = TypedValue.COMPLEX_UNIT_DIP)
        recyclerView.addEdgeSpacing(top = 4f, bottom = 4f, left = 16f, right = 16f, unit = TypedValue.COMPLEX_UNIT_DIP)

        if (mainModel.useShizukuExecutor) mainModel.addShizukuListener()
    }

    override fun onResume() {
        super.onResume()
        if (mainModel.useShizukuExecutor) mainModel.checkShizukuPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mainModel.useShizukuExecutor) mainModel.removeShizukuListener()
    }
}