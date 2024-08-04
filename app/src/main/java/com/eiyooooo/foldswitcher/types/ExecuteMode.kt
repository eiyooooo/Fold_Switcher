package com.eiyooooo.foldswitcher.types

object ExecuteMode {
    /**
     * execute with any available mode
     */
    const val MODE_ANY: Int = -1

    /**
     * unknown or no permission
     */
    const val MODE_UNKNOWN: Int = 0

    /**
     * UserExecutor mode
     */
    const val MODE_1: Int = 1

    /**
     * ShizukuExecutor command mode
     */
    const val MODE_2: Int = 2

    /**
     * ShizukuExecutor manager mode
     */
    const val MODE_3: Int = 3

    var supportModeLevel: Int = MODE_UNKNOWN
}