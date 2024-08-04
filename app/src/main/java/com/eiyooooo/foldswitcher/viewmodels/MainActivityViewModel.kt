package com.eiyooooo.foldswitcher.viewmodels

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.eiyooooo.foldswitcher.types.ExecuteMode
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import com.eiyooooo.foldswitcher.wrappers.Executor
import com.eiyooooo.foldswitcher.wrappers.ShizukuExecutor
import com.eiyooooo.foldswitcher.wrappers.UserExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

class MainActivityViewModel : ViewModel() {
    companion object {
        private var checkShizukuPermissionJob: Job? = null
    }

    val userExecutorAvailability = UserExecutor.checkAvailability()
    fun getExecutor(): Executor {
        if (_executeMode.value == ExecuteMode.MODE_UNKNOWN) {
            when {
                userExecutorAvailability -> {
                    ExecuteMode.supportModeLevel = ExecuteMode.MODE_1
                    _executeMode.value = ExecuteMode.MODE_1
                }

                ShizukuExecutor.getSupportMode() == ExecuteMode.MODE_2 -> {
                    ExecuteMode.supportModeLevel = ExecuteMode.MODE_2
                    _executeMode.value = ExecuteMode.MODE_2
                    ShizukuExecutor.setMode(ExecuteMode.MODE_2)
                }

                ShizukuExecutor.getSupportMode() == ExecuteMode.MODE_3 -> {
                    ExecuteMode.supportModeLevel = ExecuteMode.MODE_3
                    _executeMode.value = ExecuteMode.MODE_3
                    ShizukuExecutor.setMode(ExecuteMode.MODE_3)
                }
            }
        }
        return if (_executeMode.value == ExecuteMode.MODE_1) UserExecutor else ShizukuExecutor
    }

    private val _executeMode: MutableStateFlow<Int> by lazy { MutableStateFlow(ExecuteMode.MODE_UNKNOWN) }
    val executeMode: LiveData<Int> = _executeMode.asLiveData()

    fun setExecuteMode(mode: Int) {
        _executeMode.value = mode
    }

    private val _shizukuStatus: MutableStateFlow<ShizukuStatus?> by lazy { MutableStateFlow(null) }
    val shizukuStatus: LiveData<ShizukuStatus?> = _shizukuStatus.asLiveData()

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

    fun stopCheckingShizukuPermission() {
        checkShizukuPermissionJob?.cancel()
        checkShizukuPermissionJob = null
    }

    fun startCheckingShizukuPermission() {
        checkShizukuPermissionJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                checkShizukuPermission()
            }
        }
    }
}