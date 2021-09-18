//
// Created by liwu shu on 2020/12/8.
//
#include <cstdlib>
#include <cstdint>
#include <cinttypes>
#include <cstring>
#include <ctime>
#include <sys/types.h>
#include <sys/stat.h>
#include <cstdio>
#include <jni.h>
#include <libgen.h>
#include <android/log.h>
#include "bytehook.h"


#define HACKER_JNI_VERSION    JNI_VERSION_1_6
#define SAMPLE_JNI_CLASS_NAME "com/slive/demo/utils/NativeHook"
#define SAMPLE_TAG            "sliver"


#pragma clang optimize off
static void sample_strlen(JNIEnv *env, jclass thiz)
{
    (void)env;
    (void)thiz;

    __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "pre strlen()");
    ::strlen("my-password-12345678");
    __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "post strlen()");
}




#pragma clang optimize on


static int test_add(int a, int b) {
    __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "a: %d, b:%d", a,b);
    volatile int c = 0;
    c = a+b;
    __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "c: ", c);
    return a+b;
}

static void sample_test_add(JNIEnv *env, jclass thiz)
{
    __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "pre test_add()");
    test_add(1,2);
    __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "post test_add()");
    jclass testClz = env->FindClass("com/slive/demo/hook/TestObj");
    jmethodID construction_id = env->GetMethodID(testClz, "<init>","()V");
    jobject testObj = env->NewObject(testClz,construction_id);
    jmethodID  testMethod = env->GetMethodID(testClz, "addNum", "()V");
    env->CallVoidMethod(testObj, testMethod);
    if(env->ExceptionCheck()) {
        __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "sample_test_add occur Exception");
        env->ExceptionDescribe();
//        __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "ExceptionDescribe");
        env->NewStringUTF("Hello world11111");
//        __android_log_print(ANDROID_LOG_DEBUG, SAMPLE_TAG, "sample_test_add occur Exception11111");
    }
//    } else {
//        env->NewStringUTF("Hello world");
//    }
}

JNIEXPORT jobject JNICALL
Java_com_slive_demo_utils_NativeHook_nativeGeneratorSig(JNIEnv *env, jclass clazz,
                                                        jstring param_string1,
                                                        jobjectArray param_array_of_byte,
                                                        jstring param_string2,
                                                        jobject param_object) {
    return NULL;
}


static JNINativeMethod sample_jni_methods[] = {
        {
                "nativeTestStrlen",
                "()V",
                reinterpret_cast<void *>(sample_strlen)
        },

        {
            "nativeTestAdd",
                    "()V",
                    reinterpret_cast<void *>(sample_test_add)
        }
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    (void)reserved;

    if(nullptr == vm) return JNI_ERR;

    JNIEnv *env;
    if(JNI_OK != vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6)) return JNI_ERR;
    if(nullptr == env) return JNI_ERR;

    jclass cls = env->FindClass(SAMPLE_JNI_CLASS_NAME);
    if(nullptr == cls) return JNI_ERR;

    if(0 != env->RegisterNatives(cls, sample_jni_methods, sizeof(sample_jni_methods) / sizeof(sample_jni_methods[0]))) return JNI_ERR;

    return JNI_VERSION_1_6;
}

