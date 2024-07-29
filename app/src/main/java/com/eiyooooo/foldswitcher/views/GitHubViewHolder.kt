package com.eiyooooo.foldswitcher.views

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemGithubBinding
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class GitHubViewHolder(root: View) : BaseViewHolder<Any?>(root), View.OnClickListener {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            ItemGithubBinding.inflate(inflater, outer.root, true)
            GitHubViewHolder(outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setData(Uri.parse("https://github.com/eiyooooo/Fold_Switcher"))
            context.startActivity(intent)
        } catch (ignored: Exception) {
            Toast.makeText(context, context.getString(R.string.no_browser), Toast.LENGTH_SHORT).show()
        }
    }
}