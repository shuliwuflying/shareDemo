package com.lm.hook.camera;

import android.media.MediaRecorder;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

/**
 * 视频录制hook
 */
public class RecordHookImpl extends BaseHookImpl {
    private static final String TAG = "RecordHookImpl";

    private static long sStartRecordTs = 0;
    private static long sStopRecordTs = 0;
    private static long sRecordDuration = 0;

    public RecordHookImpl() {
        hookEntityList.add(startRecordHook());
        hookEntityList.add(stopRecordHook());
        hookEntityList.add(recordFinishHook());
    }

    private MethodSignature startRecordHook() {
        final String targetClass = MediaRecorder.class.getName();
        final String methodName = "start";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        sStartRecordTs = System.currentTimeMillis();
                        LogUtils.e(TAG, "sStartRecordTs: "+sStartRecordTs);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                }
        };
        return new MethodSignature(targetClass, methodName, params);
    }

    private MethodSignature stopRecordHook() {
        final String targetClass = MediaRecorder.class.getName();
        final String methodName = "stop";
        final Object[] params = new Object[] {
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        sStopRecordTs = System.currentTimeMillis();
                        sRecordDuration += sStopRecordTs - sStartRecordTs;
                        LogUtils.e(TAG, "sStopRecordTs: "+sStopRecordTs);
                    }
                }
        };
        return new MethodSignature(targetClass, methodName, params);
    }

    private MethodSignature recordFinishHook() {
        final String targetClass = MediaRecorder.class.getName();
        final String methodName = "release";
        final Object[] params = new Object[] {
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        sStopRecordTs = System.currentTimeMillis();
                        sRecordDuration += sStopRecordTs - sStartRecordTs;
                        LogUtils.e(TAG, "record_duration: "+sRecordDuration);
                    }
                }
        };
        return new MethodSignature(targetClass, methodName, params);
    }
}
