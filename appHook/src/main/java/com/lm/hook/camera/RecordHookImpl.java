package com.lm.hook.camera;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaSyncEvent;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;

/**
 * 视频录制hook
 */
public class RecordHookImpl extends BaseHookImpl {
    private static final String TAG = "RecordHookImpl";

    private long sStartRecordTs = 0;
    private long sStopRecordTs = 0;
    private long sRecordDuration = 0;
    private boolean isRecordFinish = true;
    private Object audioRecord = null;
    private IRecordListener recordListener;

    public RecordHookImpl() {
        hookEntityList.add(startRecordHook1());
        hookEntityList.add(startRecordHook2());
        hookEntityList.add(stopRecordHook());
        hookEntityList.add(recordFinishHook());
    }

    public void setRecordListener(IRecordListener recordListener) {
        this.recordListener = recordListener;
    }

    private MethodSignature startRecordHook1() {
        final String targetClass = AudioRecord.class.getName();
        final String methodName = "startRecording";
        final Object[] params = new Object[] {
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        isRecordFinish = false;
                        sStartRecordTs = System.currentTimeMillis();
                        LogUtils.e(TAG, "startRecording: "+param.thisObject);
                        LogUtils.e(TAG, "sStartRecordTs: "+sStartRecordTs);
                        audioRecord = param.thisObject;
                    }
                }
        };
        return new MethodSignature(targetClass, methodName, params);
    }

    private MethodSignature startRecordHook2() {
        final String targetClass = AudioRecord.class.getName();
        final String methodName = "startRecording";
        final Object[] params = new Object[] {
                MediaSyncEvent.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        isRecordFinish = false;
                        LogUtils.e(TAG, "startRecording: "+param.thisObject);
                        sStartRecordTs = System.currentTimeMillis();
                        LogUtils.e(TAG, "sStartRecordTs222: "+sStartRecordTs);
                        audioRecord = param.thisObject;
                    }
                }
        };
        return new MethodSignature(targetClass, methodName, params);
    }


    private MethodSignature stopRecordHook() {
        final String targetClass = AudioRecord.class.getName();
        final String methodName = "stop";
        final Object[] params = new Object[] {
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "stop: "+param.thisObject);
                        recordFinish(param);
                    }
                }
        };
        return new MethodSignature(targetClass, methodName, params);
    }

    private MethodSignature recordFinishHook() {
        final String targetClass = AudioRecord.class.getName();
        final String methodName = "release";
        final Object[] params = new Object[] {
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "release: "+param.thisObject);
                        recordFinish(param);
                    }
                }
        };
        return new MethodSignature(targetClass, methodName, params);
    }

    private void recordFinish(XC_MethodHook.MethodHookParam param) {
        if (!isRecordFinish && param.thisObject == audioRecord) {
            isRecordFinish = true;
            sStopRecordTs = System.currentTimeMillis();
            sRecordDuration += sStopRecordTs - sStartRecordTs;
            if (recordListener != null) {
                recordListener.onRecordFinish(sRecordDuration);
            }
            sRecordDuration = 0;
        }
    }


    public interface IRecordListener {
        void onRecordFinish(long duration);
    }
}
