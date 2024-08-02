package com.eiyooooo.foldswitcher.views

import android.content.ComponentName
import android.graphics.drawable.Icon
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eiyooooo.foldswitcher.MyTileService
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerHighBinding
import com.eiyooooo.foldswitcher.databinding.ItemFoldStateBinding
import com.eiyooooo.foldswitcher.helpers.SharedPreferencesHelper
import com.eiyooooo.foldswitcher.types.FoldStateData
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class FoldStateViewHolder(private val binding: ItemFoldStateBinding, private val root: View) : BaseViewHolder<FoldStateData>(root), View.OnClickListener, View.OnLongClickListener {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerHighBinding.inflate(inflater, parent, false)
            val inner = ItemFoldStateBinding.inflate(inflater, outer.root, true)
            FoldStateViewHolder(inner, outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    private val showName: String get() = data?.adjustedNameId?.let { context.getString(it) } ?: data?.name ?: ""
    private inline val textView get() = binding.root

    override fun onClick(v: View) {
        if (data.state == -1) {
            data.viewModel.executor.value.resetState()
        } else {
            data.viewModel.executor.value.requestState(data.state)
        }
    }

    override fun onLongClick(v: View): Boolean {
        if (data.state != -1) {
            SharedPreferencesHelper.saveString("quickSwitchShowName", showName)
            SharedPreferencesHelper.saveString("quickSwitchAdjustedName", data.adjustedName)
            SharedPreferencesHelper.saveInt("quickSwitchState", data.state)
            if (data.viewModel.useShizukuExecutor) {
                data.adapter.updateData(data.viewModel.shizukuStatus.value)
            } else {
                data.adapter.updateData()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data.adapter.statusBarManager.requestAddTileService(
                    ComponentName(context, MyTileService::class.java),
                    showName, Icon.createWithResource(context, data.iconId),
                    { }, { })
            }
        }
        return true
    }

    override fun onBind() {
        textView.text = showName
        if (data.state != -1) {
            root.setOnLongClickListener(this)
        }
        if (data.viewModel.executor.value.currentState.value == data.state) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.check_circle, 0, 0, 0)
        } else {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(data.iconId, 0, 0, 0)
        }
    }
}