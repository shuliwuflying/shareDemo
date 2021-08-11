package com.lm.hook.camera;

import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.lm.hook.base.BaseHookImpl;
import com.lm.hook.base.LaunchHookBaseImpl;
import com.lm.hook.meiyan.CameraAnalysis;
import com.lm.hook.utils.ConstantUtils;
import com.lm.hook.utils.HookUtils;
import com.lm.hook.utils.LogUtils;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * camera 各阶段hook
 */
public class CameraStageHookImpl extends BaseHookImpl {
    private static final String TAG = "CameraStageHookImpl";
    private static long sOpenStart = 0;
    private static long sPreviewStart = 0;
    private static long sStopPreviewStart = 0;
    private static long sCloseCameraStart = 0;
    private static long sFirstFrameReceive = 0;
    private static CameraStateCallback sCameraStateCallback;
    private static CaptureCallback captureCallback = null;
    private static Object cameraObj = null;

    private LaunchHookBaseImpl mLaunchHookBase;

    public CameraStageHookImpl(LaunchHookBaseImpl launchHookBase) {
        mLaunchHookBase = launchHookBase;
    }

    @Override
    protected void prepare(XC_LoadPackage.LoadPackageParam hookParam) {
        hookEntityList.add(openCameraV1Hook());
        hookEntityList.add(openCameraV2Hook());
        hookEntityList.add(startPreviewV1Hook());
        if(hookParam.packageName.equals(ConstantUtils.PkgName.KW_CAMERA)) {
            hookEntityList.add(previewCallbackHook());
        }
        hookEntityList.add(stopPreviewV1Hook());
        hookEntityList.add(createSessionHook());
        hookEntityList.add(startPreviewV2Hook());
        hookEntityList.add(imageReaderFrameHook());
        hookEntityList.add(stopPreviewV2Hook());
        hookEntityList.add(closeCameraV1Hook());
        hookEntityList.add(closeCameraV2Hook());
    }

