package com.lm.hook.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import java.lang.reflect.Method;
import java.util.Map;

public class HookUtils {
    private static final String TAG = "HookUtils";
    public static Object proxyPackageManager;
    public static Object originPackageManager;
    public static Class<?> activityThreadClz;
    public static Field packageManagerField;
    public static boolean isInit = false;
    public static IBinder proxyIBinder;
    public static Handler uiHandler;

    public static void init(ClassLoader classLoader) {
        try {
            if (isInit) {
                return;
            }
            //1、得到ActivityThread类
            activityThreadClz = classLoader.loadClass("android.app.ActivityThread");
            Method currentActivityThread = activityThreadClz.getMethod("currentActivityThread");
            //2、得到当前的ActivityThread对象
            Object activityThread = currentActivityThread.invoke(null);

            //3、获取sPackageManager对象, 存储proxyPackageManager
            packageManagerField = activityThreadClz.getDeclaredField("sPackageManager");
            packageManagerField.setAccessible(true);
            proxyPackageManager = packageManagerField.get(null);

            //4、置空sPackageManager
            packageManagerField.set(null, null);

            Object value = packageManagerField.get(null);
            LogUtils.e(TAG, "value: "+value);

            //5、移除sCache中对应Package的Ibinder
            removekey(classLoader);

            //6、获取getPackageManger方法
            Method getPackageManager = activityThreadClz.getMethod("getPackageManager");

            //7、获取originPackageManager
            originPackageManager = getPackageManager.invoke(activityThread);

            //8、设置proxyPackageManager
            packageManagerField.set(null, proxyPackageManager);

            //9、重新设置sCache的中的IBinder
            revertProxyBinder(classLoader);


            LogUtils.e(TAG, "proxyPackageManager: "+ proxyPackageManager + "  originPackageManager: "+originPackageManager);
            LogUtils.e(TAG, "proxySuperClz: "+proxyPackageManager.getClass().getSuperclass());
            LogUtils.e(TAG, "originSuperClz: "+originPackageManager.getClass().getSuperclass());
            isInit = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 越早hook越好，推荐在Application.attachBaseContext中调用
     */
    public static void revertOrigin(ClassLoader classLoader) {
        try {
            init(classLoader);
            packageManagerField.set(activityThreadClz, originPackageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void revertProxy(ClassLoader classLoader) {
        try {
            init(classLoader);
            packageManagerField.set(activityThreadClz, proxyPackageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removekey(ClassLoader classLoader) {
        try {
            Class<?> serviceManagerClz = classLoader.loadClass("android.os.ServiceManager");
            Field cacheField = serviceManagerClz.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String, IBinder> sCache = (Map)cacheField.get(serviceManagerClz);
            proxyIBinder = sCache.remove("package");
            LogUtils.e(TAG, "proxyIBinder: "+proxyIBinder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void revertProxyBinder(ClassLoader classLoader) {
        try {
            Class<?> serviceManagerClz = classLoader.loadClass("android.os.ServiceManager");
            Field cacheField = serviceManagerClz.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String, IBinder> sCache = (Map)cacheField.get(serviceManagerClz);
            sCache.put("package",proxyIBinder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getBinder(ClassLoader classLoader) {
        try {
            Class<?> serviceManagerClz = classLoader.loadClass("android.os.ServiceManager");
            Field cacheField = serviceManagerClz.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String, IBinder> sCache = (Map)cacheField.get(serviceManagerClz);
            IBinder obj = sCache.get("package");
            LogUtils.e(TAG, "getBinder: "+obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过进程id获取进程名
     */
    public static String getProcessNameByPid(final Context context, final int pid) {
        if (context == null || pid <= 0) {
            return "";
        }
        try {
            final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (final ActivityManager.RunningAppProcessInfo i : am.getRunningAppProcesses()) {
                if (i.pid == pid && (!TextUtils.isEmpty(i.processName))) {
                    return i.processName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] b = new byte[128];
        FileInputStream in = null;
        try {
            in = new FileInputStream("/proc/" + pid + "/cmdline");
            int len = in.read(b);
            in.close();
            if (len > 0) {
                for (int i = 0; i < len; i++) { // lots of '0' in tail , remove them
                    if (b[i] > 128 || b[i] <= 0) {
                        len = i;
                        break;
                    }
                }
                return new String(b, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void initHandler() {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
    }
}
