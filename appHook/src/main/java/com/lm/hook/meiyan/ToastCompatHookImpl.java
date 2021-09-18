package com.lm.hook.meiyan;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

class ToastCompatHookImpl extends BaseHookImpl {
    private static final String TAG = "ToastCompatHookImpl";

    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        if (Build.VERSION.SDK_INT >= 30) {
            hookEntityList.add(getToastCompatHook());
        }
//        hookEntityList.add(getAlertDialogHook());
//        FileHookManager.init();
        LogUtils.e("HookImpl", "fileHookManager hookEntityList.size: "+hookEntityList.size());
    }

    private MethodSignature getToastCompatHook() {
        return new MethodSignature("android.widget.Toast",
                "show",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "before getToastCompatHook");
                                param.setResult(null);

                            }
                        }
                }
        );
    }


    private MethodSignature getAlertDialogHook() {
        return new MethodSignature(AlertDialog.Builder.class.getName(), "<init>", new Object[] {
                Context.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "before getAlertDialogHook");
                        for(Object obj: param.args) {
                            LogUtils.e(TAG, "obj: "+obj);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "after getAlertDialogHook");
                    }

                }
        });
    }
}
