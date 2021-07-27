package com.lm.hook.base;


import android.app.Application;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

import static android.content.pm.PackageManager.GET_SIGNATURES;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class LaunchHookImpl extends BaseHookImpl {
    public static final String TAG = "LaunchHookImpl";
    private static long sAppCreateTime = 0L;
    private static long sFirstDrawFrameTime = 0L;

    public LaunchHookImpl() {
        hookEntityList.add(getApplicationHook());
    }


    private MethodSignature getApplicationHook() {
        return new MethodSignature(
                "com.linecorp.b612.android.B612Application",
                "onCreate",
                new Object[]{new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        sAppCreateTime = System.currentTimeMillis();
                        LogUtils.e(TAG, "param.thisObject: "+ param.thisObject);
                        try {
                            Application application = (Application)param.thisObject;
                            String pkgName = application.getPackageName();
                            String signatures = application.getPackageManager().getPackageInfo(pkgName, GET_SIGNATURES).signatures[0].toCharsString();
                            LogUtils.e(TAG, "pkgName: "+pkgName+"   signatures: "+signatures);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
                });
    }

    public static void recordFirstDrawFrame() {
        sFirstDrawFrameTime = System.currentTimeMillis();
        LogUtils.e(TAG, "launch cost time: "+(sFirstDrawFrameTime - sAppCreateTime));
    }

}
