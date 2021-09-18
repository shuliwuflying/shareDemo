package com.lm.hook.meiyan;

import androidx.annotation.Keep;

import com.bytedance.hubble.Hubble;
import com.bytedance.hubble.HubbleEntry;

import java.io.File;

public class FileHookManager {
    private static final String TAG = "FileHookManager";

    public static void init() {
        FileHook.hookFile();
        FileHook2.hookFile();
    }

    private static class FileHook {
        static HubbleEntry fileEntry = new HubbleEntry();

        public static void hookFile() {
            //hook
            fileEntry.setTargetConstructor(File.class.getName(), String.class);
            fileEntry.setProxy(FileHook.class.getName(), "newFileProxy", File.class, String.class);
            fileEntry.setOrigin(FileHook.class.getName(), "newFileOrigin", File.class, String.class);
            Hubble.hook(fileEntry);
        }

        @Keep
        public static void newFileProxy(File file, String filepath) {
            android.util.Log.e(TAG, "hookFile call new file: "+filepath);
            Hubble.callOrigin(fileEntry, file, filepath);
        }
        //备份函数
        @Keep
        public static void newFileOrigin(File file, String filepath) {

        }
    }


    private static class FileHook2 {
        static HubbleEntry fileEntry = new HubbleEntry();

        public static void hookFile() {
            //hook
            fileEntry.setTargetConstructor(File.class.getName(), String.class, String.class);
            fileEntry.setProxy(FileHook.class.getName(), "newFileProxy", File.class, String.class, String.class);
            fileEntry.setOrigin(FileHook.class.getName(), "newFileOrigin", File.class, String.class,String.class);
            Hubble.hook(fileEntry);
        }

        @Keep
        public static void newFileProxy(File file, String dir, String filepath) {
            android.util.Log.e(TAG, "FileHook2 call new file: "+dir);
            Hubble.callOrigin(fileEntry, file, dir, filepath);
        }

        @Keep
        public static void newFileOrigin(File file, String dir, String filepath) {

        }
    }
}
