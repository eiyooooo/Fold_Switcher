package com.eiyooooo.foldswitcher.types

import com.eiyooooo.foldswitcher.adapters.MainViewPagerAdapter
import com.eiyooooo.foldswitcher.viewmodels.MainActivityViewModel

data class FoldStateData(
    val adapter: MainViewPagerAdapter,
    val viewModel: MainActivityViewModel,
    val state: Int,
    val text: String
)