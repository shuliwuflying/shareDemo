#include <cstdlib>
#include <cstdint>
#include <cinttypes>
#include <cstring>
#include <jni.h>
#include <android/log.h>
#include "bytehook.h"
//#include "loop_data.h"

#define HACKER_JNI_VERSION    JNI_VERSION_1_6
#define HACKER_JNI_CLASS_NAME "com/bytedance/android/hacker/NativeHacker"
#define HACKER_TAG            "native_hook"

static bytehook_stub_t hacker_strlen_stub = nullptr;

class FontFakery {
public:
    FontFakery() : mFakeBold(false), mFakeItalic(false) { }
    FontFakery(bool fakeBold, bool fakeItalic) : mFakeBold(fakeBold), mFakeItalic(fakeItalic) { }
    // TODO: want to support graded fake bolding
    bool isFakeBold() { return mFakeBold; }
    bool isFakeItalic() { return mFakeItalic; }
private:
    bool mFakeBold;
    bool mFakeItalic;
};


typedef union epoll_data {
    void* ptr;
    int fd;
    uint32_t u32;
    uint64_t u64;
} epoll_data_t;

/** The type representing an epoll() event. */
struct epoll_event {
    uint32_t events;
    epoll_data_t data;
};

// callback notifier
static void hacker_hooked_callback(bytehook_stub_t task_stub, int status_code, const char *caller_path_name, const char *sym_name, void *new_func, void *prev_func, void *arg)
{
    __android_log_print(ANDROID_LOG_ERROR, HACKER_TAG,
                        ">>>>> hook %s. stub: %" PRIxPTR", status_code: %d, caller_path_name: %s, sym_name: %s, new_func: %" PRIxPTR", prev_func: %" PRIxPTR", arg: %" PRIxPTR,
                        BYTEHOOK_STATUS_CODE_OK == status_code ? "OK" : "FAILED",
                        reinterpret_cast<uintptr_t>(task_stub),
                        status_code,
                        caller_path_name,
                        sym_name,
                        reinterpret_cast<uintptr_t>(new_func),
                        reinterpret_cast<uintptr_t>(prev_func),
                        reinterpret_cast<uintptr_t>(arg));
}

static int hacker_pollonce(int fd, long ptr, int timeoutMillis ) {
    BYTEHOOK_STACK_SCOPE();

    int result = BYTEHOOK_CALL_PREV(hacker_pollonce, fd, ptr, timeoutMillis);
    __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, "native pollonce: %d", timeoutMillis);

    return result;
}

static int hacker_epoll_wait(int fd, epoll_event *events, int max_events, int timeout) {
//    size_t i =0;
    // (1) Always call the BYTEHOOK_STACK_SCOPE() macro at the beginning of the proxy function.
    BYTEHOOK_STACK_SCOPE();

    // (2) Use BYTEHOOK_CALL_PREV() macro to call the original function.

    int result = BYTEHOOK_CALL_PREV(hacker_epoll_wait, fd, events, max_events, timeout);

//    int fdRet = 0;
//    int event = 0;
//    for(int i=0;i<result;i++) {
//        fdRet = events[i].data.fd;
//        event = events[i].events;
//        __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, ">>>>> bytehook post hacker_epoll_wait(), fdRet: %d, event: %d", fdRet, event);
//    }
//    int *a = NULL;
//    for(int i = 0;i<10;i++) {
//        __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, "a[i]: %d", a[i]);
//    }
    __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, "22222222 bytehook post hacker_epoll_wait(), result: %d  timeout: %d", result,timeout);

    return result;
}


// strlen() proxy
static size_t hacker_strlen(const char* const s)
{
    // (1) Always call the BYTEHOOK_STACK_SCOPE() macro at the beginning of the proxy function.
    BYTEHOOK_STACK_SCOPE();

    __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, ">>>>> bytehook pre strlen(), msg: %s", s);

    // (2) Use BYTEHOOK_CALL_PREV() macro to call the original function.
    size_t result = BYTEHOOK_CALL_PREV(hacker_strlen, s);

    __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, ">>>>> bytehook post strlen(), msg len: %zu", result);

    return result;
}

static long hacker_recv(int socket, void* buf, size_t len, int flags, void *sockaddr, void *socklen_t) {
//    size_t i =0;
    // (1) Always call the BYTEHOOK_STACK_SCOPE() macro at the beginning of the proxy function.
    BYTEHOOK_STACK_SCOPE();

    // (2) Use BYTEHOOK_CALL_PREV() macro to call the original function.
    long result = BYTEHOOK_CALL_PREV(hacker_recv, socket, buf, len, flags, sockaddr, socklen_t);
    __android_log_print(ANDROID_LOG_ERROR, HACKER_TAG, "333333333 bytehook post hacker_recv, socket: %d, result: %ld ", socket, result,len);

    return result;
}

static void hook_populateSkPaint(void* paint, const void* font, FontFakery fakery) {
    BYTEHOOK_STACK_SCOPE();
    __android_log_print(ANDROID_LOG_ERROR, HACKER_TAG, "pre hook_populateSkPaint: font: %ld, %d, %d", font, fakery.isFakeBold(), fakery.isFakeItalic());
    if(font != nullptr) {
        BYTEHOOK_CALL_PREV(hook_populateSkPaint, paint, font, fakery);
    }
    __android_log_print(ANDROID_LOG_ERROR, HACKER_TAG, "post hook_populateSkPaint");
}

