package com.lm.hook.kw;

import android.graphics.Bitmap;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BitmapHookImpl extends BaseHookImpl {
    private static final String TAG = "KWBitmapHookImpl";
    private long startTime = 0;

    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(bitmapCompressHook());
    }


    private MethodSignature bitmapCompressHook() {
        return new MethodSignature("com.kwai.libjepg.TJUtils","compressJPG",
                new Object[]{
                        String.class,
                        int.class,
                        Bitmap.class,
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "bitmapCompressHook beforeHookedMethod");
                                startTime = System.currentTimeMillis();
                                Bitmap bitmap = (Bitmap) param.args[2];
                                if (bitmap != null) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    LogUtils.e(TAG, "width: "+width+" height: "+height);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.recordLog(TAG, "pic-save-cost: "+(System.currentTimeMillis() - startTime));
                            }
                        }
                });
    }
}
