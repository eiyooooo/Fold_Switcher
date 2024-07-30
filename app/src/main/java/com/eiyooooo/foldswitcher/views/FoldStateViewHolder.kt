package com.eiyooooo.foldswitcher.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemFoldStateBinding
import com.eiyooooo.foldswitcher.types.FoldStateData
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class FoldStateViewHolder(private val binding: ItemFoldStateBinding, root: View) : BaseViewHolder<FoldStateData>(root), View.OnClickListener {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            val inner = ItemFoldStateBinding.inflate(inflater, outer.root, true)
            FoldStateViewHolder(inner, outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    private inline val textView get() = binding.root

    override fun onClick(v: View) {
        if (data.state == -1) {
            data.viewModel.executor.value.resetState()
        } else {
            data.viewModel.executor.value.requestState(data.state)
        }
    }

    override fun onBind() {
        textView.text = data.text
        if (data.viewModel.executor.value.currentState.value == data.state) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.check_circle, 0, 0, 0)
        } else {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.phone, 0, 0, 0)
        }
    }
}