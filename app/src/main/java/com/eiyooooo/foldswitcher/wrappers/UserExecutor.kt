package com.eiyooooo.foldswitcher.wrappers

import android.annotation.SuppressLint
import android.hardware.devicestate.DeviceState
import android.hardware.devicestate.DeviceStateInfo
import android.hardware.devicestate.IDeviceStateManagerCallback
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.eiyooooo.foldswitcher.types.ExecuteMode
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.BufferedReader
import java.io.InputStreamReader

object UserExecutor : Executor {
    override val supportStates: List<Pair<Int, String>> by lazy { parseDeviceStates(executeShellCommand("cmd device_state print-states")) }

    private val _currentState: MutableStateFlow<Int?> by lazy { MutableStateFlow(null) }
    override val currentState: LiveData<Int?> = _currentState.asLiveData()

    override fun getSupportMode(): Int {
        return ExecuteMode.MODE_1
    }

    override fun setMode(mode: Int) {}

    private var availability: Boolean = false
    override fun checkAvailability(): Boolean {
        if (availability) return true
        if (supportStates.isNotEmpty()) {
            try {
                DeviceStateManager.getInstance(false).registerCallback(object : IDeviceStateManagerCallback.Stub() {
                    @SuppressLint("BlockedPrivateApi")
                    override fun onDeviceStateInfoChanged(info: DeviceStateInfo) {
                        val flagsField = info.javaClass.getDeclaredField("currentState")
                        _currentState.value = try {
                            flagsField.get(info) as Int
                        } catch (_: Exception) {
                            val state = flagsField.get(info) as? DeviceState
                            state?.javaClass?.getDeclaredMethod("getIdentifier")?.invoke(state) as? Int ?: -1
                        }
                    }

                    override fun onRequestActive(token: IBinder) {
                    }

                    override fun onRequestCanceled(token: IBinder) {
                    }
                })
            } catch (e: Exception) {
                Log.e("UserExecutor", e.stackTraceToString())
                return availability
            }
            availability = true
        }
        return availability
    }

    override fun getCurrentStateOnce(): Int {
        if (checkAvailability()) {
            return try {
                executeShellCommand("cmd device_state print-state").trim().toInt()
            } catch (e: Exception) {
                -1
            }
        }
        return -1
    }

    override fun requestState(state: Int): Boolean {
        if (checkAvailability()) {
            return !executeShellCommand("cmd device_state state $state").contains("Error")
        }
        return false
    }

    override fun resetState(): Boolean {
        if (checkAvailability()) {
            return !executeShellCommand("cmd device_state state reset").contains("Error")
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
        val output = StringBuilder()

        try {
            val process = ProcessBuilder("sh", "-c", command).start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw Exception("Execution failed with exit code: $exitCode")
            }
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }

        return output.toString()
    }
}