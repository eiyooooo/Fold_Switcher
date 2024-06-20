package com.eiyooooo.foldswitcher;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.devicestate.DeviceStateInfo;
import android.hardware.devicestate.IDeviceStateManagerCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.eiyooooo.foldswitcher.databinding.ActivityMainBinding;

import java.util.Arrays;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {
    public Handler handler;
    private ActivityMainBinding mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.background));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) != Configuration.UI_MODE_NIGHT_YES)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        handler = new Handler(getMainLooper());

        mainActivity = ActivityMainBinding.inflate(this.getLayoutInflater());
        setContentView(mainActivity.getRoot());
        setButtonListener();

        Shizuku.addRequestPermissionResultListener(this::onRequestPermissionsResult);
        try {
            if (checkShizukuPermission(0)) log("Shizuku connected");
            else log("Shizuku not connected");
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("binder haven't been received")) log("Shizuku not connected");
            else log(Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shizuku.removeRequestPermissionResultListener(this::onRequestPermissionsResult);
    }

    private void setButtonListener() {
        mainActivity.button1.setOnClickListener(v -> {
            try {
                DeviceStateInfo a = getDeviceStateManager().getDeviceStateInfo();
                getDeviceStateManager().requestState(a.supportedStates[0], 0);
            } catch (ReflectiveOperationException e) {
                log(Log.getStackTraceString(e));
            }
        });
        mainActivity.button2.setOnClickListener(v -> {
            try {
                DeviceStateInfo a = getDeviceStateManager().getDeviceStateInfo();
                getDeviceStateManager().requestState(a.supportedStates[2], 0);
            } catch (ReflectiveOperationException e) {
                log(Log.getStackTraceString(e));
            }
        });
        mainActivity.button3.setOnClickListener(v -> {
            try {
                DeviceStateInfo a = getDeviceStateManager().getDeviceStateInfo();
                getDeviceStateManager().requestState(a.supportedStates[4], 0);
            } catch (ReflectiveOperationException e) {
                log(Log.getStackTraceString(e));
            }
        });
        mainActivity.button4.setOnClickListener(v -> {
            try {
                DeviceStateInfo a = getDeviceStateManager().getDeviceStateInfo();
                getDeviceStateManager().requestState(a.supportedStates[5], 0);
            } catch (ReflectiveOperationException e) {
                log(Log.getStackTraceString(e));
            }
        });
        mainActivity.button5.setOnClickListener(v -> {
            try {
                DeviceStateInfo a = getDeviceStateManager().getDeviceStateInfo();
                log(Arrays.toString(a.supportedStates));
            } catch (ReflectiveOperationException e) {
                log(Log.getStackTraceString(e));
            }
        });
        mainActivity.button6.setOnClickListener(v -> {
            try {
                if (checkShizukuPermission(0)) log("Shizuku connected");
                else log("Shizuku not connected");
            } catch (Exception e) {
                String msg = e.getMessage();
                if (msg != null && msg.contains("binder haven't been received")) log("Shizuku not connected");
                else log(Log.getStackTraceString(e));
            }
        });
        mainActivity.buttonRefresh.setOnClickListener(v -> {
            mainActivity.logText.setText(log.toString());
        });
        mainActivity.buttonClear.setOnClickListener(v -> {
            log.setLength(0);
            mainActivity.logText.setText(log.toString());
        });
    }

    private DeviceStateManager getDeviceStateManager() throws ReflectiveOperationException {
        if (DeviceStateManager.deviceStateManager == null) {
            DeviceStateManager.deviceStateManager = new DeviceStateManager();
            DeviceStateManager.deviceStateManager.registerCallback(new IDeviceStateManagerCallback.Stub() {
                @Override
                public void onDeviceStateInfoChanged(DeviceStateInfo info) {
                    log("onDeviceStateInfoChanged");
                }

                @Override
                public void onRequestActive(IBinder token) {
                    log("onRequestActive");
                }

                @Override
                public void onRequestCanceled(IBinder token) {
                    log("onRequestCanceled");
                }
            });
        }
        return DeviceStateManager.deviceStateManager;
    }

    private static final StringBuilder log = new StringBuilder();

    private void log(String str) {
        Log.d("Fold_Switcher", str);
        log.append("<").append(DateFormat.format("HH:mm:ss", new java.util.Date())).append("> ");
        log.append(str).append("\n");
        handler.post(() -> mainActivity.logText.setText(log.toString())); //TODO
    }

    private boolean checkShizukuPermission(int code) {
        if (Shizuku.isPreV11())
            return false;
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            try {
                getDeviceStateManager();
            } catch (ReflectiveOperationException e) {
                log(Log.getStackTraceString(e));
            }
            return true;
        } else if (Shizuku.shouldShowRequestPermissionRationale())
            return false;
        else {
            Shizuku.requestPermission(code);
            return false;
        }
    }

    private void onRequestPermissionsResult(int requestCode, int grantResult) {
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            try {
                getDeviceStateManager();
            } catch (ReflectiveOperationException e) {
                log(Log.getStackTraceString(e));
            }
        }
    }
}