package com.lm.hook.meiyan;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

class PictureHookImpl extends BaseHookImpl {
    private static final String TAG = "LogHookImpl";
    private static final String TAG1 = "LogHookImpl111";
    private static final String TAG2 = "LogHookImpl222";
    private static final String TAG3 = "LogHookImpl3333";


    public PictureHookImpl() {
        hookEntityList.add(getDebugHook("a"));
        hookEntityList.add(getDebugHook("b"));
        hookEntityList.add(getDebugHook("c"));
        hookEntityList.add(getDebugHook("d"));
        hookEntityList.add(getDebugHook("e"));
        hookEntityList.add(getDebugHookF("f"));
        //AccoundSdk

//        hookEntityList.add(getAccountHook("a"));
//        hookEntityList.add(getAccountHook("b"));
//        hookEntityList.add(getAccountHook("c"));
//        hookEntityList.add(getAccountHook("d"));
//        hookEntityList.add(getAccountHook("e"));
//        hookEntityList.add(getAccountHook("f"));

        //MTCameraSDK
//        hookEntityList.add(getMTCameraSdkLog("a"));
//        hookEntityList.add(getMTCameraSdkLog1("b"));
//        hookEntityList.add(getMTCameraSdkLog1("a"));
        hookEntityList.add(getMTCameraImplHook("a"));

        hookEntityList.add(getMTPerformanceData("<init>"));


        //NDebug
        hookEntityList.add(getNDebugHook("d"));
        hookEntityList.add(getNDebugHook("e"));
        hookEntityList.add(getNDebugHook("i"));
        hookEntityList.add(getNDebugHook("v"));
    }



    private MethodSignature getDebugHook(final String methodName) {
        final String targetClz = "com.meitu.library.util.Debug.Debug";
        final Object[] params = new Object[] {
            String.class,
            String.class,
            Throwable.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    StringBuilder sb = new StringBuilder();
                    for(int i =0;i< param.args.length; i++) {
                        if (param.args[i] == null) {
                            continue;
                        }
                        sb.append(param.args[i]);
                        if (i != param.args.length -1) {
                            sb.append(",");
                        }
                    }
                    String value = sb.toString();
                    if (value.contains("calculateFileSize")) {
                        return;
                    }
                    handleLog(value);
                    if(value.contains("Meitu,java.io.IOException")) {
                        LogUtils.e(TAG, android.util.Log.getStackTraceString(new Throwable("IOException")));
                    }
                    LogUtils.i(TAG, "Debug_"+methodName+": "+value);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                }
            }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getDebugHookF(final String methodName) {
        final String targetClz = "com.meitu.library.util.Debug.Debug";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(int i =0;i< param.args.length; i++) {
                            if (param.args[i] == null) {
                                continue;
                            }
                            sb.append(param.args[i]);
                            if (i != param.args.length -1) {
                                sb.append(",");
                            }
                        }
                        LogUtils.i(TAG, "Debug_"+methodName+": "+sb.toString());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }


    private MethodSignature getAccountHook(final String methodName) {
        final String targetClz = "com.meitu.library.account.util.AccountSdkLog";
        final Object[] params = new Object[] {
            String.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    StringBuilder sb = new StringBuilder();
                    for(int i =0;i< param.args.length; i++) {
                        if (param.args[i] == null) {
                            continue;
                        }
                        sb.append(param.args[i]);
                        if (i != param.args.length -1) {
                            sb.append(",");
                        }
                    }
                    LogUtils.i(TAG1, "AccountSdkLog_"+methodName+": "+sb.toString());
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                }
            }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getMTCameraSdkLog(final String methodName) {
        final String targetClz = "com.meitu.library.camera.util.h";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(int i =0;i< param.args.length; i++) {
                            if (param.args[i] == null) {
                                continue;
                            }
                            sb.append(param.args[i]);
                            if (i != param.args.length -1) {
                                sb.append(",");
                            }
                        }
                        LogUtils.i(TAG2, "int$"+methodName+": "+sb.toString());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getMTCameraSdkLog1(final String methodName) {
        final String targetClz = "com.meitu.library.camera.util.h";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                long.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(int i =0;i< param.args.length; i++) {
                            if (param.args[i] == null) {
                                continue;
                            }
                            sb.append(param.args[i]);
                            if (i != param.args.length -1) {
                                sb.append(",");
                            }
                        }
                        handleCameraState(param.args);
                        LogUtils.i(TAG2, "long$"+methodName+": "+sb.toString());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }


    private MethodSignature getMTPerformanceData(final String methodName) {
        final String targetClz = "com.meitu.media.mtmvcore.MTPerformanceData";
        final Object[] params = new Object[] {
                float.class,
                float.class,
                float.class,
                float.class,
                float.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(int i =0;i< param.args.length; i++) {
                            if (param.args[i] == null) {
                                continue;
                            }
                            sb.append(param.args[i]);
                            if (i != param.args.length -1) {
                                sb.append(",");
                            }
                        }
                        LogUtils.i(TAG2, "MTPerformanceData_"+methodName+": "+sb.toString());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }


    private MethodSignature getMTCameraImplHook(String methodName) {
        final String targetClz = "com.meitu.library.camera.util.h";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(true);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(true);
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature getNDebugHook(final String methodName) {
        final String targetClz = "com.meitu.core.types.NDebug";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                Throwable.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(int i =0;i< param.args.length; i++) {
                            if (param.args[i] == null) {
                                continue;
                            }
                            sb.append(param.args[i]);
                            if (i != param.args.length -1) {
                                sb.append(",");
                            }
                        }
                        String value = sb.toString();
                        handleNDebugLog(value);
                        LogUtils.i(TAG, "NDebug_"+methodName+": "+value);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private static void handleLog(String msg) {
        if (msg.contains(CameraAnalysis.TAKE_PICTURE_TAG)) {
            CameraAnalysis.filterTakePictureCost(msg);
        }
    }

    private static void handleNDebugLog(String msg) {
        if (msg.contains(CameraAnalysis.PICTURE_SIZE_TAG) && msg.contains("DCIM")) {
            CameraAnalysis.filterPictureSize(msg);
            CameraAnalysis.filterSaveBitmapCost(msg);
        }
    }

    private static void handleCameraState(Object[] args) {
        if (args.length > 1 && args[0].equals("StateCamera")) {
            String valueStr = args[1].toString();
            String[] values = valueStr.split(":");
            if (values.length == 3 && values[1].contains("cost")) {
                CameraAnalysis.printCameraCost(values[1]+":"+values[2]);
            }
        }
    }

}
