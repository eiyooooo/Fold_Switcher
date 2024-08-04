package com.eiyooooo.foldswitcher.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.eiyooooo.foldswitcher.R
import com.eiyooooo.foldswitcher.databinding.HomeItemContainerBinding
import com.eiyooooo.foldswitcher.databinding.ItemSwitchModeBinding
import com.eiyooooo.foldswitcher.types.ExecuteMode
import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import rikka.recyclerview.BaseViewHolder
import rikka.recyclerview.BaseViewHolder.Creator

class SwitchModeViewHolder(root: View) : BaseViewHolder<MainActivityViewModel>(root), View.OnClickListener {
    companion object {
        val CREATOR = Creator<Any> { inflater: LayoutInflater, parent: ViewGroup? ->
            val outer = HomeItemContainerBinding.inflate(inflater, parent, false)
            ItemSwitchModeBinding.inflate(inflater, outer.root, true)
            SwitchModeViewHolder(outer.root)
        }
    }

    init {
        root.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val items = context.resources.getStringArray(R.array.mode)
        when (ExecuteMode.supportModeLevel) {
            ExecuteMode.MODE_UNKNOWN -> {
                items[0] = items[0] + context.getString(R.string.not_support)
                items[1] = items[1] + context.getString(R.string.not_support)
                items[2] = items[2] + context.getString(R.string.not_support)
            }

            ExecuteMode.MODE_2 -> {
                items[0] = items[0] + context.getString(R.string.not_support)
            }

            ExecuteMode.MODE_3 -> {
                items[0] = items[0] + context.getString(R.string.not_support)
                items[1] = items[1] + context.getString(R.string.not_support)
            }
        }
        var checkedItem = (data.executeMode.value ?: ExecuteMode.supportModeLevel) - 1

        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.switch_mode)
            .setSingleChoiceItems(items, checkedItem) { dialog, which ->
                val listView = (dialog as AlertDialog).listView
                when (ExecuteMode.supportModeLevel) {
                    ExecuteMode.MODE_UNKNOWN -> {
                        listView.setItemChecked(which, false)
                    }

                    ExecuteMode.MODE_1 -> {
                        listView.setItemChecked(which, true)
                        checkedItem = which
                    }

                    ExecuteMode.MODE_2 -> {
                        if (which == 0) {
                            listView.setItemChecked(1, true)
                        } else {
                            listView.setItemChecked(which, true)
                            checkedItem = which
                        }
                    }

                    ExecuteMode.MODE_3 -> {
                        if (which == 0 || which == 1) {
                            listView.setItemChecked(2, true)
                        } else {
                            listView.setItemChecked(which, true)
                            checkedItem = which
                        }
                    }
                }
            }
            .setPositiveButton(R.string.ok) { _, _ ->
                if (checkedItem != -1) {
                    data.setExecuteMode(checkedItem + 1)
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }
}