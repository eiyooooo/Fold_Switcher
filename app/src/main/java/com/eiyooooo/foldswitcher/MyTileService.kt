package com.eiyooooo.foldswitcher

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.eiyooooo.foldswitcher.helpers.SharedPreferencesHelper
import com.eiyooooo.foldswitcher.views.MainActivity
import com.eiyooooo.foldswitcher.wrappers.Executor
import com.eiyooooo.foldswitcher.wrappers.ShizukuExecutor
import com.eiyooooo.foldswitcher.wrappers.UserExecutor
import rikka.shizuku.Shizuku

class MyTileService : TileService() {
    private val quickSwitchName get() = SharedPreferencesHelper.getString("quickSwitchName", "")
    private val quickSwitchState get() = SharedPreferencesHelper.getInt("quickSwitchState", -1)

    override fun onStartListening() {
        super.onStartListening()
        val tile = qsTile
        tile.state = Tile.STATE_INACTIVE
        runWithExecutor {
            val currentState = it.getCurrentStateOnce()
            if (currentState != -1 && quickSwitchState == currentState) {
                tile.state = Tile.STATE_ACTIVE
            }
        }
        tile.label = quickSwitchName.ifEmpty { getString(R.string.no_quick_switch) }
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val tile = qsTile
        val state = quickSwitchState
        if (tile.state == Tile.STATE_INACTIVE) {
            if (state != -1) {
                runWithExecutor {
                    if (it.requestState(state)) {
                        tile.state = Tile.STATE_ACTIVE
                    }
                }
            } else {
                startMainActivity()
            }
        } else if (tile.state == Tile.STATE_ACTIVE) {
            runWithExecutor {
                if (it.resetState()) {
                    val currentState = it.getCurrentStateOnce()
                    if (currentState != -1 && state == currentState) {
                        tile.state = Tile.STATE_ACTIVE
                    } else {
                        tile.state = Tile.STATE_INACTIVE
                    }
                }
            }
        }
        tile.updateTile()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("StartActivityAndCollapseDeprecated")
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setPackage(packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            startActivityAndCollapse(pendingIntent)
        } else {
            startActivityAndCollapse(intent)
        }
    }

    private fun runWithExecutor(function: (Executor) -> Unit) {
        if (UserExecutor.checkAvailability()) {
            function(UserExecutor)
        } else {
            if (Shizuku.pingBinder() && !Shizuku.isPreV11() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                ShizukuExecutor.setStatus(true)
                function(ShizukuExecutor)
            } else {
                startMainActivity()
            }
        }
    }
}