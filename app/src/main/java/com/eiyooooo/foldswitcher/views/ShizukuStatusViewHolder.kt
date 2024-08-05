package com.eiyooooo.foldswitcher.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemShizukuStatusBinding
import com.eiyooooo.foldswitcher.types.ShizukuStatus
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class ShizukuStatusViewHolder(private val binding: ItemShizukuStatusBinding, root: View) : BaseViewHolder<ShizukuStatus>(root) {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            val inner = ItemShizukuStatusBinding.inflate(inflater, outer.root, true)
            ShizukuStatusViewHolder(inner, outer.root)
        }
    }

    private inline val explanation get() = binding.ShizukuExplanation
    private inline val warning get() = binding.ShizukuWarning

    override fun onBind() {
        val status = data

        when (status) {
            ShizukuStatus.HAVE_PERMISSION -> {
                warning.visibility = View.GONE
                explanation.setText(R.string.Shizuku_connected)
            }

            ShizukuStatus.VERSION_NOT_SUPPORT -> {
                warning.visibility = View.VISIBLE
                explanation.setText(R.string.Shizuku_need_update)
            }

            else -> {
                warning.visibility = View.VISIBLE
                explanation.setText(R.string.Shizuku_explanation)
            }
        }
    }
}