    private static MethodSignature openCameraV1Hook() {
        return new MethodSignature(Camera.class.getName(), "open",
                new Object[]{
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (param.args[0] != null) {
                                    CameraAnalysis.printCameraFace("camera_level: V1");
                                    onOpenCameraBefore(param);
                                }
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                onOpenCameraFinish();
                                cameraObj = param.getResult();
                            }
                        }
                });
    }

    private static MethodSignature closeCameraV1Hook() {
        return new MethodSignature(Camera.class.getName(), "release",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                onCloseCameraBefore(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                Object camera = param.thisObject;
                                if (camera == cameraObj) {
                                    onCloseCameraFinish();
                                    cameraObj = null;
                                }
                            }
                        }
                });
    }


    private static MethodSignature closeCameraV2Hook() {
        return new MethodSignature("android.hardware.camera2.impl.CameraDeviceImpl", "close",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                onCloseCameraBefore(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                onCloseCameraFinish();
                            }
                        }
                });
    }

    private static MethodSignature startPreviewV1Hook() {
        return new MethodSignature(Camera.class.getName(), "startPreview",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                onStartPreviewBefore(param);
                            }
                        }
                });
    }

    private MethodSignature previewCallbackHook() {
        final String targetClz = Camera.class.getName();
        final String method = "setPreviewCallbackWithBuffer";
        final Object[] methodParams = new Object[] {
                Camera.PreviewCallback.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        PreviewCallback listener = new PreviewCallback((Camera.PreviewCallback) param.args[0]);
                        param.args[0] = listener;
                    }
                }
        };
        return new MethodSignature(targetClz, method, methodParams);
    }


    private static MethodSignature stopPreviewV1Hook() {
        return new MethodSignature(Camera.class.getName(), "stopPreview",
                new Object[]{
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                onStopPreviewBefore(param);
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                onStopPreviewFinish();
                            }
                        }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static MethodSignature openCameraV2Hook() {
        return new MethodSignature(CameraManager.class.getName(), "openCamera",
                new Object[]{
                        String.class,
                        CameraDevice.StateCallback.class,
                        Handler.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                CameraAnalysis.printCameraFace("camera_level: V2");
                                onOpenCameraBefore(param);

                                sCameraStateCallback = new CameraStateCallback((CameraDevice.StateCallback) param.args[1]);
                                param.args[1] = sCameraStateCallback;
                            }
                        }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static MethodSignature createSessionHook() {
        final String targetClz = "android.hardware.camera2.impl.CameraDeviceImpl";
        final String methodName = "createCaptureSession";
        final Object[] params = new Object[] {
                List.class,
                CameraCaptureSession.StateCallback.class,
                Handler.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "createCaptureSession before");
                        List<Surface> list = (List) param.args[0];
                        for(Surface surface: list) {
                            LogUtils.e(TAG, "createCaptureSession surface: "+surface);
                        }
                        onStartPreviewBefore(param);
                    }
                }

        };
        return new MethodSignature(targetClz, methodName,params);
    }

    private static MethodSignature createRequestHook() {
        final String targetClz = "android.hardware.camera2.impl.CameraDeviceImpl";
        final String method = "createCaptureRequest";
        final Object[] params = new Object[] {
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (Integer.parseInt(param.args[0].toString()) == CameraDevice.TEMPLATE_PREVIEW) {
                            LogUtils.e(TAG, "createCaptureRequest before ");
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private static MethodSignature startPreviewV2Hook() {
        final String targetClz = "android.hardware.camera2.impl.CameraCaptureSessionImpl";
        final String method = "setRepeatingRequest";
        final Object[] params = new Object[]{
                CaptureRequest.class,
                CameraCaptureSession.CaptureCallback.class,
                Handler.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "setRepeatingRequest before");
                        onStartPreviewBefore(param);
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private static MethodSignature stopPreviewV2Hook() {
        final String targetClz = "android.hardware.camera2.impl.CameraCaptureSessionImpl";
        final String method = "stopRepeating";
        final Object[] params = new Object[] {
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.e(TAG, "stopRepeating before");
                        onStopPreviewBefore(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        LogUtils.e(TAG, "stopRepeating after");
                        onStopPreviewFinish();
                    }
                }
        };
        return new MethodSignature(targetClz, method, params);
    }

    private MethodSignature imageReaderFrameHook() {
        final String targetClz = "android.media.ImageReader";
        final String methodName = "setOnImageAvailableListener";
        final Object[] params = new Object[] {
                ImageReader.OnImageAvailableListener.class,
                Handler.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        ImageReader imageReader = (ImageReader) param.thisObject;
                        int previewW = ParamsHookImpl.previewWidth;
                        int previewH = ParamsHookImpl.previewHeight;
                        Object obj = param.args[0];
                        if (obj != null) {
                            if (imageReader.getHeight() == previewW && imageReader.getWidth() == previewH) {
                                ImageReader.OnImageAvailableListener listener = (ImageReader.OnImageAvailableListener) obj;
                                param.args[0] = new ImageOnFrameListener(listener);
                            }
                        }
                    }
                }
        };
        return new MethodSignature(targetClz, methodName, params);
    }



    private static class CameraStateCallback extends CameraDevice.StateCallback {
        private CameraDevice.StateCallback realCallback;

        CameraStateCallback(CameraDevice.StateCallback callback) {
            realCallback = callback;
        }

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            LogUtils.e(TAG, "camera: "+camera);
            onOpenCameraFinish();
            realCallback.onOpened(camera);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            LogUtils.e(TAG, "onDisconnected");
            realCallback.onDisconnected(camera);
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            LogUtils.e(TAG, "onError");
            realCallback.onError(camera, error);
        }
    }


    private static void onOpenCameraBefore(XC_MethodHook.MethodHookParam param) {
        sOpenStart = System.currentTimeMillis();
        Integer value = Integer.parseInt(String.valueOf(param.args[0]));
        CameraAnalysis.printCameraFace("facing: " + (value == 1 ? "front" : "back"));
        HookUtils.initHandler();
    }

    private static void onOpenCameraFinish() {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "open-camera-cost: " + (System.currentTimeMillis() - sOpenStart));
    }

    private static void onCloseCameraBefore(XC_MethodHook.MethodHookParam param) {
        sCloseCameraStart = System.currentTimeMillis();
    }

    private static void onCloseCameraFinish() {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "close-camera-cost: " + (System.currentTimeMillis() - sCloseCameraStart));
    }

    private static void onStartPreviewBefore(XC_MethodHook.MethodHookParam param) {
        LogUtils.e(TAG, "onStartPreviewBefore");
        if (sPreviewStart == 0) {
            sPreviewStart = System.currentTimeMillis();
        }
    }

    private void onStartPreviewFinish() {
        LogUtils.e(TAG, "onStartPreviewFinish: "+(System.currentTimeMillis() - sPreviewStart));
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "start-preview-cost: " + (System.currentTimeMillis() - sPreviewStart));
        sPreviewStart = 0;
        mLaunchHookBase.setFirstReceiveFrame();
    }

    private static void onStopPreviewBefore(XC_MethodHook.MethodHookParam param) {
        sStopPreviewStart = System.currentTimeMillis();
    }

    private static void onStopPreviewFinish() {
        LogUtils.recordLog(ConstantUtils.MY_LOG_TAG, "stop-preview-cost: " + (System.currentTimeMillis() - sStopPreviewStart));
    }

    public void recordFirstFrameReceive() {
        if (sFirstFrameReceive ==0) {
            sFirstFrameReceive = System.currentTimeMillis();
            mLaunchHookBase.setFirstReceiveFrame();
            onStartPreviewFinish();
        }
    }

    static class CaptureCallback extends CameraCaptureSession.CaptureCallback {
        CameraCaptureSession.CaptureCallback captureCallback;

        public CaptureCallback(CameraCaptureSession.CaptureCallback callback) {
            captureCallback = callback;
        }

        @Override
        public void onCaptureStarted(CameraCaptureSession session,
                                     CaptureRequest request, long timestamp, long frameNumber) {
            captureCallback.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        public CameraCaptureSession.CaptureCallback getCaptureCallback() {
            return captureCallback;
        }
    }

    private static CameraCaptureSession.CaptureCallback getCameraCallback(CameraCaptureSession.CaptureCallback callback) {
        if (captureCallback != null && callback == captureCallback.getCaptureCallback()) {
            return captureCallback;
        }
        captureCallback = new CaptureCallback(callback);
        return captureCallback;
    }

    class ImageOnFrameListener implements ImageReader.OnImageAvailableListener {
        ImageReader.OnImageAvailableListener imageAvailableListener;

        public ImageOnFrameListener(ImageReader.OnImageAvailableListener listener) {
            LogUtils.e(TAG, "ImageOnFrameListener");
            imageAvailableListener = listener;
        }

        @Override
        public void onImageAvailable(ImageReader reader) {
            if (sPreviewStart > 0) {
                onStartPreviewFinish();
            }
            imageAvailableListener.onImageAvailable(reader);
        }
    }

    private class PreviewCallback implements Camera.PreviewCallback {

        Camera.PreviewCallback previewCallback;
        private long startTime = 0;
        private int count = 0;


        PreviewCallback(Camera.PreviewCallback  callback) {
            previewCallback = callback;
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            if(sPreviewStart > 0) {
                onStartPreviewFinish();
            }

            count++;
            long timeDuration = System.currentTimeMillis() - startTime;
            if (Math.abs(timeDuration - ConstantUtils.TIME_DURATION_PRINT_FPS) < 15 || timeDuration >= ConstantUtils.TIME_DURATION_PRINT_FPS) {
                LogUtils.e(TAG,"onFrameAvailable thread: "+Thread.currentThread());
                CameraAnalysis.printPreviewFps(String.format("%.2f",count*1.0f/ConstantUtils.TIME_STAMP_COUNT));
                CameraAnalysis.isPreviewParamsSet = true;
                count = 0;
                startTime = System.currentTimeMillis();
            }
            if (previewCallback != null) {
                previewCallback.onPreviewFrame(data, camera);
            }
        }
    }
}
