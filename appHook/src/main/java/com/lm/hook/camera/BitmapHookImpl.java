package com.lm.hook.camera;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.utils.LogUtils;

import java.io.OutputStream;
import java.net.URI;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BitmapHookImpl extends BaseHookImpl {
    private static final String TAG = "BitmapHookImpl";
    private long startTime = 0;

    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(bitmapCompressHook());
        hookEntityList.add(bitmapInsertHook());
        hookEntityList.add(bitmapSendHook());
        hookEntityList.add(bitmapInsertHook2());
    }


    private MethodSignature bitmapCompressHook() {
        return new MethodSignature(Bitmap.class.getName(),"compress",
                new Object[]{
                        Bitmap.CompressFormat.class,
                        int.class,
                        OutputStream.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "bitmapCompressHook beforeHookedMethod");
                                Bitmap bitmap = (Bitmap) param.thisObject;
                                startTime = System.currentTimeMillis();
                                if (bitmap != null) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    LogUtils.e(TAG, "width: "+width+" height: "+height);
                                }

                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "save cost: "+(System.currentTimeMillis() - startTime));
                            }
                        }
                });
    }

    private MethodSignature bitmapInsertHook() {
        return new MethodSignature(MediaStore.Images.Media.class.getName(),"insertImage",
                new Object[]{
                        ContentResolver.class,
                        Bitmap.class,
                        String.class,
                        String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "bitmapInsertHook beforeHookedMethod");
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "save cost: "+(System.currentTimeMillis() - startTime));
                            }
                        }
                });
    }

    private MethodSignature bitmapInsertHook2() {
        return new MethodSignature(MediaStore.Images.Media.class.getName(),"insertImage",
                new Object[]{
                        ContentResolver.class,
                        String.class,
                        String.class,
                        String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "bitmapInsertHook2 beforeHookedMethod");
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "save cost: "+(System.currentTimeMillis() - startTime));
                            }
                        }
                });
    }



    private MethodSignature bitmapSendHook() {
        return new MethodSignature(Intent.class.getName(),"<init>",
                new Object[]{
                        String.class,
                        Uri.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "bitmapSendHook beforeHookedMethod");
                                LogUtils.e(TAG, android.util.Log.getStackTraceString(new Throwable("bitmapSendHook")));
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtils.e(TAG, "pic-save-cost: "+(System.currentTimeMillis() - startTime));
                            }
                        }
                });
    }
}
