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
import com.eiyooooo.foldswitcher.wrappers.ShizukuExecutor
import com.eiyooooo.foldswitcher.wrappers.UserExecutor
import rikka.shizuku.Shizuku

class MyTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()
        val tile = qsTile
        tile.state = Tile.STATE_INACTIVE

        if (UserExecutor.checkAvailability()) {
            val currentState = UserExecutor.getCurrentStateOnce()
            if (currentState != -1 && SharedPreferencesHelper.getInt("quickSwitchState", -1) == currentState) {
                tile.state = Tile.STATE_ACTIVE
            }
        } else {
            if (checkShizukuPermission()) {
                ShizukuExecutor.setStatus(true)
                val currentState = ShizukuExecutor.getCurrentStateOnce()
                if (currentState != -1 && SharedPreferencesHelper.getInt("quickSwitchState", -1) == currentState) {
                    tile.state = Tile.STATE_ACTIVE
                }
            } else {
                startMainActivity()
            }
        }

        tile.label = SharedPreferencesHelper.getString("quickSwitchName").ifEmpty { getString(R.string.no_quick_switch) }
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val tile = qsTile
        val state = SharedPreferencesHelper.getInt("quickSwitchState", -1)
        if (tile.state == Tile.STATE_INACTIVE) {
            if (state != -1) {
                if (UserExecutor.checkAvailability()) {
                    if (UserExecutor.requestState(state)) {
                        tile.state = Tile.STATE_ACTIVE
                    }
                } else {
                    if (checkShizukuPermission()) {
                        ShizukuExecutor.setStatus(true)
                        if (ShizukuExecutor.requestState(state)) {
                            tile.state = Tile.STATE_ACTIVE
                        }
                    } else {
                        startMainActivity()
                    }
                }
            } else {
                startMainActivity()
            }
        } else if (tile.state == Tile.STATE_ACTIVE) {
            if (UserExecutor.checkAvailability()) {
                if (UserExecutor.resetState()) {
                    val currentState = UserExecutor.getCurrentStateOnce()
                    if (currentState != -1 && state == currentState) {
                        tile.state = Tile.STATE_ACTIVE
                    } else {
                        tile.state = Tile.STATE_INACTIVE
                    }
                }
            } else {
                if (checkShizukuPermission()) {
                    ShizukuExecutor.setStatus(true)
                    if (ShizukuExecutor.resetState()) {
                        val currentState = ShizukuExecutor.getCurrentStateOnce()
                        if (currentState != -1 && state == currentState) {
                            tile.state = Tile.STATE_ACTIVE
                        } else {
                            tile.state = Tile.STATE_INACTIVE
                        }
                    }
                } else {
                    startMainActivity()
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

    private fun checkShizukuPermission(): Boolean {
        if (!Shizuku.pingBinder()) {
            return false
        }
        if (Shizuku.isPreV11()) {
            return false
        }
        return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    }
}