//// pollInner proxy
static size_t hacker_pollInner(int timeOut)
{
    // (1) Always call the BYTEHOOK_STACK_SCOPE() macro at the beginning of the proxy function.
    BYTEHOOK_STACK_SCOPE();

    __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, ">>>>> bytehook pre pollInner(), timeOut: %d", timeOut);

    // (2) Use BYTEHOOK_CALL_PREV() macro to call the original function.
    size_t result = BYTEHOOK_CALL_PREV(hacker_pollInner, timeOut);

    __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, ">>>>> bytehook post strlen(), result: %zu", result);

    return result;
}

static char* hacker_isHookPMS(JNIEnv *env)
{
    // (1) Always call the BYTEHOOK_STACK_SCOPE() macro at the beginning of the proxy function.
    BYTEHOOK_STACK_SCOPE();

    __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, ">>>>> bytehook pre hacker_isHookPMS");

    __android_log_print(ANDROID_LOG_DEBUG, HACKER_TAG, ">>>>> bytehook post hacker_isHookPMS");

    return (char *)'0';
}


static void addHook() {
    // hook for partial caller

//    bytehook_hook_all(
//            nullptr,
//            "_ZN7android6Looper9pollInnerE",
//            reinterpret_cast<void *>(hacker_pollInner),
//            hacker_hooked_callback,
//            nullptr);

//    bytehook_hook_all(
//            nullptr,
//            "epoll_wait",
//            reinterpret_cast<void *>(hacker_epoll_wait),
//            hacker_hooked_callback,
//            nullptr);
//
//    hacker_strlen_stub = bytehook_hook_all(
//            nullptr,
//            "recvfrom",
//            reinterpret_cast<void *>(hacker_recv),
//            hacker_hooked_callback,
//            nullptr);


    hacker_strlen_stub = bytehook_hook_single(
            "librelease_sig.so",
            nullptr,
            "_ZN11ValidateKey9isHookPMSE",
            reinterpret_cast<void *>(hacker_isHookPMS),
            hacker_hooked_callback,
            nullptr);

    __android_log_print(ANDROID_LOG_ERROR, HACKER_TAG, "addHook: %ld",hacker_strlen_stub);


//    hacker_strlen_stub = bytehook_hook_all(
//        nullptr,
//        "strlen",
//        reinterpret_cast<void *>(hacker_strlen),
//        hacker_hooked_callback,
//        nullptr);

    //hook populateSkPaint
//    hacker_strlen_stub = bytehook_hook_single(
//            "libandroid_runtime.so",
//            nullptr,
//            "_ZN7android15MinikinFontSkia15populateSkPaintEP7SkPaintPKNS_11MinikinFontENS_10FontFakeryE",
//            reinterpret_cast<void *>(hook_populateSkPaint),
//            hacker_hooked_callback,
//            nullptr);
}

// hook
static void hacker_hook(JNIEnv *env, jclass thiz)
{
    (void)env, (void)thiz;

    // already hooked?
    if(nullptr != hacker_strlen_stub) return;

    addHook();

    if(nullptr == hacker_strlen_stub)
        __android_log_print(ANDROID_LOG_ERROR, HACKER_TAG, ">>>>> hook test_add() FAILED");
    else
        __android_log_print(ANDROID_LOG_ERROR, HACKER_TAG, ">>>>> hook test_add() SUCCESS");
}


// unhook
static void hacker_unhook(JNIEnv *env, jclass thiz)
{
    (void)env, (void)thiz;

    // already unhooked?
    if(nullptr == hacker_strlen_stub) return;

    // unhook
    int status_code = bytehook_unhook(hacker_strlen_stub);
    hacker_strlen_stub = nullptr;

    __android_log_print(ANDROID_LOG_ERROR, HACKER_TAG, ">>>>> unhook strlen() %s. status_code: %d",
                        BYTEHOOK_STATUS_CODE_OK == status_code ? "OK" : "FAILED", status_code);
}


static JNINativeMethod hacker_jni_methods[] = {
        {
                "nativeHook",
                "()V",
                reinterpret_cast<void *>(hacker_hook)
        },
        {
                "nativeUnhook",
                "()V",
                reinterpret_cast<void *>(hacker_unhook)
        }
};


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm,
                                  void *reserved)
{
    (void)reserved;

    if(nullptr == vm) return JNI_ERR;

    JNIEnv *env;
    if(JNI_OK != vm->GetEnv(reinterpret_cast<void **>(&env), HACKER_JNI_VERSION)) return JNI_ERR;
    if(nullptr == env) return JNI_ERR;

    jclass cls = env->FindClass(HACKER_JNI_CLASS_NAME);
    if(nullptr == cls) return JNI_ERR;

    if(0 != env->RegisterNatives(cls, hacker_jni_methods, sizeof(hacker_jni_methods) / sizeof(hacker_jni_methods[0]))) return JNI_ERR;

    return HACKER_JNI_VERSION;
}
