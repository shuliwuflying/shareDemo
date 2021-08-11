package com.lm.hook.bcamera;

import com.lm.hook.base.LogHookIml;
import com.lm.hook.camera.PreviewFpsHookImpl;
import com.lm.hook.camera.BitmapHookImpl;
import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.ParamsHookImpl;
import com.lm.hook.base.AdHookImpl;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BCameraHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        LaunchHookImpl hook = new LaunchHookImpl();
        hook.init(param);
        new ParamsHookImpl().init(param);
        new CameraStageHookImpl(hook).init(param);
        new RenderFpsHookImpl(hook).init(param);
        new BitmapHookImpl().init(param);
        new PreviewFpsHookImpl(hook).init(param);
        new LaunchHookImpl().init(param);
        new LogHookIml().init(param);
        new AdHookImpl().init(param);
    }
}
