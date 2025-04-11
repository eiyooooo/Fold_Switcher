package com.eiyooooo.foldswitcher

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.eiyooooo.foldswitcher.types.ExecuteMode
import com.eiyooooo.foldswitcher.wrappers.ShizukuExecutor
import com.eiyooooo.foldswitcher.wrappers.UserExecutor
import rikka.shizuku.Shizuku

class ReceiverActivity : Activity() {
    companion object {
        const val EXTRA_STATE_ID = "state_id"
        private const val TAG = "FoldSwitcherReceiver"
    }

    @Suppress("DEPRECATION")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }

        val stateId = intent?.getIntExtra(EXTRA_STATE_ID, Int.MIN_VALUE) ?: Int.MIN_VALUE

        when {
            stateId == -1 -> {
                resetDeviceState()
            }

            stateId >= 0 -> {
                setDeviceState(stateId)
            }

            else -> {
                Toast.makeText(this, getString(R.string.no_state_id), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "No valid state ID provided: $stateId")
            }
        }

        finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
    }

    private fun setDeviceState(stateId: Int) {
        if (UserExecutor.checkAvailability()) {
            UserExecutor.requestState(stateId)
            Log.d(TAG, "Requested state $stateId via UserExecutor")
        } else {
            if (Shizuku.pingBinder() && !Shizuku.isPreV11() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                ShizukuExecutor.setMode(ExecuteMode.MODE_ANY)
                ShizukuExecutor.requestState(stateId)
                Log.d(TAG, "Requested state $stateId via ShizukuExecutor")
            } else {
                Toast.makeText(this, getString(R.string.no_executor_available), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "No available executor to set device state")
            }
        }
    }

    private fun resetDeviceState() {
        if (UserExecutor.checkAvailability()) {
            UserExecutor.resetState()
            Log.d(TAG, "Reset state via UserExecutor")
        } else {
            if (Shizuku.pingBinder() && !Shizuku.isPreV11() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                ShizukuExecutor.setMode(ExecuteMode.MODE_ANY)
                ShizukuExecutor.resetState()
                Log.d(TAG, "Reset state via ShizukuExecutor")
            } else {
                Toast.makeText(this, getString(R.string.no_executor_available), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "No available executor to reset device state")
            }
        }
    }
}
