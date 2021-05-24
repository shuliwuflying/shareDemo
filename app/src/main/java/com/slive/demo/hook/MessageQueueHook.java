package com.slive.demo.hook;

import android.os.Message;
import android.os.MessageQueue;

import com.creation.ultrasonic.IProbe;
import com.creation.ultrasonic.impl.Examination;
import com.creation.ultrasonic.impl.hook.MethodSign;
import com.creation.ultrasonic.impl.hook.fasthook.FastHookProbe;

import java.util.ArrayList;

public class MessageQueueHook extends Examination {

    @Override
    protected ArrayList<IProbe> initProbeList() {
        // 返回用去监控的IProbe list
        ArrayList<IProbe> arrayList = new ArrayList<>();
        arrayList.add(new MessageQueueHookItem());
        return arrayList;
    }


    private static class MessageQueueHookItem extends FastHookProbe {
        public static String[][] HOOK_ITEMS;

        public MessageQueueHookItem() {

        }


        @Override
        protected MethodSign getMethodSign() {
            return MethodSign.createSimple(MessageQueue.class,"next", Message.class);
        }


        public static Message hook(MessageQueue queue) {
            android.util.Log.e("sliver", "MessageQueueHook1111111");
            Message msg = backup(queue);
            android.util.Log.e("sliver", "MessageQueueHook: "+msg);
            return msg;
        }

        public static native Message backup(MessageQueue queue);

    }

}
