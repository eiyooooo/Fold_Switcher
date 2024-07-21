package com.eiyooooo.foldswitcher.adapters

import android.annotation.SuppressLint
import androidx.lifecycle.lifecycleScope
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import com.eiyooooo.foldswitcher.views.HomeFragment
import com.eiyooooo.foldswitcher.views.ShizukuInstructionViewHolder
import com.eiyooooo.foldswitcher.views.ShizukuRequestViewHolder
import com.eiyooooo.foldswitcher.views.ShizukuStatusViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import rikka.recyclerview.IdBasedRecyclerViewAdapter
import rikka.recyclerview.IndexCreatorPool

class HomeFragmentAdapter(homeFragment: HomeFragment, private val checkShizukuPermission: () -> Unit) : IdBasedRecyclerViewAdapter(ArrayList()) {
    companion object {
        private const val ID_SHIZUKU_STATUS = 0L
        private const val ID_SHIZUKU_INSTRUCTION = 1L
        private const val ID_SHIZUKU_REQUEST = 2L

        private lateinit var scope: CoroutineScope
        private var checkShizukuPermissionJob: Job? = null
    }

    init {
        setHasStableIds(true)
        scope = homeFragment.lifecycleScope
    }

    override fun onCreateCreatorPool(): IndexCreatorPool {
        return IndexCreatorPool()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(shizukuStatus: ShizukuStatus?) {
        clear()
        checkShizukuPermissionJob?.cancel()
        checkShizukuPermissionJob = null

        addItem(ShizukuStatusViewHolder.CREATOR, shizukuStatus, ID_SHIZUKU_STATUS)

        if (shizukuStatus != ShizukuStatus.HAVE_PERMISSION) {
            addItem(ShizukuInstructionViewHolder.CREATOR, null, ID_SHIZUKU_INSTRUCTION)
            if (shizukuStatus != ShizukuStatus.SHIZUKU_NOT_RUNNING) {
                addItem(ShizukuRequestViewHolder.CREATOR, null, ID_SHIZUKU_REQUEST)

                checkShizukuPermissionJob = scope.launch {
                    while (isActive) {
                        delay(1000)
                        checkShizukuPermission()
                    }
                }
            }
        }

        notifyDataSetChanged()
    }
}