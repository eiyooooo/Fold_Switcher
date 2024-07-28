package com.eiyooooo.foldswitcher.wrappers

import androidx.lifecycle.LiveData

interface Executor {
    val currentState: LiveData<Int?>

    val supportStates: List<Pair<Int, String>>

    fun setStatus(status: Boolean)

    fun checkAvailability(): Boolean

    fun requestState(state: Int)

    fun resetState()

    fun parseDeviceStates(input: String): List<Pair<Int, String>>

    fun executeShellCommand(command: String): String
}