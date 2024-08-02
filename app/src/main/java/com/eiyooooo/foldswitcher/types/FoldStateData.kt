package com.eiyooooo.foldswitcher.types

import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.adapters.MainViewPagerAdapter
import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel

data class FoldStateData(
    val adapter: MainViewPagerAdapter,
    val viewModel: MainActivityViewModel,
    val state: Int,
    val name: String
) {
    val adjustedName: String by lazy {
        var adjustedName = name.replaceSuffix("_STATE", "_MODE")
        adjustedName = when (adjustedName) {
            "CLOSE" -> "CLOSED"
            "OPENED" -> "OPEN"
            "HALF_CLOSED" -> "HALF_OPEN"
            "HALF_FOLDED" -> "HALF_OPEN"
            "HALF_OPENED" -> "HALF_OPEN"
            "CONCURRENT_INNER_DEFAULT", "DUAL" -> "DUAL_DISPLAY_MODE"
            "REAR_DUAL" -> "REAR_DUAL_MODE"
            else -> adjustedName
        }
        if (adjustedName.startsWith("HALF_")) {
            adjustedName = "HALF-" + adjustedName.substring("HALF_".length)
        }
        adjustedName.replace('_', ' ').uppercase()
    }
    val adjustedNameId: Int? by lazy { getAdjustedNameId(adjustedName) }
    val iconId: Int by lazy { getIconId(this) }
}

private fun String.replaceSuffix(old: String, new: String): String {
    return if (endsWith(old)) substring(0, length - old.length) + new else this
}

internal fun getIconId(adjustedName: String): Int {
    return FOLDING_STATE_ICONS[adjustedName] ?: R.drawable.foldable_phone
}

internal fun getIconId(foldStateData: FoldStateData): Int {
    return if (foldStateData.state == -1) R.drawable.reset
    else FOLDING_STATE_ICONS[foldStateData.adjustedName] ?: R.drawable.foldable_phone
}

internal fun getAdjustedNameId(adjustedName: String): Int? {
    return FOLDING_STATE_NAMES[adjustedName]
}

private val FOLDING_STATE_NAMES = mapOf(
    "CLOSED" to R.string.posture_closed,
    "DUAL DISPLAY MODE" to R.string.posture_dual_display,
    "FLIPPED" to R.string.posture_flipped,
    "HALF-OPEN" to R.string.posture_half_folded,
    "OPEN" to R.string.posture_open,
    "REAR DISPLAY MODE" to R.string.posture_rear_display,
    "TENT" to R.string.posture_tent,
)

private val FOLDING_STATE_ICONS = mapOf(
    "CLOSED" to R.drawable.posture_closed,
    "DUAL DISPLAY MODE" to R.drawable.posture_dual_display,
    "FLIPPED" to R.drawable.posture_flipped,
    "HALF-OPEN" to R.drawable.posture_half_folded,
    "OPEN" to R.drawable.posture_open,
    "REAR DISPLAY MODE" to R.drawable.posture_rear_display,
    "TENT" to R.drawable.posture_tent,
)