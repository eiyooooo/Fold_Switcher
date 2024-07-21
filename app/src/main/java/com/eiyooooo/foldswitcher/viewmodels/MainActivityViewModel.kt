package com.eiyooooo.foldswitcher.viewmodels

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

class MainActivityViewModel : ViewModel() {
    private val _shizukuStatus: MutableStateFlow<ShizukuStatus?> by lazy { MutableStateFlow(null) }
    val shizukuStatus: Flow<ShizukuStatus?> = _shizukuStatus

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        checkShizukuPermission()
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        checkShizukuPermission()
    }

    private val requestPermissionResultListener = Shizuku.OnRequestPermissionResultListener { _, grantResult ->
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            _shizukuStatus.value = ShizukuStatus.HAVE_PERMISSION
        } else {
            _shizukuStatus.value = ShizukuStatus.NO_PERMISSION
        }
    }

    fun addShizukuListener() {
        Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
        Shizuku.addBinderDeadListener(binderDeadListener)
        Shizuku.addRequestPermissionResultListener(requestPermissionResultListener)
    }

    fun removeShizukuListener() {
        Shizuku.removeBinderReceivedListener(binderReceivedListener)
        Shizuku.removeBinderDeadListener(binderDeadListener)
        Shizuku.removeRequestPermissionResultListener(requestPermissionResultListener)
    }

    fun checkShizukuPermission() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!Shizuku.pingBinder()) {
                _shizukuStatus.value = ShizukuStatus.SHIZUKU_NOT_RUNNING
                return@launch
            }
            if (Shizuku.isPreV11()) {
                _shizukuStatus.value = ShizukuStatus.VERSION_NOT_SUPPORT
            }
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                _shizukuStatus.value = ShizukuStatus.HAVE_PERMISSION
            } else {
                _shizukuStatus.value = ShizukuStatus.NO_PERMISSION
            }
        }
    }
}