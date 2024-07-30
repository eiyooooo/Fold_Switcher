package com.eiyooooo.foldswitcher.views

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemLicensesBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class LicensesViewHolder(root: View) : BaseViewHolder<Any?>(root), View.OnClickListener {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            ItemLicensesBinding.inflate(inflater, outer.root, true)
            LicensesViewHolder(outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
    }
}