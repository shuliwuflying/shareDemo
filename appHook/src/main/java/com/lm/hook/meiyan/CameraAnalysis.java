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
    private static final String VIDEO_REX = "(?<=(Width|Height|duration)) [0-9]+\\.?[0-9]+";
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
            LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "takePicture cost: "+sTakePictureCost);
        }
    }

    public static void filterPictureSize(String msg) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "Hd capture "+isHdCapture);
        Pattern p=Pattern.compile(SIZE_REX);
        Matcher m=p.matcher(msg);
        if (m.find()) {
            String valueStr = m.group();
            if (!TextUtils.isEmpty(valueStr) && valueStr.contains(",")) {
                String[] values = valueStr.split(",");
                sTakePictureSize = "width: "+values[0]+", height: "+values[1];
                LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "takePicture size: "+sTakePictureSize);
            } else {
                LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "takePicture size: " + valueStr);
            }
        }
        isHdCapture = false;
    }

    public static void filterSaveBitmapCost(String msg) {
        Pattern p=Pattern.compile(SAVE_COST_REX);
        Matcher m=p.matcher(msg);
        if (m.find()) {
            String valueStr = m.group();
            LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "savePicture cost: "+valueStr);
        }
    }

    public static void printPreviewFps(String fps) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "preview fps: "+fps);
    }

    public static void printRenderFps(String fps) {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "render fps: "+fps);
    }

    public static void filterVideoSize(String msg) {
        Pattern p=Pattern.compile(VIDEO_REX);
        Matcher m=p.matcher(msg);
        StringBuilder sb = new StringBuilder();
        int index = 0;
        sb.append("video");

        while (m.find()) {
            index ++;
            String value = m.group();
            if (index == 0) {
                sb.append(" width: ");
                sb.append(value);
            } else if (index == 1) {
                sb.append(" height: ");
                sb.append(value);
            } else {
                sb.append(" duration: ");
                try {
                    float floadValue = Float.parseFloat(value);
                    sb.append(floadValue*1000);
                } catch (Exception e) {
                    sb.append(value);
                    e.printStackTrace();
                }
            }


        }
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, sb.toString());
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
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, msg);
    }
}
