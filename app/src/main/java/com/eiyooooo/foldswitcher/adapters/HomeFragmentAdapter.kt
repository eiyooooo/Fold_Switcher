package com.eiyooooo.foldswitcher.adapters

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import com.eiyooooo.foldswitcher.views.ShizukuStatusViewHolder
import rikka.recyclerview.IdBasedRecyclerViewAdapter
import rikka.recyclerview.IndexCreatorPool

class HomeFragmentAdapter(shizukuStatus: LiveData<ShizukuStatus?>) : IdBasedRecyclerViewAdapter(ArrayList()) {

    init {
        shizukuStatus.observeForever {
            updateData(it)
        }
        setHasStableIds(true)
    }

    companion object {
        private const val ID_SHIZUKU_STATUS = 0L
    }

    override fun onCreateCreatorPool(): IndexCreatorPool {
        return IndexCreatorPool()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(shizukuStatus: ShizukuStatus?) {
        clear()

        if (shizukuStatus != ShizukuStatus.HAVE_PERMISSION) {
            addItem(ShizukuStatusViewHolder.CREATOR, shizukuStatus, ID_SHIZUKU_STATUS)
        }

        notifyDataSetChanged()
    }
}
