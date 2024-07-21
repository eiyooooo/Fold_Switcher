package com.eiyooooo.foldswitcher.views

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemShizukuInstructionBinding
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class ShizukuInstructionViewHolder(private val binding: ItemShizukuInstructionBinding, root: View) : BaseViewHolder<Any?>(root), View.OnClickListener {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            val inner = ItemShizukuInstructionBinding.inflate(inflater, outer.root, true)
            ShizukuInstructionViewHolder(inner, outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    private inline val title get() = binding.root

    override fun onClick(v: View) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            var url = "https://shizuku.rikka.app/guide/setup/"
            if (title.text.contains("说明")) {
                url = "https://shizuku.rikka.app/zh-hans/guide/setup/"
            }
            intent.setData(Uri.parse(url))
            context.startActivity(intent)
        } catch (ignored: Exception) {
            Toast.makeText(context, context.getString(R.string.no_browser), Toast.LENGTH_SHORT).show()
        }
    }
}