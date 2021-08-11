package com.lm.hook.kw;

import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.HdCaptureHookImpl;
import com.lm.hook.camera.ParamsHookImpl;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class KWCameraHookManager {
    private static final String TAG = "xposedHook";

    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        LaunchHookImpl launchHook = new LaunchHookImpl();
        launchHook.init(param);
        new CameraStageHookImpl(launchHook).init(param);
        new ParamsHookImpl().init(param);
        new HdCaptureHookImpl().init(param);
        new ContextHookImpl().init(param);
//        new KwReportHook().init(param);
        new KWLogHookImpl().init(param);
        new RenderFpsHookImpl().init(param);
        new KWRecordHookImpl().init(param);
        new NormalCaptureHookImpl().init(param);

    }
}
