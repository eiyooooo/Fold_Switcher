package com.eiyooooo.foldswitcher

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors
import org.lsposed.hiddenapibypass.HiddenApiBypass

class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        HiddenApiBypass.addHiddenApiExemptions("L")
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}