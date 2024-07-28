package com.eiyooooo.foldswitcher.wrappers;

import android.annotation.SuppressLint;
import android.hardware.devicestate.DeviceStateInfo;
import android.hardware.devicestate.IDeviceStateManagerCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Method;

import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;

public final class DeviceStateManager {
    private static volatile DeviceStateManager instance;
    private static volatile DeviceStateManager instanceByShizuku;

    public static DeviceStateManager getInstance(Boolean shizukuMode) throws ReflectiveOperationException {
        if (shizukuMode) {
            if (instanceByShizuku == null) {
                synchronized (DeviceStateManager.class) {
                    if (instanceByShizuku == null) {
                        instanceByShizuku = new DeviceStateManager(true);
                    }
                }
            }
            return instanceByShizuku;
        } else {
            if (instance == null) {
                synchronized (DeviceStateManager.class) {
                    if (instance == null) {
                        instance = new DeviceStateManager(false);
                    }
                }
            }
            return instance;
        }
    }

    public static void resetDeviceStateManager(Boolean shizukuMode) {
        if (shizukuMode) {
            instanceByShizuku = null;
        } else {
            instance = null;
        }
    }

    private final IInterface manager;
    private final Class<?> managerCls;

    @SuppressLint({"BlockedPrivateApi", "PrivateApi"})
    private DeviceStateManager(Boolean shizukuMode) throws ReflectiveOperationException {
        if (shizukuMode) manager = getServiceByShizuku();
        else manager = getService();
        managerCls = manager.getClass();
    }

    @SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})
    private static IInterface getService() throws ReflectiveOperationException {
        IBinder binder = (IBinder) Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class).invoke(null, "device_state");
        Method asInterfaceMethod = Class.forName("android.hardware.devicestate.IDeviceStateManager" + "$Stub").getMethod("asInterface", IBinder.class);
        return (IInterface) asInterfaceMethod.invoke(null, binder);
    }

    @SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})
    private static IInterface getServiceByShizuku() throws ReflectiveOperationException {
        IBinder binder = new ShizukuBinderWrapper(SystemServiceHelper.getSystemService("device_state"));
        Method asInterfaceMethod = Class.forName("android.hardware.devicestate.IDeviceStateManager" + "$Stub").getMethod("asInterface", IBinder.class);
        return (IInterface) asInterfaceMethod.invoke(null, binder);
    }

    private Method getDeviceStateInfoMethod;

    public DeviceStateInfo getDeviceStateInfo() throws ReflectiveOperationException {
        if (getDeviceStateInfoMethod == null) {
            getDeviceStateInfoMethod = managerCls.getMethod("getDeviceStateInfo");
        }
        return (DeviceStateInfo) getDeviceStateInfoMethod.invoke(manager);
    }

    private Method registerCallbackMethod;

    public void registerCallback(IDeviceStateManagerCallback callback) throws ReflectiveOperationException {
        if (registerCallbackMethod == null) {
            registerCallbackMethod = managerCls.getMethod("registerCallback", IDeviceStateManagerCallback.class);
        }
        registerCallbackMethod.invoke(manager, callback);
    }

    private Method requestStateMethod;

    public void requestState(int state, int flags) throws ReflectiveOperationException {
        if (requestStateMethod == null) {
            requestStateMethod = managerCls.getMethod("requestState", IBinder.class, int.class, int.class);
        }
        requestStateMethod.invoke(manager, new Binder(), state, flags);
    }

    private Method cancelStateRequestMethod;

    public void cancelStateRequest() throws ReflectiveOperationException {
        if (cancelStateRequestMethod == null) {
            cancelStateRequestMethod = managerCls.getMethod("cancelStateRequest");
        }
        cancelStateRequestMethod.invoke(manager);
    }
}