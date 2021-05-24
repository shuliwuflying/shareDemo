package com.slive.demo.hook;



import com.creation.ultrasonic.DoctorSingleton;
import com.creation.ultrasonic.IExamination;
import com.creation.ultrasonic.IProbe;
import com.creation.ultrasonic.LineWords;
import com.creation.ultrasonic.impl.Examination;
import com.creation.ultrasonic.impl.hook.MethodSign;
import com.creation.ultrasonic.impl.hook.fasthook.FastHookProbe;

import java.util.ArrayList;

public class LaunchHook extends Examination {

    public static void initHook() {
        DoctorSingleton.instance().initExamination(
                new LineWords(
                        "com.lemon.",
                        "com.android.maya.",
                        "com.maya.",
                        "my.maya.",
                        "com.bytedance.",
                        "com.ss."
                ),
                IExamination.Type.Bitmap
        );
        DoctorSingleton.instance().addProbe(new LaunchHook());
        DoctorSingleton.instance().start();
    }

    @Override
    protected ArrayList<IProbe> initProbeList() {
        ArrayList<IProbe> list = new ArrayList<>();
//        list.add(new SpHookItem());
//        list.add(new LibHookItem());
        return list;
    }

//    private static class SpHookItem extends FastHookProbe {
//        public static String[][] HOOK_ITEMS;
//        //getSharedPreferences
//
//        @Override
//        protected MethodSign getMethodSign() {
//            Class<?> clz = null;
//            try {
//                clz = Class.forName("android.app.ContextImpl");
//            } catch (Exception e) {
//
//            }
//            return MethodSign.create(clz, "getSharedPreferences", new Class[]{String.class, int.class}, SharedPreferences.class);
//        }
//
//        public static SharedPreferences hook(Object context, String name, int mode) {
//            android.util.Log.e("sliver","SpHook name: "+name + "  thread: "+Thread.currentThread());
//            return backup(context,name, mode);
//        }
//
//        public static native SharedPreferences backup(Object context, String name, int mode);
//    }
//
//    private static class LibHookItem extends FastHookProbe {
//        public static String[][] HOOK_ITEMS;
//
//        @Override
//        protected MethodSign getMethodSign() { ;
//            return MethodSign.createVoid(System.class, "loadLibrary", new Class[]{String.class});
//        }
//
//        public static void hook(String name) {
//            android.util.Log.e("sliver","libHook name: "+name + "  thread: "+Thread.currentThread());
//            backup(name);
//        }
//        public static native void backup(String name);
//    }
//
//    private static class WebHookItem extends FastHookProbe {
//        public static String[][] HOOK_ITEMS;
//
//        @Override
//        protected MethodSign getMethodSign() {
//            try {
//                Class<?> webClz = Class.forName("android.webkit.WebViewFactory");
//                return MethodSign.createVoid(webClz, "setDataDirectorySuffix", new Class[]{String.class});
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        public static void hook(String name) {
//            android.util.Log.e("sliver","WebHookItem name: "+name + "  thread: "+Thread.currentThread());
//            backup(name);
//        }
//        public static native void backup(String name);
//    }

    public static class ThreadPoolHook extends FastHookProbe {
        public static String[][] HOOK_ITEMS;

        @Override
        protected MethodSign getMethodSign() {
            try {
                Class<?> webClz = Class.forName("com.lm.components.threadpool.ThreadPoolUtil");
                return MethodSign.createVoid(webClz, "submitTask", new Class[]{Runnable.class, String.class, long.class});
            } catch (Exception e) {
                return null;
            }
        }

        public static void hook(String name) {
            android.util.Log.e("sliver","WebHookItem name: "+name + "  thread: "+Thread.currentThread());
            backup(name);
        }
        public static native void backup(String name);
    }
}
