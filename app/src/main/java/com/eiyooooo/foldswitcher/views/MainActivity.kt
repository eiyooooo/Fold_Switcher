package com.eiyooooo.foldswitcher.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.adapters.MainViewPagerAdapter
import com.eiyooooo.foldswitcher.databinding.ActivityMainBinding
import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
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

        mainModel.getExecutor().currentState.observe(this) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.github -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.setData(Uri.parse("https://github.com/eiyooooo/Fold_Switcher"))
                    startActivity(intent)
                } catch (ignored: Exception) {
                    Toast.makeText(this, getString(R.string.no_browser), Toast.LENGTH_SHORT).show()
                }
                true
            }

            R.id.licenses -> {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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