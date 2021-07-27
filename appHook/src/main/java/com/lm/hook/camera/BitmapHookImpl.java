package com.lm.hook.camera;

import android.graphics.Bitmap;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class BitmapHookImpl extends BaseHookImpl {
    private static final String TAG = "BitmapHookImpl";

    public void hook(XC_LoadPackage.LoadPackageParam param) {
        findAndHookMethod("com.linecorp.kale.android.filter.oasis.filter.utils.GLHelper", param.classLoader, "buildBitmapFromGL",
                int.class,
                int.class,
                int.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "buildBitmapFromGL");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        );

        findAndHookMethod(Bitmap.class.getName(), param.classLoader, "createBitmap",
                int.class,
                int.class,
                Bitmap.Config.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "createBitmap");
                        android.util.Log.e("sliver", android.util.Log.getStackTraceString(new Throwable("createBitmap")));
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        );
    }
}
