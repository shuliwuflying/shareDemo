package com.lm.hook;

import android.text.TextUtils;

import com.lm.hook.bcamera.BCameraHookManager;
import com.lm.hook.fucamera.FuCameraHookManager;
import com.lm.hook.meiyan.MeiYanHookManager;
import com.lm.hook.utils.HookUtils;
import com.lm.hook.utils.LogUtils;
import com.lm.hook.utils.RecordLogUtils;
import com.lm.hook.yyb.YYBHookManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedHookImpl implements IXposedHook {
    private static final String TAG = "XposedHook";
    private static final String SP_NAME = "xposed_hook";
    private static final String PATH = "/sdcard/hook/file.txt";
    private static final String SNOW = "com.campmobile.snowcamera";
    private static final String MY_CAMERA = "com.meitu.meiyancamera";
    private static final String FU_CAMERA = "com.lemon.fucamera";
    private static final String YYB = "com.tencent.android.qqdownloader";
    private List<String> pkgList = new ArrayList<>();
    boolean isInit = false;

    private static IXposedHook hook = new XposedHookImpl();

    private XposedHookImpl() {
        pkgList.add(SNOW);
        pkgList.add(MY_CAMERA);
        pkgList.add(FU_CAMERA);
        pkgList.add(YYB);
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
        if (!isInit) {
            init();
            isInit = true;
        }
        printLog("invokeHook pkg: "+param.packageName +"  pkgList.size: "+pkgList.size());
        printList(pkgList);
        if (pkgList.contains(param.packageName)) {
            printLog("invokeHook pkg: "+param.packageName +"  processName: "+param.processName);
            if (YYB.equals(param.packageName)) {
                YYBHookManager.hook(param);
            }
            if (!param.packageName.equals(param.processName)) {
                return;
            }
            if (MY_CAMERA.equals(param.packageName)) {
                MeiYanHookManager.hook(param);
            } else if(SNOW.equals(param.packageName)) {
                BCameraHookManager.hook(param);
            } else if (FU_CAMERA.equals(param.packageName)) {
                FuCameraHookManager.hook(param);
            }
            RecordLogUtils.init(param.packageName);
        }
    }

    private void printLog(String msg) {
        LogUtils.v(TAG, msg);
    }

    private void printList(List<String> msgList) {
        for(String msg: msgList) {
            printLog(msg);
        }
    }
}