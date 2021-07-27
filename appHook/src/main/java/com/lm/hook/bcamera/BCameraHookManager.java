package com.lm.hook.bcamera;

import com.lm.hook.base.LaunchHookImpl;
import com.lm.hook.base.LogHookIml;
import com.lm.hook.base.PreviewFpsHookImpl;
import com.lm.hook.camera.BitmapHookImpl;
import com.lm.hook.camera.CameraHookImpl;
import com.lm.hook.camera.ParamsHookImpl;
import com.lm.hook.base.AdHookImpl;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BCameraHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        new ParamsHookImpl().init(param);
        new CameraHookImpl().init(param);
        new RenderFpsHookImpl().init(param);
        new BitmapHookImpl().init(param);
        new PreviewFpsHookImpl().init(param);
        new LaunchHookImpl().init(param);
        new LogHookIml().init(param);
        new AdHookImpl().init(param);
    }
}
