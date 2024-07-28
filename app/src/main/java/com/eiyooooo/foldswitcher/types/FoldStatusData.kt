package com.eiyooooo.foldswitcher.types

import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel

data class FoldStatusData(
    val viewModel: MainActivityViewModel,
    val state: Int,
    val text: String
)