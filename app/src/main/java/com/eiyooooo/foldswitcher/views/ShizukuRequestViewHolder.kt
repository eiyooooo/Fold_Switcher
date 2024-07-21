package com.eiyooooo.foldswitcher.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemShizukuRequestBinding
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator
import rikka.shizuku.Shizuku

class ShizukuRequestViewHolder(root: View) : BaseViewHolder<Any?>(root), View.OnClickListener {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            ItemShizukuRequestBinding.inflate(inflater, outer.root, true)
            ShizukuRequestViewHolder(outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        Shizuku.requestPermission(0)
    }
}