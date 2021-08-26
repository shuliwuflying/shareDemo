package com.lm.hook.meiyan;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * 视频信息获取
 */
class MediaLogHookImpl extends BaseHookImpl {
    private static final String TAG = "MediaLogHookImpl";
    private static final String TARGET_CLZ = "com.meitu.media.tools.utils.debug.Logger";
    private static final String VIDEO_REX ="(?<=(Width |Height |duration |AudioBitrate|VideoBitrate))[0-9]+\\.?[0-9]+";


    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(getMMToolHook("a"));
    }


    private static MethodSignature getMMToolHook(final String methodName) {
        return new MethodSignature(TARGET_CLZ, methodName,
                new Object[] {
                        String.class,
                        String.class,
                        Throwable.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                String value = String.valueOf(param.args[1]);
                                handleMMDebug(value);
                                LogUtils.i(TAG, "mmtool_"+methodName+": "+value);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            }
                        }
                });
    }

    private static void handleMMDebug(String msg) {
        if (msg.contains("[VideoFilterEdit]mVideoWidth")) {
            filterVideoSize(msg);
        }
    }

    public static void filterVideoSize(String msg) {
        Pattern p=Pattern.compile(VIDEO_REX);
        Matcher m=p.matcher(msg);
        StringBuilder sb = new StringBuilder();
        int index = 0;
        sb.append("video");
        String width="";
        String height="";
        String duration="";
        String videoBitrate="";
        String audioBitrate="";
        while (m.find()) {
            String value = m.group();
            LogUtils.e(TAG, "value: "+value);
            if (index == 0) {
                width = value;
            } else if (index == 1) {
                height = value;
            } else if (index == 2) {
                videoBitrate = value;
            } else if (index == 3) {
                audioBitrate = value;
            } else if (index == 4) {
                duration = value;
            }
            index ++;
        }
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, String.format("video-resolution: %s,%s", width,height));
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, String.format("video-bitrate: %s",videoBitrate));
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, String.format("audio-bitrate: %s",audioBitrate));
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, String.format("video-duration: %s",duration));
    }
}
