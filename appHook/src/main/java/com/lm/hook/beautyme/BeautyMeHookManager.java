package com.lm.hook.beautyme;

import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.HdCaptureHookImpl;
import com.lm.hook.camera.ParamsHookImpl;
import com.lm.hook.camera.PreviewFpsHookImpl;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BeautyMeHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        LaunchHookImpl launchHook = new LaunchHookImpl();
        launchHook.init(param);
        new ParamsHookImpl().init(param);
        new PreviewFpsHookImpl(launchHook).init(param);
        new CameraStageHookImpl(launchHook).init(param);
        new HdCaptureHookImpl().init(param);
        new ComplexHookImpl().init(param);
    }
}
