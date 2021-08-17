package com.lm.hook.camera;

import android.graphics.SurfaceTexture;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NormalRenderFpsHook extends BaseHookImpl {
    private static final String TAG = "NormalRenderFpsHook";

    private static boolean sIsFirstFrame = false;
    private static int sDrawFrameCount = 0;
    private static long sRecordLastFpsTs = 0L;

    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(getUpdateImaTxt());
    }

    private MethodSignature getUpdateImaTxt() {
        final String targetClz = SurfaceTexture.class.getName();
        final String methodName = "updateTexImage";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        printRenderFps();
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private void printRenderFps() {
        if (!sIsFirstFrame) {
            sIsFirstFrame = true;
            sRecordLastFpsTs = System.currentTimeMillis();
        }
        sDrawFrameCount++;
        long timeDuration = System.currentTimeMillis() - sRecordLastFpsTs;
        if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 15 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
            LogUtils.recordLog(TAG, String.format("updateTxtFps: %.2f",sDrawFrameCount*1.0/ConstantUtils.TIME_STAMP_COUNT));
            sRecordLastFpsTs = System.currentTimeMillis();
            sDrawFrameCount = 0;
        }
    }

}
