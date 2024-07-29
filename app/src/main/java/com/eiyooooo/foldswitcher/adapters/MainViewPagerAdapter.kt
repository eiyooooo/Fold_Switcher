package com.eiyooooo.foldswitcher.adapters

import android.annotation.SuppressLint
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.types.FoldStatusData
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel
import com.eiyooooo.foldswitcher.views.FoldStatusViewHolder
import com.eiyooooo.foldswitcher.views.MainActivity
import com.eiyooooo.foldswitcher.views.ShizukuInstructionViewHolder
import com.eiyooooo.foldswitcher.views.ShizukuRequestViewHolder
import com.eiyooooo.foldswitcher.views.ShizukuStatusViewHolder
import rikka.recyclerview.IdBasedRecyclerViewAdapter
import rikka.recyclerview.IndexCreatorPool

class MainViewPagerAdapter(private val mainActivity: MainActivity, private val mainModel: MainActivityViewModel) : IdBasedRecyclerViewAdapter(ArrayList()) {
    companion object {
        private const val ID_SHIZUKU_STATUS = 0L
        private const val ID_SHIZUKU_INSTRUCTION = 1L
        private const val ID_SHIZUKU_REQUEST = 2L
        private const val ID_RESET_STATE = 3L
        private val idList = mutableListOf<Pair<Int, Long>>()
    }

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

        addItem(ShizukuStatusViewHolder.CREATOR, shizukuStatus, ID_SHIZUKU_STATUS)

        if (shizukuStatus == ShizukuStatus.HAVE_PERMISSION) {
            mainModel.executor.value.setStatus(true)
            addStates()
        } else {
            mainModel.executor.value.setStatus(false)
            addItem(ShizukuInstructionViewHolder.CREATOR, null, ID_SHIZUKU_INSTRUCTION)
            if (shizukuStatus != ShizukuStatus.SHIZUKU_NOT_RUNNING) {
                addItem(ShizukuRequestViewHolder.CREATOR, null, ID_SHIZUKU_REQUEST)
                mainModel.startCheckingShizukuPermission()
            }
        }

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData() {
        mainModel.stopCheckingShizukuPermission()
        clear()

        addStates()

        notifyDataSetChanged()
    }

    private fun addStates() {
        addItem(FoldStatusViewHolder.CREATOR, FoldStatusData(mainModel, -1, mainActivity.getString(R.string.state_reset)), ID_RESET_STATE)
        var id = 4L
        mainModel.executor.value.supportStates.forEach { pair ->
            val name = pair.second.ifEmpty { pair.first.toString() }
            addItem(FoldStatusViewHolder.CREATOR, FoldStatusData(mainModel, pair.first, name), id)
            idList.add(pair.first to id)
            id++
        }
    }
}