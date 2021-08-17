package com.lm.hook.lv;

import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.HdCaptureHookImpl;
import com.lm.hook.camera.NormalRenderFpsHook;
import com.lm.hook.camera.ParamsHookImpl;
import com.lm.hook.camera.PreviewFpsHookImpl;


import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class LvCameraHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        new CameraStageHookImpl(null).init(param);
        new ParamsHookImpl().init(param);
        new HdCaptureHookImpl().init(param);
        new PreviewFpsHookImpl(null).init(param);
        new NormalRenderFpsHook().init(param);
    }
}
