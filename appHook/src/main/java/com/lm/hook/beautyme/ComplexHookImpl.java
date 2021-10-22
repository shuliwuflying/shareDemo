package com.lm.hook.beautyme;

import android.content.Context;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.camera.PreviewHookImpl;
import com.lm.hook.camera.RecordHookImpl;
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
    private final String PIC_SAVE_COST = "picture_save_picture";
    private final String VIDEO_SAVE_COST = "video_save_video";

    private int count = 0;
    private float fpsCount = 0;
    private RecordHookImpl.IRecordListener recordListener;

    private final List<String> filterTagList = Arrays.asList(
            TAKE_PICTURE,
            VIDEO_RECORD,
            LONG_VIDEO,
            PIC_SAVE_COST,
            VIDEO_SAVE_COST
    );

    public ComplexHookImpl(RecordHookImpl.IRecordListener listener) {
        this.recordListener = listener;
    }


    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(getAppLogHook());
        hookEntityList.add(getAlogHook());
        hookEntityList.add(getAlogWHook());
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
                                    String isHd = jsonObject.get("HD_take_mode").toString();
                                    LogUtils.recordLog(TAG, "hd-picture: "+jsonObject.get("HD_take_mode"));
                                    LogUtils.recordLog(TAG, "capture-cost: "+jsonObject.get("time_cost"));
                                    if (isHd.equals("false")) {
                                        LogUtils.recordLog(TAG, PreviewHookImpl.sPictureSize);
                                    }
                                    break;
                                case VIDEO_RECORD:
                                case LONG_VIDEO:
                                    if (recordListener != null) {
                                        recordListener.onRecordFinish(Long.parseLong(String.valueOf(jsonObject.get("duration"))));
                                    }
                                    break;
                                case PIC_SAVE_COST:
//                                    LogUtils.recordLog(TAG, "pic-save-cost: "+jsonObject.get("save_time"));
                                    break;
                                case VIDEO_SAVE_COST:
                                    LogUtils.recordLog(TAG, "video-save-cost: "+jsonObject.get("save_time"));
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
                            String fps = value.substring(FPS_TAG.length()).trim();
                            LogUtils.recordLog(TAG, "render-fps: "+fps);
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature getAlogWHook() {
        final String targetClz = "com.ss.android.agilelogger.ALog";
        final String method = "w";
        final Object[] params = new Object[] {
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String value = param.args[1].toString();
                        if (value.contains(FPS_TAG)){
                            String fps = value.substring(FPS_TAG.length()).trim();
                            LogUtils.recordLog(TAG, "render-fps111: "+fps);
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

}
