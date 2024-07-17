package com.eiyooooo.foldswitcher.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.eiyooooo.foldswitcher.helpers.ShizukuHelper

class HomeFragmentViewModel : ViewModel() {
    val shizukuStatus = ShizukuHelper.shizukuStatus.asLiveData()
}