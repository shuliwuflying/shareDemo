package com.lm.hook.kw;

import android.hardware.Camera;
import android.view.TextureView;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

public class HwPreviewFpsHookImpl extends BaseHookImpl {
    private static final String TAG = "HwPreviewFpsHookImpl";
    private static List<TextureView> textureViewList = new ArrayList<>();
    private static boolean sIsFirstFrame = false;
    private static long startTime = 0;
    private static long sCount1 = 0;


    public HwPreviewFpsHookImpl() {
        hookEntityList.add(getCameraPreviewHook());
    }

    private static MethodSignature getCameraPreviewHook() {
        final String targetClz = Camera.class.getName();
        final String method = "setPreviewCallbackWithBuffer";
        final Object[] methodParams = new Object[] {
            Camera.PreviewCallback.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    LogUtils.e(TAG, "getCameraPreviewHook1111: "+android.util.Log.getStackTraceString(new Throwable("setPreviewCallbackWithBuffer")));
                    FrameAvailableListener listener = new FrameAvailableListener((Camera.PreviewCallback) param.args[0]);
                    param.args[0] = listener;
                }
            }
        };
        return new MethodSignature(targetClz, method, methodParams);
    }

    private static class FrameAvailableListener implements Camera.PreviewCallback {

        Camera.PreviewCallback previewCallback;
        private long startTime = 0;
        private int count = 0;


        FrameAvailableListener(Camera.PreviewCallback  callback) {
            previewCallback = callback;
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
                LaunchHookImpl.setFirstReceiveFrame();
            }

            count++;
            long timeDuration = System.currentTimeMillis() - startTime;
            if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 15 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
                LogUtils.e(TAG,"onFrameAvailable thread: "+Thread.currentThread());
                CameraAnalysis.printPreviewFps(String.format("%.2f",count/5.0));
                CameraAnalysis.isPreviewParamsSet = true;
                count = 0;
                startTime = System.currentTimeMillis();
            }
            previewCallback.onPreviewFrame(data, camera);
        }
    }

}
