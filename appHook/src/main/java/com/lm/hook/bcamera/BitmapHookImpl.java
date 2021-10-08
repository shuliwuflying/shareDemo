package com.lm.hook.bcamera;

import android.graphics.Bitmap;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import java.io.OutputStream;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class BitmapHookImpl extends BaseHookImpl {
    private static final String TAG = "B612BitmapHookImpl";
    private long startTime = 0;

    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(jpegTurboHook());
    }

    private MethodSignature jpegTurboHook() {
        return new MethodSignature("com.linecorp.android.common.jpegturbo.JpegTurbo","compressSafely",
                new Object[]{
                        Bitmap.class,
                        int.class,
                        String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "jpegTurboHook beforeHookedMethod");
                                startTime = System.currentTimeMillis();
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "jpegTurboHook afterHookedMethod");
                                LogUtils.recordLog(TAG, "pic-save-cost: "+(System.currentTimeMillis() - startTime));
                            }
                        }
                });
    }
}
