package com.slive.demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Trace;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.libcamera.CameraActivity;
import com.slive.demo.hook.NewLaunchHook;
import com.slive.demo.utils.ActivityStack;
import com.slive.demo.utils.BLog;
import com.vega.core.utils.OOMTraceUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends BaseActivity {

    LottieAnimationView lottieAnimationView;
    TextView playBtn;
    TextView watchBtn;
    TextView createFileBtn;
    TextView deleteFileBtn;
    ImageView testImage1, testImage2;
    int clickCount= 0;
    private Handler uiHandler = new Handler();
    private Handler workHandler;
    private String fileRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM"+ File.separator + "Camera" + File.separator;
    private String fileName = "1611491538416.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        watchBtn = (TextView) findViewById(R.id.watch);
        NewLaunchHook.init();
        new Thread().start();
        new Thread(new Runnable() {
            public void run() {
                watchBtn.setText("test111111");
                watchBtn.invalidate();
//                test();
            }
        }).start();
        long startTime = System.currentTimeMillis();
        try {
            System.loadLibrary("aaa");
        } catch (Throwable e){
            e.printStackTrace();
        }
        android.util.Log.e("sliver", "setDataDirectorySuffix cost: "+(System.currentTimeMillis() - startTime));
        SharedPreferences sp = getSharedPreferences("sp_test", Context.MODE_PRIVATE);
//        try {
//            Class<?> webClz = Class.forName("android.webkit.WebViewFactory");
//            Method[] methods = webClz.getDeclaredMethods();
//            for(Method m: methods) {
//                Log.e("sliver", "method: "+m.getName());
//            }
//            Method method = webClz.getDeclaredMethod("setDataDirectorySuffix", String.class);
//            if (method != null) {
//                method.setAccessible(true);
//            }
//            method.invoke(webClz, "aaaa");
//            Log.e("sliver", "webClz: "+webClz);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        NewLaunchHook.hookLibrary();
//        System.loadLibrary("libfasthook.so");
        ActivityStack.add(this);
//        try {
//            android.util.Log.e("sliver","onCreate");
//            Thread.sleep(10 * 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        test();
        initViews();
        initWorkHandler();
        Looper.getMainLooper().getQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                android.util.Log.e("sliver","queueIdle invoke");
                return true;
            }
        });
        ViewServer.get(this).addWindow(this);
        initService(MyService.class);
        initService(MyService2.class);
    }

    private void initWorkHandler() {
        HandlerThread handlerThread = new HandlerThread("print-queue");
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper());
//        printQueue();
    }

    private void printQueue() {
        workHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    android.util.Log.e("sliver", "MessageQueue isIdle: "+Looper.getMainLooper().getQueue().isIdle());
                    MessageQueue queue = Looper.getMainLooper().getQueue();
                    Field msgHeader = queue.getClass().getDeclaredField("mMessages");
                    msgHeader.setAccessible(true);
                    Message msg = (Message)msgHeader.get(queue);
                    int count = 0;
                    int token = -1;
                    while(msg != null) {
                        android.util.Log.e("sliver", "msg: "+msg);
                        if (msg.getTarget() == null && token == -1) {
                            token = msg.arg1;
                        }
                        Field next = msg.getClass().getDeclaredField("next");
                        next.setAccessible(true);
                        Object nextValue = next.get(msg);
                        if (nextValue != null) {
                            msg = (Message) nextValue;
                        } else {
                            msg = null;
                        }
                        count ++;
                    }
                    android.util.Log.e("sliver", "count: "+count +"  token: "+token);
//                    if (count > 20 && token != -1) {
//                        removeSyncBarrier(token);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                test();
                printQueue();
            }
        }, 5000);
    }

    @Override
    protected void onStart() {
        Log.e("sliver","onStart");
        super.onStart();
        Log.e("sliver","onStart111111");
    }

    @Override
    protected void onResume() {
//        try {
//            android.util.Log.e("sliver","onResume");
//            Thread.sleep(10 * 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    public void initViews() {
        Trace.beginSection("initView");
        Log.e("sliver","initViews");
        BLog.e("sliver","initViews");
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.create_room_animation_view);
        playBtn = (TextView) findViewById(R.id.play);
        watchBtn = (TextView) findViewById(R.id.watch);
        createFileBtn = (TextView) findViewById(R.id.create_file);
        deleteFileBtn = (TextView) findViewById(R.id.delete_file);
        createFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/test/activity").navigation(MainActivity.this, new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {
                        Log.e("sliver", "onFound");
                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        Log.e("sliver", "onLost");
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                        Log.e("sliver", "onArrival");
                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {
                        Log.e("sliver", "onInterrupt");
                    }
                });
            }
        });
        deleteFileBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                ARouter.getInstance().build(Uri.parse("arouter://m.aliyun.com/test/activity")).navigation();
                if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},1000);
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                LottieTask<LottieComposition> lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(MainActivity.this, "file:///android_asset/video.json");
//                lottieCompositionLottieTask.addListener(new LottieListener<LottieComposition>() {
//                    @Override
//                    public void onResult(LottieComposition result) {
//                        lottieAnimationView.setComposition(result);
//                        lottieAnimationView.setRepeatCount(2);
//                        lottieAnimationView.playAnimation();
//                    }
//                });
//                lottieCompositionLottieTask.addFailureListener(new LottieListener<Throwable>() {
//
//                    @Override
//                    public void onResult(Throwable result) {
//                        android.util.Log.e("sliver", "result: "+result);
//                    }
//                });
//                lottieAnimationView.playAnimation();
                OOMTraceUtils traceUtils = new OOMTraceUtils();
                traceUtils.getExtraMemoryTrace();
            }
        });
        watchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MVVMActivity.class);
                startActivity(intent);
            }
        });
        testImage1 = findViewById(R.id.test_image);
        testImage2 = findViewById(R.id.test_image2);
        testImage2.setAlpha(0.5f);
        try {
            Thread.sleep(200);
        } catch (Exception e) {

        }
        Trace.endSection();

    }

    private void test() {
        playBtn = (TextView) findViewById(R.id.play);
        new Thread(new Runnable() {
            public void run() {
                clickCount++;
                if(watchBtn.getParent() != null && watchBtn.getParent() instanceof ViewGroup) {
                    ((ViewGroup)(watchBtn.getParent())).invalidate();
                }
//                postSyncBarrier();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                clickCount++;
                if(watchBtn.getParent() != null && watchBtn.getParent() instanceof ViewGroup) {
                    ((ViewGroup)(watchBtn.getParent())).invalidate();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                clickCount++;
                if(watchBtn.getParent() != null && watchBtn.getParent() instanceof ViewGroup) {
                    ((ViewGroup)(watchBtn.getParent())).invalidate();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                clickCount++;
                if(watchBtn.getParent() != null && watchBtn.getParent() instanceof ViewGroup) {
                    ((ViewGroup)(watchBtn.getParent())).invalidate();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                clickCount++;
                if(watchBtn.getParent() != null && watchBtn.getParent() instanceof ViewGroup) {
                    ((ViewGroup)(watchBtn.getParent())).invalidate();
                }
            }
        }).start();
    }

    private void play(String value) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("sliver", "post runnable: $value");
            }
        });
    }

    private void  startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            String filepath = getExternalFilesDir("")+File.separator + "test.jpg";
