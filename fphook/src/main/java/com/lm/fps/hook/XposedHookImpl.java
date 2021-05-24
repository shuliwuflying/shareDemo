package com.lm.fps.hook;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;


import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class XposedHookImpl implements IXposedHook {
    private static final String TAG = "XposedHook";
    private static final String SP_NAME = "xposed_hook";
    private static final String PATH = "/sdcard/hook/file.txt";
    private List<String> pkgList = new ArrayList<>();
    private long startTime = 0;
    private int count = 0;
    boolean isInited = false;
    private List<FrameMonitor> monitorList = new ArrayList<>();
    private List<TextureView> textureViewList = new ArrayList<>();

    private static IXposedHook hook = new XposedHookImpl();

    private XposedHookImpl() {

    }

    public static IXposedHook  getInstance() {
        return hook;
    }


    @Override
    public void init() {
        try {
            File file = new File(PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            RandomAccessFile raf = new RandomAccessFile(PATH, "rw");
            while(true) {
                String pkg = raf.readLine();
                if (TextUtils.isEmpty(pkg) || TextUtils.isEmpty(pkg.trim())) {
                    break;
                }
                pkgList.add(pkg);
            }
            raf.close();
            printLog("init pkgList.size: "+pkgList.size());
            printList(pkgList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addHookTarget(String pkgName) {
        pkgList.add(pkgName);
        try {
            FileOutputStream fos = new FileOutputStream(PATH, true);
            fos.write(pkgName.getBytes());
            fos.write("\n".getBytes());
            fos.flush();
            fos.close();
            printLog("init pkgList.size: "+pkgList.size());
            printList(pkgList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        printLog("addHookTarget pkgList.size: "+pkgList.size());
        printList(pkgList);
    }

    @Override
    public void invokeHook(XC_LoadPackage.LoadPackageParam param) {
        if (!isInited) {
            init();
            isInited = true;
        }
        printLog("invokeHook pkg: "+param.packageName +"  pkgList.size: "+pkgList.size());
        printList(pkgList);
        if (pkgList.contains(param.packageName)) {
            findAndHookMethod("android.view.TextureView", param.classLoader, "onAttachedToWindow",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Object object = param.thisObject;
                            printLog("onAttachedToWindow: "+object);
                            if (object instanceof TextureView) {
                                TextureView view = (TextureView)object;
                                textureViewList.add(view);
                            }
                        }
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        }
                    }
            );

            findAndHookMethod("android.graphics.SurfaceTexture", param.classLoader, "setOnFrameAvailableListener",
                    SurfaceTexture.OnFrameAvailableListener.class,
                    Handler.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            for(Object object: param.args) {
                                printLog("object: "+object);
                            }
                            FrameMonitor monitor = new FrameMonitor();
                            monitor.startTime = 0;
                            monitor.count = 0;
                            monitorList.add(monitor);
                            TextureView view = getTextureView((SurfaceTexture)param.thisObject);
                            if (view != null) {
                                monitor.viewId = getViewTag(view);
                                param.args[0] = new FrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)param.args[0],monitor.viewId);
                            }
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        }
                    }
            );
        }
    }


    private void printLog(String msg) {
        android.util.Log.w(TAG,msg);
    }

    private void printList(List<String> list) {
        for(String value: list) {
            printLog("pkg11111: "+value);
        }
    }


    private class FrameAvailableListener implements SurfaceTexture.OnFrameAvailableListener {

        SurfaceTexture.OnFrameAvailableListener mListener;
        String tag = "";
        private long startTime = 0;
        private int count = 0;


        FrameAvailableListener(SurfaceTexture.OnFrameAvailableListener listener, String tag) {
            mListener = listener;
            this.tag = tag;
        }


        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            FrameMonitor monitor = getFrameMonitor(tag);
            if (monitor == null) {
                printLog("monitor == null");
                mListener.onFrameAvailable(surfaceTexture);
                return;
            }
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }

            if (monitor.count == 0) {
                monitor.startTime = System.currentTimeMillis();
            }
            monitor.count ++;
            count ++;
            long timeDuration = System.currentTimeMillis() - monitor.startTime;
            if (Math.abs(timeDuration - 1000) < 15 || timeDuration > 1000) {
                String averageFps = String.format("%.2f",(count*1000.0/(System.currentTimeMillis() - startTime)));
                printLog(monitor.viewId+"   fps: "+monitor.count  + "   monitor time: "+timeDuration+ "  average fps: "+ averageFps);
                monitor.count = 0;
                monitor.startTime = System.currentTimeMillis();
            }
            mListener.onFrameAvailable(surfaceTexture);
        }
    }

    private FrameMonitor getFrameMonitor(String tag) {
        for(FrameMonitor monitor: monitorList) {
            if (tag.equals(monitor.viewId)) {
                return monitor;
            }
        }
        return null;
    }

    private TextureView getTextureView(SurfaceTexture texture) {
        for(TextureView textureView: textureViewList) {
            if (textureView.getSurfaceTexture() == texture) {
                return textureView;
            }
        }
        return null;
    }


    private static class FrameMonitor {
        int count = 0;
        long startTime = 0;
        SurfaceTexture.OnFrameAvailableListener listener;
        String viewId = "";
    }

    /**
     * 获取View 的Tag 值
     * @param view
     * @return
     * example: com.bilibili.videoeditor.sdk.BLiveWindow{e4b9d2b V.ED..... ........ 0,122-1080,729 #7f0901fe app:id/live_window_preview}
     */
    private String getViewTag(View view) {

        String value = view.toString();
        int indexSplit1 = value.indexOf("{");
        int indexSplit2 = value.indexOf("}");
        if (indexSplit1 <=0 || indexSplit2 <= 0) {
            return Long.toHexString(view.getId());
        }
        StringBuilder sb = new StringBuilder();
        String prefixString = value.substring(0,indexSplit1);
        String targetString = value.substring(indexSplit1+1, indexSplit2);
        String className = prefixString.substring((prefixString.lastIndexOf("."))+1);
        printLog("className: "+className);
        sb.append(className);
        String matchTargetString = ".. ";
        int rectFirstIndex = targetString.lastIndexOf(matchTargetString);
        int rectLastIndex = targetString.indexOf("#");
        if (rectFirstIndex != -1 && rectLastIndex != -1) {
            String rect = targetString.substring(rectFirstIndex+matchTargetString.length(), rectLastIndex);
            printLog("rect: "+rect);
            if (!TextUtils.isEmpty(rect)){
                sb.append(" - ");
                sb.append(rect);
            }
        }

        int idFirstIndex = targetString.lastIndexOf("/");
        String idValue = targetString.substring(idFirstIndex + 1);
        printLog("idValue: "+idValue);
        if (!TextUtils.isEmpty(idValue)) {
            sb.append("- ");
            sb.append(idValue);
        }
        return sb.toString();
    }

}
