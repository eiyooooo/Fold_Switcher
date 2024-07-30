package com.eiyooooo.foldswitcher.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.helpers.SharedPreferencesHelper
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemInstructionQuickSwitchBinding
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
        SharedPreferencesHelper.saveString("quickSwitchName", "")
        SharedPreferencesHelper.saveInt("quickSwitchState", -1)
        title.text = context.getString(R.string.instruction_no_quick_switch)
    }

    override fun onBind() {
        val name = SharedPreferencesHelper.getString("quickSwitchName")
        state = SharedPreferencesHelper.getInt("quickSwitchState", -1)
        if (name.isEmpty() || state == -1) {
            SharedPreferencesHelper.saveString("quickSwitchName", "")
            SharedPreferencesHelper.saveInt("quickSwitchState", -1)
        } else {
            title.text = String.format(context.getString(R.string.instruction_current_quick_switch), name)
        }
    }
}