//            File file = new File(filepath);
//            if(!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            startActivityForResult(takePictureIntent, 9527);
//        }
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo resolveInfo: list) {
            Log.e("sliver","resolveInfo: "+resolveInfo);
            ActivityInfo info = resolveInfo.activityInfo;
            if (info != null) {
                Log.e("sliver","info: "+info);
            }
        }

        ResolveInfo defaultInfo  = getPackageManager().resolveActivity(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (defaultInfo != null) {
            Log.e("sliver","defaultInfo: "+defaultInfo.activityInfo.packageName);
        }

    }

    private void postSyncBarrier() {
        try {
            Class<?> msgQueueClz = MessageQueue.class;
            Method postSyncBarrier = msgQueueClz.getDeclaredMethod("postSyncBarrier");
            postSyncBarrier.invoke(Looper.getMainLooper().getQueue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSyncBarrier(int token) {
        try {
            Class<?> msgQueueClz = MessageQueue.class;
            Method postSyncBarrier = msgQueueClz.getDeclaredMethod("removeSyncBarrier");
            postSyncBarrier.invoke(Looper.getMainLooper().getQueue(), token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        Log.e("sliver","onStop111111");
//        try {
//            Thread.sleep(60 * 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Log.e("sliver","onStop222222");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    private void initService(Class<?> targetClz) {
        Intent intent = new Intent(this, targetClz);
        startService(intent);
    }

}