package com.eiyooooo.foldswitcher.wrappers

import android.hardware.devicestate.DeviceStateInfo
import android.hardware.devicestate.IDeviceStateManagerCallback
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.eiyooooo.foldswitcher.types.ExecuteMode
import kotlinx.coroutines.flow.MutableStateFlow
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuRemoteProcess

object ShizukuExecutor : Executor {
    private var supportMode: Int = ExecuteMode.MODE_UNKNOWN
    private var mode: Int = ExecuteMode.MODE_UNKNOWN

    override lateinit var supportStates: List<Pair<Int, String>>

    private val _currentState: MutableStateFlow<Int?> by lazy { MutableStateFlow(null) }
    override val currentState: LiveData<Int?> = _currentState.asLiveData()

    override fun getSupportMode(): Int {
        checkAvailability()
        return supportMode
    }

    override fun setMode(mode: Int) {
        if (mode == ExecuteMode.MODE_UNKNOWN) {
            this.mode = ExecuteMode.MODE_UNKNOWN
            availability = false
            DeviceStateManager.resetDeviceStateManager(true)
        } else {
            if (this.mode == ExecuteMode.MODE_UNKNOWN) {
                this.mode = ExecuteMode.MODE_ANY
                checkAvailability()
                if (mode != ExecuteMode.MODE_ANY) {
                    this.mode = mode
                }
            } else if (mode != ExecuteMode.MODE_ANY) {
                this.mode = mode
            }
        }
    }

    private var availability: Boolean = false
    override fun checkAvailability(): Boolean {
        if (mode == ExecuteMode.MODE_UNKNOWN) return false
        if (availability) return true

        supportStates = parseDeviceStates(executeShellCommand("cmd device_state print-states"))
        try {
            if (supportStates.isEmpty()) {
                supportMode = ExecuteMode.MODE_3
                supportStates = DeviceStateManager.getInstance(true).deviceStateInfo.supportedStates?.map { it to "" }?.toList()!!
            } else {
                supportMode = ExecuteMode.MODE_2
            }
            if (supportStates.isEmpty()) {
                throw Exception("no supportStates")
            }
            mode = supportMode

            DeviceStateManager.getInstance(true).registerCallback(object : IDeviceStateManagerCallback.Stub() {
                override fun onDeviceStateInfoChanged(info: DeviceStateInfo) {
                    _currentState.value = info.currentState
                }

                override fun onRequestActive(token: IBinder) {
                }

                override fun onRequestCanceled(token: IBinder) {
                }
            })
        } catch (e: Exception) {
            Log.e("ShizukuExecutor", e.stackTraceToString())
            return availability
        }

        availability = true
        return true
    }

    override fun getCurrentStateOnce(): Int {
        if (checkAvailability()) {
            try {
                if (mode == ExecuteMode.MODE_2) {
                    return executeShellCommand("cmd device_state print-state").trim().toInt()
                } else if (mode == ExecuteMode.MODE_3) {
                    return DeviceStateManager.getInstance(true).deviceStateInfo.currentState
                }
            } catch (e: Exception) {
                return -1
            }
        }
        return -1
    }

    override fun requestState(state: Int): Boolean {
        if (checkAvailability()) {
            if (mode == ExecuteMode.MODE_2) {
                return !executeShellCommand("cmd device_state state $state").contains("Error")
            } else if (mode == ExecuteMode.MODE_3) {
                try {
                    DeviceStateManager.getInstance(true).requestState(state, 0)
                    return true
                } catch (e: Exception) {
                    return false
                }
            }
        }
        return false
    }

    override fun resetState(): Boolean {
        if (checkAvailability()) {
            if (mode == ExecuteMode.MODE_2) {
                return !executeShellCommand("cmd device_state state reset").contains("Error")
            } else if (mode == ExecuteMode.MODE_3) {
                try {
                    DeviceStateManager.getInstance(true).cancelStateRequest()
                    return true
                } catch (e: Exception) {
                    return false
                }
            }
        }
        return false
    }

    override fun parseDeviceStates(input: String): List<Pair<Int, String>> {
        val pattern = Regex("""DeviceState\{identifier=(\d+), name='([^']+)'.*?\}""")
        return pattern.findAll(input).map { matchResult ->
            val (identifier, name) = matchResult.destructured
            identifier.toInt() to name
        }.toList()
    }

    override fun executeShellCommand(command: String): String {
        val process = try {
            Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            ).also { it.isAccessible = true }.invoke(null, arrayOf("sh", "-c", command), null, null)
        } catch (_: Exception) {
        }

        if (process is ShizukuRemoteProcess) {
            val inputStream = process.inputStream
            val result = inputStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                return "Error: Execution failed with exit code: $exitCode"
            }
            return result
        } else {
            return "Error: Execution failed"
        }
    }
}