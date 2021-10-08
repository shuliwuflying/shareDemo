package com.lm.hook.beautyme;

import com.lm.hook.base.MediaCodecHookImpl;
import com.lm.hook.camera.BitmapHookImpl;
import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.HdCaptureHookImpl;
import com.lm.hook.camera.PreviewHookImpl;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BeautyMeHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        LaunchHookImpl launchHook = new LaunchHookImpl();
        launchHook.init(param);

        CameraStageHookImpl cameraStageHook = new CameraStageHookImpl(launchHook);
        new PreviewHookImpl(cameraStageHook).init(param);
        cameraStageHook.init(param);
        new HdCaptureHookImpl().init(param);
        MediaCodecHookImpl mediaCodecHook = new MediaCodecHookImpl();
        mediaCodecHook.init(param);
        new ComplexHookImpl(mediaCodecHook).init(param);
        new ShortCutHookImpl().init(param);
        new BitmapHookImpl().init(param);
    }
}
