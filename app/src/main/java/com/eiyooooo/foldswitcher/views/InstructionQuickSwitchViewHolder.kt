package com.eiyooooo.foldswitcher.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemInstructionQuickSwitchBinding
import com.eiyooooo.foldswitcher.helpers.SharedPreferencesHelper
import com.eiyooooo.foldswitcher.types.getAdjustedNameId
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class InstructionQuickSwitchViewHolder(private val binding: ItemInstructionQuickSwitchBinding, root: View) : BaseViewHolder<Any?>(root), View.OnClickListener {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            val inner = ItemInstructionQuickSwitchBinding.inflate(inflater, outer.root, true)
            InstructionQuickSwitchViewHolder(inner, outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    private inline val title get() = binding.root
    private var state: Int = -1

    override fun onClick(v: View) {
        SharedPreferencesHelper.saveString("quickSwitchShowName", "")
        SharedPreferencesHelper.saveString("quickSwitchAdjustedName", "")
        SharedPreferencesHelper.saveInt("quickSwitchState", -1)
        title.text = context.getString(R.string.instruction_no_quick_switch)
    }

    override fun onBind() {
        val name = SharedPreferencesHelper.getString("quickSwitchShowName", "")
        val adjustedName = SharedPreferencesHelper.getString("quickSwitchAdjustedName", "")
        state = SharedPreferencesHelper.getInt("quickSwitchState", -1)
        if (name.isEmpty() || adjustedName.isEmpty() || state == -1) {
            SharedPreferencesHelper.saveString("quickSwitchShowName", "")
            SharedPreferencesHelper.saveString("quickSwitchAdjustedName", "")
            SharedPreferencesHelper.saveInt("quickSwitchState", -1)
        } else {
            val showName = if (adjustedName.isNotEmpty()) getAdjustedNameId(adjustedName)?.let { context.getString(it) } ?: name else name
            title.text = String.format(context.getString(R.string.instruction_current_quick_switch), showName)
        }
    }
}