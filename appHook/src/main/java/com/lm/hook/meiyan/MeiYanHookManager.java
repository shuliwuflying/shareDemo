package com.lm.hook.meiyan;

import com.lm.hook.camera.HdCaptureHookImpl;
import com.lm.hook.camera.PreviewFpsHookImpl;
import com.lm.hook.camera.CameraStageHookImpl;
import com.lm.hook.camera.ParamsHookImpl;
import com.lm.hook.base.AdHookImpl;
import com.lm.hook.camera.RecordHookImpl;

import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * 美颜相机hook功能
 */
public class MeiYanHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        LaunchHookImpl launchHook = new LaunchHookImpl();
        launchHook.init(param);
        new ParamsHookImpl().init(param);
        new PreviewFpsHookImpl(launchHook).init(param);
        new CameraStageHookImpl(launchHook).init(param);
        new HdCaptureHookImpl().init(param);
        new RenderFpsHookImpl().init(param);
        new AdHookImpl().init(param);
        new SoLoadLibraryHookImpl().init(param);
        new SignatureHookImpl().init(param);
        new PictureHookImpl().init(param);
        new ReportHookImpl().init(param);
        new RecordHookImpl().init(param);
    }
}
