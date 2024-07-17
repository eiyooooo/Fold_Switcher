package com.eiyooooo.foldswitcher.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemRequestShizukuBinding
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class ShizukuStatusViewHolder(private val binding: ItemRequestShizukuBinding, root: View) : BaseViewHolder<ShizukuStatus>(root) {

    companion object {
        val TAG: String = ShizukuStatusViewHolder::class.java.simpleName

        val CREATOR = Creator<ShizukuStatus> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            val inner = ItemRequestShizukuBinding.inflate(inflater, outer.root, true)
            ShizukuStatusViewHolder(inner, outer.root)
        }
    }

    private inline val buttonSetupInstruction get() = binding.buttonSetupInstruction
    private inline val buttonRequestPermission get() = binding.buttonRequestPermission

    override fun onBind() {
        val status = data

        if (status == ShizukuStatus.SHIZUKU_NOT_RUNNING) {
            buttonRequestPermission.isClickable = false
        }

        //TODO: migrate to init
        buttonSetupInstruction.setOnClickListener {
            Log.d(TAG, "buttonSetupInstruction onClick")
            //TODO
        }
        buttonRequestPermission.setOnClickListener {
            Log.d(TAG, "buttonRequestPermission onClick")
            //TODO
        }
    }
}
