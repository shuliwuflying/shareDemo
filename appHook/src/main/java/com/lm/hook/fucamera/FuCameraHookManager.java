package com.lm.hook.fucamera;

import com.lm.hook.base.PreviewFpsHookImpl;
import com.lm.hook.camera.CameraHookImpl;
import com.lm.hook.camera.ParamsHookImpl;


import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FuCameraHookManager {
    private static final String TAG = "xposedHook";

    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        android.util.Log.e(TAG, "FuCameraHookManager");
        new ParamsHookImpl().init(param);
        new PreviewFpsHookImpl().init(param);
        new CameraHookImpl().init(param);
    }
}
