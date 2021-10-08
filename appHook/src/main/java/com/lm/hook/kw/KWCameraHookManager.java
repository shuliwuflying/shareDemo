package com.lm.hook.kw;

import com.lm.hook.base.MediaCodecHookImpl;
import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.HdCaptureHookImpl;
import com.lm.hook.camera.PreviewHookImpl;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class KWCameraHookManager {
    private static final String TAG = "xposedHook";

    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        LaunchHookImpl launchHook = new LaunchHookImpl();
        launchHook.init(param);
        CameraStageHookImpl cameraStageHook = new CameraStageHookImpl(launchHook);
        cameraStageHook.init(param);
        new PreviewHookImpl(cameraStageHook).init(param);
        new HdCaptureHookImpl().init(param);
        new ContextHookImpl().init(param);
        new KwReportHook().init(param);
        new KWLogHookImpl().init(param);
        new RenderFpsHookImpl().init(param);
        MediaCodecHookImpl mediaCodecHook = new MediaCodecHookImpl();
        mediaCodecHook.init(param);
        KWRecordHookImpl recordHook = new KWRecordHookImpl();
        recordHook.init(param);
        recordHook.setRecordListener(mediaCodecHook);
        new NormalCaptureHookImpl().init(param);
    }
}
