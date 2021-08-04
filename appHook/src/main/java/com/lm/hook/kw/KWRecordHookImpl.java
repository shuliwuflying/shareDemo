package com.lm.hook.kw;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class KWRecordHookImpl extends BaseHookImpl {
    private static final String TAG = "KWRecordHookImpl";
    private static final String TARGET_CLZ = "com.kwai.camerasdk.mediarecorder.MediaRecorderImpl";
    private static final String PARAMS_CLZ = "com.kwai.camerasdk.mediarecorder.MediaRecorderImpl$InternalListener";
    private static String sVideo_resolution = "";

    private static long sStartRecordTs = 0;
    private static long sStopRecordTs = 0;
    private static long sRecordDuration = 0;


    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        try {
            hookEntityList.add(startRecordHook());
            hookEntityList.add(stopRecordHook());
            hookEntityList.add(mediaCodecEncodeHook());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private MethodSignature startRecordHook() throws ClassNotFoundException {
        final String methodName = "nativeStartRecording";
        final Class<?> paramClz = hookParam.classLoader.loadClass(PARAMS_CLZ);
        final Object[] params = new Object[] {
                long.class,
                String.class,
                boolean.class,
                boolean.class,
                long.class,
                float.class,
                int.class,
                boolean.class,
                boolean.class,
                int.class,
                String.class,
                byte[].class,
                paramClz,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        for(Object obj: param.args) {
                            sb.append(obj);
                            sb.append(",");
                        }
                        LogUtils.e(TAG, "nativeStartRecording: "+sb.toString());
                        sStartRecordTs = System.currentTimeMillis();
                        sRecordDuration = 0;
                        LogUtils.e(TAG, "sStartRecordTs: "+sStartRecordTs);
                    }
                }
        };
        return new MethodSignature(TARGET_CLZ, methodName, params);
    }

    private MethodSignature stopRecordHook() {
        final String methodName = "stopRecording";
        final Object[] params = new Object[] {
                boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        sStopRecordTs = System.currentTimeMillis();
                        sRecordDuration += sStopRecordTs - sStartRecordTs;
                        LogUtils.recordLog(TAG, sVideo_resolution);
                        LogUtils.recordLog(TAG, "video-duration:"+sRecordDuration);
                        LogUtils.e(TAG, "sStopRecordTs: "+sStopRecordTs+"   param: "+param.args[0]+"  sRecordDuration: "+sRecordDuration);
                    }
                }
        };
        return new MethodSignature(TARGET_CLZ, methodName, params);
    }

    private MethodSignature mediaCodecEncodeHook() {
//        int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean
        final String targetClz = "com.kwai.camerasdk.encoder.MediaCodecEncoder";
        final String methodName = "initEncode";
        final Object[] params = new Object[] {
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        sVideo_resolution = "video-resolution: "+param.args[2]+","+param.args[3];
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }

    private MethodSignature recordFinishHook() {
        final String methodName = "release";
        final Object[] params = new Object[] {
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        sStopRecordTs = System.currentTimeMillis();
                        sRecordDuration += sStopRecordTs - sStartRecordTs;
                        LogUtils.recordLog(TAG, "record_duration: "+sRecordDuration);
                    }
                }
        };
        return new MethodSignature(TARGET_CLZ, methodName, params);
    }
}
