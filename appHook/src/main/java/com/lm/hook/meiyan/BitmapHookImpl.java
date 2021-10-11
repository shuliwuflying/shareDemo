package com.lm.hook.meiyan;


import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BitmapHookImpl extends BaseHookImpl {
    private static final String TAG = "MYBitmapHookImpl";
    private long startTime = 0;

    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        try {
            hookEntityList.add(bitmapCompressHook());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private MethodSignature bitmapCompressHook() throws ClassNotFoundException {
        Class<?> param1 = hookParam.classLoader.loadClass("com.meitu.core.types.NativeBitmap");
        Class<?> param4 = hookParam.classLoader.loadClass("com.meitu.core.imageloader.ImageInfo$ImageFormat");
        return new MethodSignature("com.meitu.core.imageloader.MteSkiaImageLoader","saveImageToDisk",
                new Object[]{
                        param1,
                        String.class,
                        int.class,
                        param4,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "bitmapCompressHook beforeHookedMethod");
                                startTime = System.currentTimeMillis();

                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.recordLog(TAG, "pic-save-cost: "+(System.currentTimeMillis() - startTime));
                            }
                        }
                });
    }
}
