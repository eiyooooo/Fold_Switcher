package com.eiyooooo.foldswitcher

import android.app.Application
import android.content.Context
import com.eiyooooo.foldswitcher.helpers.ShizukuHelper
import org.lsposed.hiddenapibypass.HiddenApiBypass

class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        HiddenApiBypass.addHiddenApiExemptions("L")
    }

    override fun onCreate() {
        super.onCreate()
        ShizukuHelper.setShizukuListener()
    }
}