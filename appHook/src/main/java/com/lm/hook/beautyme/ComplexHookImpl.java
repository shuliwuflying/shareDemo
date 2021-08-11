package com.lm.hook.beautyme;

import android.content.Context;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ComplexHookImpl extends BaseHookImpl {
    private final String TAG = "ComplexHookImpl";
    private final String TAKE_PICTURE = "take_picture_available";
    private final String VIDEO_RECORD = "take_video_available";
    private final String LONG_VIDEO = "take_long_video_available";
    private final String FPS_TAG = "on draw frame fps:";
    private int count = 0;
    private float fpsCount = 0;
    private final List<String> filterTagList = Arrays.asList(
            TAKE_PICTURE,
            VIDEO_RECORD,
            LONG_VIDEO
    );


    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(getAppLogHook());
        hookEntityList.add(getAlogHook());
    }

    private MethodSignature getAppLogHook() {
        final String targetClz = "com.ss.android.common.applog.AppLog";
        final String method = "onEvent";
        final Object[] params = new Object[] {
                Context.class,
                String.class,
                String.class,
                String.class,
                long.class,
                long.class,
                boolean.class,
                JSONObject.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String tag = param.args[2].toString();
                        LogUtils.e(TAG, "onEvent: "+tag);
                        if (filterTagList.contains(tag)) {
                            JSONObject jsonObject = (JSONObject) param.args[7];
                            LogUtils.e(TAG, "onEvent: "+param.args[2]+"  value: "+jsonObject.toString());
                            switch (tag) {
                                case TAKE_PICTURE:
                                    LogUtils.recordLog(TAG, "hd-picture:"+jsonObject.get("HD_take_mode"));
                                    LogUtils.recordLog(TAG, "capture-cost:"+jsonObject.get("time_cost"));
                                    break;
                                case VIDEO_RECORD:
                                    LogUtils.recordLog(TAG, "video-duration:"+jsonObject.get("duration"));
                                    break;
                                case LONG_VIDEO:
                                    LogUtils.recordLog(TAG, "video-duration:"+jsonObject.get("duration"));
                                    break;
                            }
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature getAlogHook() {
        final String targetClz = "com.ss.android.agilelogger.ALog";
        final String method = "i";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String value = param.args[1].toString();
                        if (value.contains(FPS_TAG)){
                            count++;
                            String fps = value.substring(FPS_TAG.length()).trim();
                            fpsCount += Float.parseFloat(fps);
                            if (count == 5) {
                                LogUtils.recordLog(TAG, "render-fps:"+fpsCount/5);
                                count = 0;
                                fpsCount = 0;
                            }
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }
}
