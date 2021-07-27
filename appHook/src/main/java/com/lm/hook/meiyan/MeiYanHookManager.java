package com.lm.hook.meiyan;

import com.lm.hook.base.PreviewFpsHookImpl;
import com.lm.hook.base.ClassHookImpl;
import com.lm.hook.camera.BitmapHookImpl;
import com.lm.hook.camera.CameraHookImpl;
import com.lm.hook.camera.ParamsHookImpl;
import com.lm.hook.base.AdHookImpl;
import com.lm.hook.utils.RecordLogUtils;

import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * 美颜相机hook功能
 */
public class MeiYanHookManager {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        new ParamsHookImpl().init(param);
        new PreviewFpsHookImpl().init(param);
        new CameraHookImpl().init(param);
        new RenderFpsHookImpl().init(param);
        new AdHookImpl().init(param);
        new SoLoadLibraryHookImpl().init(param);
        new SignatureHookImpl().init(param);
        new LogHookImpl().init(param);
        new ReportHookImpl().init(param);
    }
}
