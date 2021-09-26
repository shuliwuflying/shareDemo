package com.lm.hook.meiyan;


import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;


import de.robv.android.xposed.XC_MethodHook;


/**
 * hook release_sig 加载失败，改用SystemLoad(soName)的方式加载
 */
class SoLoadLibraryHookImpl extends BaseHookImpl {
    private static final String TAG = "SoLoadLibraryHookImpl";

    public SoLoadLibraryHookImpl() {
        if (!LaunchHookImpl.isHighVersion) {
            hookEntityList.add(skipReLinkHook());
        }
    }

    private MethodSignature skipReLinkHook() {
        final String targetClz = "com.meitu.remote.hotfix.internal.K";
        final String methodName = "b";
        final Object[] params = new Object[] {
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String soName = param.args[0].toString();
                        if ("release_sig".equals(soName)) {
                            param.setResult(null);
                        }

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(false);
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }
}
