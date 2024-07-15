package com.eiyooooo.foldswitcher.helpers

import android.content.pm.PackageManager
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import kotlinx.coroutines.flow.MutableStateFlow
import rikka.shizuku.Shizuku

class ShizukuHelper {
    companion object {
        val shizukuStatus: MutableStateFlow<ShizukuStatus?> by lazy { MutableStateFlow(null) }

        fun setShizukuListener() {
            Shizuku.addBinderReceivedListener {
                if (shizukuStatus.value == ShizukuStatus.SHIZUKU_NOT_RUNNING)
                    shizukuStatus.value = ShizukuStatus.SHIZUKU_RUNNING
            }
            Shizuku.addBinderDeadListener {
                shizukuStatus.value = ShizukuStatus.SHIZUKU_NOT_RUNNING
            }
            Shizuku.addRequestPermissionResultListener { _, grantResult ->
                if (grantResult == PackageManager.PERMISSION_GRANTED)
                    shizukuStatus.value = ShizukuStatus.HAVE_PERMISSION
                else
                    shizukuStatus.value = ShizukuStatus.NO_PERMISSION
            }
        }

        fun checkShizukuPermission(code: Int) {
            try {
                if (Shizuku.isPreV11())
                    shizukuStatus.value = ShizukuStatus.VERSION_NOT_SUPPORT
                if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED)
                    shizukuStatus.value = ShizukuStatus.HAVE_PERMISSION
                else {
                    Shizuku.requestPermission(code)
                    shizukuStatus.value = ShizukuStatus.NO_PERMISSION
                }
            } catch (e: Exception) {
                val msg = e.message
                if (msg != null && msg.contains("binder haven't been received"))
                    shizukuStatus.value = ShizukuStatus.SHIZUKU_NOT_RUNNING
                else {
                    shizukuStatus.value = ShizukuStatus.ERROR
                    //TODO: log(Log.getStackTraceString(e))
                }
            }
        }
    }
}