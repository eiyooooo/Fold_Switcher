package com.eiyooooo.foldswitcher.helpers

import android.content.pm.PackageManager
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import rikka.shizuku.Shizuku

object ShizukuHelper {
    private val _shizukuStatus: MutableStateFlow<ShizukuStatus?> by lazy { MutableStateFlow(null) }
    val shizukuStatus: Flow<ShizukuStatus?> = _shizukuStatus

    fun setShizukuListener() {
        Shizuku.addBinderReceivedListener {
            if (_shizukuStatus.value == ShizukuStatus.SHIZUKU_NOT_RUNNING) {
                _shizukuStatus.value = ShizukuStatus.SHIZUKU_RUNNING
            }
        }
        Shizuku.addBinderDeadListener {
            _shizukuStatus.value = ShizukuStatus.SHIZUKU_NOT_RUNNING
        }
        Shizuku.addRequestPermissionResultListener { _, grantResult ->
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                _shizukuStatus.value = ShizukuStatus.HAVE_PERMISSION
            }
            else {
                _shizukuStatus.value = ShizukuStatus.NO_PERMISSION
            }
        }
    }

    fun checkShizukuPermission(code: Int) {
        try {
            if (Shizuku.isPreV11()) {
                _shizukuStatus.value = ShizukuStatus.VERSION_NOT_SUPPORT
            }
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                _shizukuStatus.value = ShizukuStatus.HAVE_PERMISSION
            }
            else {
                Shizuku.requestPermission(code)
                _shizukuStatus.value = ShizukuStatus.NO_PERMISSION
            }
        } catch (e: Exception) {
            val msg = e.message
            if (msg != null && msg.contains("binder haven't been received")) {
                _shizukuStatus.value = ShizukuStatus.SHIZUKU_NOT_RUNNING
            }
            else {
                _shizukuStatus.value = ShizukuStatus.ERROR
                //TODO: log(Log.getStackTraceString(e))
            }
        }
    }
}