package com.eiyooooo.foldswitcher;

import android.app.Application;
import android.content.Context;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        HiddenApiBypass.addHiddenApiExemptions("L");
    }
}