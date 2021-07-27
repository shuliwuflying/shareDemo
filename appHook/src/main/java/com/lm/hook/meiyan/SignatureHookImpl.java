package com.lm.hook.meiyan;

import android.content.Context;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.HookUtils;
import com.lm.hook.utils.LogUtils;

import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;


/**
 * 签名hook，解决网络资源不下发的问题
 */
class SignatureHookImpl extends BaseHookImpl {
    private static final String TAG = "SignatureHookImpl";

    public SignatureHookImpl() {
//        hookEntityList.add(getSignatureMethod());
//        hookEntityList.add(getSigEntryConstructor1());
//        hookEntityList.add(getSigEntryConstructor2());
        hookEntityList.add(generatorSigHook());
        hookEntityList.add(generatorSigHook2());
    }

    @Override
    public void hookImpl() {

    }

    private MethodSignature getSignatureMethod() {
        return new MethodSignature("com.meitu.iap.core.util.SigTool",
                "createSigEntity",
                new Object[]{
                        String.class,
                        HashMap.class,
                        Context.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "before createSigEntity");

                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "after createSigEntity");
                            }
                        }
                }
        );
    }

    private MethodSignature getSigEntryConstructor1() {
        final String targetClz = "com.meitu.secret.SigEntity";
        final String method = "<init>";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "before createSigEntity111");
                        LogUtils.e(TAG, android.util.Log.getStackTraceString(new Throwable("createSigEntity111")));
                        for(Object obj: param.args) {
                            LogUtils.e(TAG, "after createSigEntity111 obj: "+obj.toString());
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "after createSigEntity111");

                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature getSigEntryConstructor3() {
        final String targetClz = "com.meitu.secret.SigEntity";
        final String method = "<clinit>";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "before createSigEntity111");
                        LogUtils.e(TAG, android.util.Log.getStackTraceString(new Throwable("createSigEntity111")));
                        for(Object obj: param.args) {
                            LogUtils.e(TAG, "after createSigEntity111 obj: "+obj.toString());
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "after createSigEntity111");

                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature getSigEntryConstructor2() {
        final String targetClz = "com.meitu.secret.SigEntity";
        final String method = "<init>";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "before createSigEntity222");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "after createSigEntity222");
                        for(Object obj: param.args) {
                            LogUtils.e(TAG, "after createSigEntity222 obj: "+obj.toString());
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }


    private MethodSignature generatorSigHook() {
        final String targetClz = "com.meitu.secret.SigEntity";
        final String method = "generatorSig";
        final Object[] params = new Object[] {
                String.class,
                String[].class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        LogUtils.e(TAG, "before generatorSigHook111");
//                        for(Object obj: param.args) {
//                            LogUtils.e(TAG, "before generatorSigHook111 obj: "+obj.toString());
//                        }
                        HookUtils.revertOrigin(hookParam.classLoader);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "after generatorSigHook111");
                        HookUtils.revertProxy(hookParam.classLoader);
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }


    private MethodSignature generatorSigHook2() {
        final String targetClz = "com.meitu.secret.SigEntity";
        final String method = "generatorSig";
        final Object[] params = new Object[] {
                String.class,
                String[].class,
                String.class,
                Object.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        LogUtils.e(TAG, "before generatorSigHook222");
                        HookUtils.revertOrigin(hookParam.classLoader);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "after generatorSigHook222: "+param.getResult());
                        HookUtils.revertProxy(hookParam.classLoader);
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature open_p_hook() {
        final String targetClz = "com.meitu.library.account.open";
        final String method = "P";
        final Object[] params = new Object[]{
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "before open_p_hook");
                        param.setResult(null);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "after open_p_hook");
                        param.setResult(false);

                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }
}
