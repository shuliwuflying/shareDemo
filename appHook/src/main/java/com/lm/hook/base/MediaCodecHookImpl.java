package com.lm.hook.base;

import android.media.MediaFormat;

import com.lm.hook.camera.RecordHookImpl;
import com.lm.hook.utils.LogUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MediaCodecHookImpl extends BaseHookImpl implements RecordHookImpl.IRecordListener {
    private static final String TAG = "MediaCodecHookImpl";
    public static String sWidth = "";
    public static String sHeight = "";
    public static String sBitrate = "";
    private static String sMime = "";

    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(getVideoResolutionHook());
        hookEntityList.add(getVideoBitrateHook());
    }

    private MethodSignature getVideoResolutionHook() {
        final String targetClz = MediaFormat.class.getName();
        final String method = "createVideoFormat";
        final Object[] params = new Object[] {
            String.class,
            int.class,
            int.class,
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String mime = String.valueOf(param.args[0]);
                    if (mime.contains("video")) {
                        sMime = mime;
                        sWidth = String.valueOf(param.args[1]);
                        sHeight = String.valueOf(param.args[2]);
                        LogUtils.e(TAG, "beforeHookedMethod mime: " + mime + "  width: " + sWidth + "  height: " + sHeight + "  " + param.getResult());
                    }
                }
            }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature getVideoBitrateHook() {
        final String targetClz = MediaFormat.class.getName();
        final String method = "setInteger";
        final Object[] params = new Object[] {
                String.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        MediaFormat mediaFormat = (MediaFormat) param.thisObject;
                        String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                        if (sMime != null && sMime.equals(mime)) {
                            String value = String.valueOf(param.args[0]);
                            if (value.equals("bitrate")) {
                                sBitrate = String.valueOf(param.args[1]);
                                LogUtils.e(TAG, "beforeHookedMethod sBitrate: "+sBitrate + "   "+param.thisObject);
                            }
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    @Override
    public void onRecordFinish(long duration) {
        LogUtils.recordLog(TAG, "video-duration: " + duration);
        LogUtils.recordLog(TAG, String.format("video-resolution: %s,%s",MediaCodecHookImpl.sWidth,MediaCodecHookImpl.sHeight));
        LogUtils.recordLog(TAG, String.format("video-bitrate: %s", MediaCodecHookImpl.sBitrate));
    }
}
