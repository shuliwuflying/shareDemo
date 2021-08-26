package com.lm.hook.lv;

import com.lm.hook.base.MediaCodecHookImpl;
import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.HdCaptureHookImpl;
import com.lm.hook.camera.NormalRenderFpsHook;
import com.lm.hook.camera.PreviewHookImpl;
import com.lm.hook.camera.RecordHookImpl;


import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class LvCameraHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        CameraStageHookImpl cameraStageHook = new CameraStageHookImpl(null);
        cameraStageHook.init(param);
        new PreviewHookImpl(cameraStageHook).init(param);
        new HdCaptureHookImpl().init(param);
        new NormalRenderFpsHook().init(param);
        MediaCodecHookImpl mediaCodecHook = new MediaCodecHookImpl();
        RecordHookImpl recordHook = new RecordHookImpl();
        mediaCodecHook.init(param);
        recordHook.init(param);
        recordHook.setRecordListener(mediaCodecHook);
    }
}
