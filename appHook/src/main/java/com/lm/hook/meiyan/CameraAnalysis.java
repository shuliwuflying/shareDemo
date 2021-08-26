package com.lm.hook.meiyan;

import android.text.TextUtils;
import android.util.Log;

import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.LogUtils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraAnalysis {

    static final String TAKE_PICTURE_TAG ="handle take picture";
    static final String PICTURE_SIZE_TAG = "MteSkiaImageLoader saveImageToDisk";

    private static final String COST_REX = "(?<=\\[)\\d+ms(?=\\])";
    private static final String SIZE_REX =  "(?<=(\\] \\[)).*(?=\\] use)";
    private static final String SAVE_COST_REX = "(?<=(\\] use ))\\d+ ms";

    private static CameraAnalysis sAnalysis;
    private static String sTakePictureCost = "";
    private static String sTakePictureSize = "";
    public static boolean isHdCapture = false;
    public static boolean isPreviewParamsSet = false;

    private CameraAnalysis() {

    }

    public static CameraAnalysis getInstance() {
        return Holder._INSTANCE;
    }

    private static class Holder {
        private static CameraAnalysis _INSTANCE = new CameraAnalysis();
    }


    public static void filterTakePictureCost(String msg) {
        Pattern p=Pattern.compile(COST_REX);
        Matcher m=p.matcher(msg);
        if (m.find()) {
            sTakePictureCost = m.group();
            LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "capture-cost: "+sTakePictureCost);
        }
    }

    public static void filterPictureSize(String msg) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "hd-capture: "+isHdCapture);
        Pattern p=Pattern.compile(SIZE_REX);
        Matcher m=p.matcher(msg);
        if (m.find()) {
            String valueStr = m.group();
            if (!TextUtils.isEmpty(valueStr) && valueStr.contains(",")) {
                String[] values = valueStr.split(",");
                sTakePictureSize = ""+values[0]+","+values[1];
                LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "capture-size: "+sTakePictureSize);
            } else {
                LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "capture-size: " + valueStr);
            }
        }
        isHdCapture = false;
    }

    public static void filterSaveBitmapCost(String msg) {
        Pattern p=Pattern.compile(SAVE_COST_REX);
        Matcher m=p.matcher(msg);
        if (m.find()) {
            String valueStr = m.group();
            LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "save-cost: "+valueStr);
        }
    }

    public static void printPreviewFps(String fps) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "preview-fps: "+fps);
    }

    public static void printRenderFps(String fps) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "render-fps: "+fps);
    }


    public static void printCameraFace(String msg) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, msg);
    }

    public static void printCameraCost(String msg) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, msg);
    }

    public static void printPreviewSize(String msg) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, msg);
    }

    public static void printCameraCharacters(String msg) {
        if (isPreviewParamsSet) {
            return;
        }
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, msg);
    }

    public static void print(String msg) {
        LogUtils.e(ConstantUtils.MY_LOG_TAG, msg);
    }
}
