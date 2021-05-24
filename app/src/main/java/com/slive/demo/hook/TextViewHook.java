package com.slive.demo.hook;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.TextView;

import com.creation.ultrasonic.IProbe;
import com.creation.ultrasonic.impl.Examination;
import com.creation.ultrasonic.impl.hook.MethodSign;
import com.creation.ultrasonic.impl.hook.fasthook.FastHookProbe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class TextViewHook extends Examination {
    private static final String TAG = "sliver";
    private static int count = 0;
    private static Map<Integer, Object> map = new HashMap<>();

    @Override
    protected ArrayList<IProbe> initProbeList() {
        // 返回用去监控的IProbe list
        ArrayList<IProbe> arrayList = new ArrayList<>();
        arrayList.add(new HandleRunHookItem());
        arrayList.add(new HandleTextViewMeasure());
        return arrayList;
    }


    private static class HandleRunHookItem extends FastHookProbe {
        public static String[][] HOOK_ITEMS;

        public HandleRunHookItem() {

        }


        @Override
        protected MethodSign getMethodSign() {
            Class<?> hookClz = null;
            try {
                hookClz = Class.forName("android.text.TextLine");
                android.util.Log.e("sliver", "hookClz: "+hookClz);
                Method method = hookClz.getDeclaredMethod("handleRun",new Class[]{int.class, int.class, int.class, boolean.class,
                        Canvas.class, float.class, int.class, int.class, int.class, Paint.FontMetricsInt.class, boolean.class});
                android.util.Log.e("sliver", "Method: "+method);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return MethodSign.create(hookClz,"handleRun", new Class[]{int.class, int.class, int.class, boolean.class,
                    Canvas.class, float.class, int.class, int.class, int.class, Paint.FontMetricsInt.class, boolean.class}, float.class);
        }

        public static int hook(Object object, int start, int measureLimit,
                               int limit, boolean runIsRtl, Canvas c, float x, int top, int y,
                               int bottom, Paint.FontMetricsInt fmi, boolean needWidth) {

            android.util.Log.e("sliver", "start: "+start+"   measureLimit: "+measureLimit+"   fmi: "+fmi);
            int ret = backup(object, start, measureLimit, limit, runIsRtl, c, x, top, y, bottom, fmi, needWidth);
            return ret;
        }

        public static native int backup(Object object, int start, int measureLimit,
                                        int limit, boolean runIsRtl, Canvas c, float x, int top, int y,
                                        int bottom, Paint.FontMetricsInt fmi, boolean needWidth);
    }


    private static class HandleTextViewMeasure extends FastHookProbe {
        public static String[][] HOOK_ITEMS;

        public HandleTextViewMeasure() {

        }


        @Override
        protected MethodSign getMethodSign() {
            return MethodSign.createVoid(TextView.class,"onMeasure",new Class[]{int.class, int.class});
        }

        public static void hook(TextView view, int w, int h) {
            if (view.getText().length() == 0) {
                if (!map.containsKey(view.getId())) {
                    android.util.Log.e("sliver", "view: "+view);
                    map.put(view.getId(), view);
                }

            }
            backup(view,w, h);
        }

        public static native void backup(TextView view, int w, int h);
    }

}
