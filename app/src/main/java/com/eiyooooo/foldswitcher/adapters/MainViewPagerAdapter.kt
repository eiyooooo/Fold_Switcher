package com.eiyooooo.foldswitcher.adapters

import android.annotation.SuppressLint
import android.app.StatusBarManager
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.types.ExecuteMode
import com.eiyooooo.foldswitcher.types.FoldStateData
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel
import com.eiyooooo.foldswitcher.views.FoldStateViewHolder
import com.eiyooooo.foldswitcher.views.InstructionQuickSwitchViewHolder
import com.eiyooooo.foldswitcher.views.InstructionViewHolder
import com.eiyooooo.foldswitcher.views.SwitchModeViewHolder
import com.eiyooooo.foldswitcher.views.MainActivity
import com.eiyooooo.foldswitcher.views.ShizukuInstructionViewHolder
import com.eiyooooo.foldswitcher.views.ShizukuRequestViewHolder
import com.eiyooooo.foldswitcher.views.ShizukuStatusViewHolder
import com.eiyooooo.foldswitcher.views.StatusViewHolder
import com.eiyooooo.foldswitcher.wrappers.ShizukuExecutor
import rikka.recyclerview.IdBasedRecyclerViewAdapter
import rikka.recyclerview.IndexCreatorPool

class MainViewPagerAdapter(private val mainActivity: MainActivity, private val mainModel: MainActivityViewModel) : IdBasedRecyclerViewAdapter(ArrayList()) {
    companion object {
        private const val ID_STATUS = 0L
        private const val ID_SHIZUKU_INSTRUCTION = 1L
        private const val ID_SHIZUKU_REQUEST = 2L
        private const val ID_SWITCH_MODE = 3L
        private const val ID_INSTRUCTION = 4L
        private const val ID_INSTRUCTION_QUICK_SWITCH = 5L
        private const val ID_RESET_STATE = 6L
        private val idList = mutableListOf<Pair<Int, Long>>()
    }

    val statusBarManager: StatusBarManager = mainActivity.getSystemService(StatusBarManager::class.java)

    init {
        setHasStableIds(true)
    }

    override fun onCreateCreatorPool(): IndexCreatorPool {
        return IndexCreatorPool()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(shizukuStatus: ShizukuStatus?) {
        mainModel.stopCheckingShizukuPermission()
        clear()
        shizukuStatus?.let { ShizukuExecutor.setMode(if (it == ShizukuStatus.HAVE_PERMISSION) ExecuteMode.MODE_ANY else ExecuteMode.MODE_UNKNOWN) }

        if (mainModel.executeMode.value == ExecuteMode.MODE_1) {
            addItem(StatusViewHolder.CREATOR, shizukuStatus, ID_STATUS)
        } else {
            addItem(ShizukuStatusViewHolder.CREATOR, shizukuStatus, ID_STATUS)
        }
        addItem(SwitchModeViewHolder.CREATOR, mainModel, ID_SWITCH_MODE)

        if (mainModel.executeMode.value == ExecuteMode.MODE_1) {
            addStates()
        } else {
            if (shizukuStatus == ShizukuStatus.HAVE_PERMISSION) {
                addStates()
            } else {
                addItem(ShizukuInstructionViewHolder.CREATOR, null, ID_SHIZUKU_INSTRUCTION)
                if (shizukuStatus != ShizukuStatus.SHIZUKU_NOT_RUNNING) {
                    addItem(ShizukuRequestViewHolder.CREATOR, null, ID_SHIZUKU_REQUEST)
                    mainModel.startCheckingShizukuPermission()
                }
            }
        }

        notifyDataSetChanged()
    }

    private fun addStates() {
        addItem(InstructionViewHolder.CREATOR, null, ID_INSTRUCTION)
        addItem(InstructionQuickSwitchViewHolder.CREATOR, null, ID_INSTRUCTION_QUICK_SWITCH)
        addItem(FoldStateViewHolder.CREATOR, FoldStateData(this, mainModel, -1, mainActivity.getString(R.string.state_reset)), ID_RESET_STATE)
        var id = 7L
        mainModel.getExecutor().supportStates.forEach { pair ->
            val name = pair.second.ifEmpty { pair.first.toString() }
            addItem(FoldStateViewHolder.CREATOR, FoldStateData(this, mainModel, pair.first, name), id)
            idList.add(pair.first to id)
            id++
        }
    }
}