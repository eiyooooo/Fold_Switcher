package com.eiyooooo.foldswitcher.adapters

import android.annotation.SuppressLint
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel
import com.eiyooooo.foldswitcher.views.ShizukuInstructionViewHolder
import com.eiyooooo.foldswitcher.views.ShizukuRequestViewHolder
import com.eiyooooo.foldswitcher.views.ShizukuStatusViewHolder
import rikka.recyclerview.IdBasedRecyclerViewAdapter
import rikka.recyclerview.IndexCreatorPool

class MainViewPagerAdapter(private val mainModel: MainActivityViewModel) : IdBasedRecyclerViewAdapter(ArrayList()) {
    companion object {
        private const val ID_SHIZUKU_STATUS = 0L
        private const val ID_SHIZUKU_INSTRUCTION = 1L
        private const val ID_SHIZUKU_REQUEST = 2L
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

        if (shizukuStatus != ShizukuStatus.HAVE_PERMISSION) {
            addItem(ShizukuInstructionViewHolder.CREATOR, null, ID_SHIZUKU_INSTRUCTION)
            if (shizukuStatus != ShizukuStatus.SHIZUKU_NOT_RUNNING) {
                addItem(ShizukuRequestViewHolder.CREATOR, null, ID_SHIZUKU_REQUEST)
                mainModel.startCheckingShizukuPermission()
            }
        }

        notifyDataSetChanged()
    }
}