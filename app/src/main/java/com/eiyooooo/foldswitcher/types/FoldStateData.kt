package com.eiyooooo.foldswitcher.types

import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel

data class FoldStateData(
    val viewModel: MainActivityViewModel,
    val state: Int,
    val text: String
)