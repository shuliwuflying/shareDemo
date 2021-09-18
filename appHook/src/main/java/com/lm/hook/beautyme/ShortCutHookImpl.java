package com.lm.hook.beautyme;

import android.content.ContextWrapper;
import android.os.Build;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class ShortCutHookImpl extends BaseHookImpl {
    private final static String TAG = "ShortCutHookImpl";

    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            hookEntityList.add(getShortCutHook());
//            hookEntityList.add(getShortCutHook1());
//            hookEntityList.add(getShortCutHook2());
        }
    }


    private MethodSignature getShortCutHook() {
        final String targetClz = "android.content.pm.ShortcutManager";
        final String method = "addDynamicShortcuts";
        final Object[] params = new Object[] {
                List.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "beforeHookedMethod111 getShortCutHook");
                        param.setResult(false);
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }


    private MethodSignature getShortCutHook1() {
        final String targetClz = "android.content.pm.ShortcutManager";
        final String method = "removeAllDynamicShortcuts";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "beforeHookedMethod111 getShortCutHook1");
                        param.setResult(false);
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature getShortCutHook2() {
        final String targetClz = ContextWrapper.class.getName();
        final String method = "getSystemServiceName";
        final Object[] params = new Object[] {
                Class.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "beforeHookedMethod111 getShortCutHook2: "+param.args[0]);
                        if ("android.content.pm.ShortcutManager".equals(param.args[0].toString())) {
                            LogUtils.e(TAG, "beforeHookedMethod111 getShortCutHook2 setResult");
                            param.setResult(false);
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

}
