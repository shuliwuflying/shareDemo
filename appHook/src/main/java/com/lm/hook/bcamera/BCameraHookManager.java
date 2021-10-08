package com.lm.hook.bcamera;

import com.lm.hook.base.MediaCodecHookImpl;
import com.lm.hook.camera.HdCaptureHookImpl;
import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.PreviewHookImpl;
import com.lm.hook.base.AdHookImpl;
import com.lm.hook.camera.RecordHookImpl;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BCameraHookManager {
    private static final String TAG = "BCameraHookManager";

    public static void hook(final XC_LoadPackage.LoadPackageParam param) {
        LaunchHookImpl hook = new LaunchHookImpl();
        hook.init(param);

        CameraStageHookImpl cameraStageHook = new CameraStageHookImpl(hook);
        cameraStageHook.init(param);
        new RenderFpsHookImpl(hook).init(param);
        HdCaptureHookImpl hdCaptureHook = new HdCaptureHookImpl();
        hdCaptureHook.init(param);
        hdCaptureHook.setCaptureCallback(new HdCaptureHookImpl.ICaptureCallback() {
            @Override
            public void onCaptureComplete(boolean isHdCapture) {

            }
        });
        new PreviewHookImpl(cameraStageHook).init(param);
        new LaunchHookImpl().init(param);
        new AdHookImpl().init(param);
        RecordHookImpl recordHook = new RecordHookImpl();
        recordHook.init(param);
        MediaCodecHookImpl mediaCodecHook = new MediaCodecHookImpl();
        mediaCodecHook.init(param);
        recordHook.setRecordListener(mediaCodecHook);
        new LogHookIml().init(param);

    }
}
