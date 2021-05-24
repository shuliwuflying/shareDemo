package com.slive.demo.hook;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;

import com.creation.ultrasonic.IProbe;
import com.creation.ultrasonic.impl.Examination;
import com.creation.ultrasonic.impl.hook.MethodSign;
import com.creation.ultrasonic.impl.hook.fasthook.FastHookProbe;

import java.util.ArrayList;

public class SyncBarrierHook extends Examination {
    private static final String TAG = "sliver";
    private static int count = 0;

    @Override
    protected ArrayList<IProbe> initProbeList() {
        // 返回用去监控的IProbe list
        ArrayList<IProbe> arrayList = new ArrayList<>();
        arrayList.add(new MessageQueueHook());
        arrayList.add(new MessageQueueHook1());
        return arrayList;
    }


    private static class BarrierHookItem extends FastHookProbe {
        public static String[][] HOOK_ITEMS;

        public BarrierHookItem() {

        }


        @Override
        protected MethodSign getMethodSign() {
            return MethodSign.createSimple(MessageQueue.class,"postSyncBarrier", int.class);
        }


        public static int hook(MessageQueue queue) {

            count ++;
//            android.util.Log.e(TAG, "postSyncBarrier count: "+count + "thread: "+ Looper.myLooper());
            int ret = backup(queue);
            return ret;
        }

        public static native int backup(MessageQueue queue);
    }

    private static class BarrierHookItem2 extends FastHookProbe {
        public static String[][] HOOK_ITEMS;

        public BarrierHookItem2() {

        }


        @Override
        protected MethodSign getMethodSign() {
            return MethodSign.createVoid(MessageQueue.class,"removeSyncBarrier", new Class<?>[]{int.class});
        }

        public static int hook(MessageQueue queue, int token) {
            count --;
            android.util.Log.e(TAG, "removeSyncBarrier count: "+count +"  token: "+token);
            int ret = backup(queue, token);
            return ret;
        }

        public static native int backup(MessageQueue queue, int token);
    }

    private static class MessageQueueHook extends FastHookProbe {
        public static String[][] HOOK_ITEMS;

        public MessageQueueHook() {

        }


        @Override
        protected MethodSign getMethodSign() {
            return MethodSign.createSimple(MessageQueue.class,"next", Message.class);
        }


        public static Message hook(MessageQueue queue) {
            android.util.Log.e(TAG, "000000000000000 MessageQueue next begin");
            Message ret = backup(queue);
            android.util.Log.e(TAG, "000000000000000 MessageQueue next end: "+ret);
            return ret;
        }

        public static native Message backup(MessageQueue queue);
    }

    private static class MessageQueueHook1 extends FastHookProbe {
        public static String[][] HOOK_ITEMS;

        public MessageQueueHook1() {

        }


        @Override
        protected MethodSign getMethodSign() {
            return MethodSign.createVoid(MessageQueue.class,"nativePollOnce", new Class[]{long.class, int.class});
        }


        public static void hook(MessageQueue queue, long ptr, int timeOut) {
            android.util.Log.e(TAG, "1111111111 MessageQueue nativePollOnce begin");
            long time = System.currentTimeMillis();
            backup(queue, ptr, timeOut);
            android.util.Log.e(TAG, "1111111111 MessageQueue nativePollOnce finish timeOut: "+timeOut+"   duration: "+(System.currentTimeMillis() - time));
        }

        public static native void backup(MessageQueue queue, long ptr, int timeOut);
    }

}
