package com.sanbox.baseutils;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.lm.components.core.CoreConfig;
import com.lm.components.core.CoreInitHooks;
import com.lm.components.core.CoreManager;
import com.lm.components.core.init.IInitTaskHook;
import com.lm.components.core.log.CoreALogConfig;
import com.lm.components.core.npth.CoreNpthConfig;
import com.lm.components.core.settings.CoreSettingsConfig;
import com.lm.components.core.slardar.CoreSlardarConfig;
import com.lm.components.network.NetworkManager;
import com.lm.components.network.utils.SignUtil;
import com.lm.components.settings.depends.INetwork;
import com.lm.components.settings.depends.ISettingsCallback;
import com.lm.components.utils.AssistToolQuery;

object BaseCore {
    var mRequestCount = 0

    fun init(context:Context) {
        CoreManager.init(createCoreConfig(context), createCoreInitHooks(context));
    }

    private fun createCoreConfig(context: Context): CoreConfig {
        val gpu = SignUtil.doBase64Encode("")
        return CoreConfig(
            debug = true,
            application = context as Application,
            appId = 111,
            appName = "CoreDemo",
            channelName = "googleplay",
            versionName = "1.0.0",
            updateVersionCode = "10000",
            language = "",
            location = "",
            /** gpu	Adreno (TM) 640 */
            gpuRender = gpu,
            commonParamsJson = "{}",
            /** settings 特有的配置项 */
            coreSettingsConfig = CoreSettingsConfig(
                settingsCallback = object: ISettingsCallback {
                    override fun getRequestUrl(): String {
                        return "${mRequestCount++} + https://bits.bytedance.net/bytebus/components/components/detail/6087"
                    }
                },
                settingsNetwork = SettingsNetwork(),
                immediatelyRequest = false,
                callbackOnMainThread = false
            ),
            /** alog 特有的配置项 */
            coreALogConfig = CoreALogConfig(
                debugLog = true
            ),
            /** slardar 特有的配置项 */
            coreSlardarConfig = CoreSlardarConfig(
                enableFullFps = false,
                enableDebug = true
            ),
            /** npth 特有的配置项 */
            coreNpthConfig = CoreNpthConfig(
                openNpthCrashCreator = AssistToolQuery.query("beauty_pref_open_npth_crash") == "true"
            ),
            adjustTerminate = false
        )
    }

    private fun createCoreInitHooks(context: Context): CoreInitHooks {
        val logInitHooks = object : IInitTaskHook {
            override fun before(params: MutableMap<String, String>) {
            }

            override fun after() {
                // TOdO
            }
        }

        val networkInitHooks = object : IInitTaskHook {
            override fun before(params: MutableMap<String, String>) {
                var totalMemory = 0L
                var availableMemory = 0L
                try {
//                  val actManager = FuCore.getCore().context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    val memInfo = ActivityManager.MemoryInfo()
//                  actManager.getMemoryInfo(memInfo)
                    totalMemory = memInfo.totalMem / (1024 * 1024)
                    availableMemory = memInfo.availMem / (1024 * 1024)
                } catch (th: Throwable) {
                    th.printStackTrace()
                }
                // 添加到 公共 header
                params["total-memory"] = totalMemory.toString()
                params["available-memory"] = availableMemory.toString()

                val networkConfigure = NetworkManager.instance.getNetWorkConfigure()
                networkConfigure.setDebug(false)

                val isBoe = false
                // 不管是否开启boe这个都要设置，否则boe 环境切换会出问题
                networkConfigure.setBoeEnv(context, isBoe)
                if (!isBoe) {
                    /**
                     * 初始化前，不启用 cronet，在 CameraHelper#onFrameAvailable 再开启.
                     * 另外，注意，只能在未开启 boe 的环境下才能关闭 cronet。因为 okhttp 不支持 boe 环境，cronet
                     * 通过把 https 的请求降级为 http 来支持的
                     */
                    networkConfigure.setUseCronetCore(false)
                }

                // 设置拦截器
//                networkConfigure.addInterceptor(RetrofitHeaderInterceptor())
                networkConfigure.updateIgnoreAddCommonParamsList(
                    ignoreElement = "",
                    remove = true
                )
                networkConfigure.addPassBoeHosts(listOf(""))
            }

            override fun after() {
                // TOdO
            }
        }

        val settingsInitHooks = object : IInitTaskHook {
            override fun before(params: MutableMap<String, String>) {
                NetworkManager.instance.getNetWorkConfigure()
                    .updateIgnoreAddCommonParamsList(
                        ignoreElement = "",
                        remove = false
                    )
            }

            override fun after() {
//                SettingsManager.doLoop(60)
            }
        }

        val reportInitHooks = object : IInitTaskHook {
            override fun before(params: MutableMap<String, String>) {
                params["user_id"] = ""
                params["login"] = "n"
                params["gender"] = ""
                params["is_mobile_binded"] = ""
                params["contacts_uploaded"] = ""
                params["is_old"] = ""
                params["abtest"] = ""
                params["faceu_openudid"] = ""
                params["GPU_renderer"] = ""
                params["GPU_alus"] = ""
                params["push_permission"] = ""
                params["web_ua"] = ""
            }

            override fun after() {
            }

        }

        val slardarInitHooks = object : IInitTaskHook {
            override fun before(params: MutableMap<String, String>) {
                val header = builderHeader()
                params.putAll(header)
            }

            override fun after() {
            }
        }

        val npthInitHooks = object : IInitTaskHook {
            override fun before(params: MutableMap<String, String>) {
                val header = builderHeader()
                params.putAll(header)
            }

            override fun after() {

            }
        }
        return CoreInitHooks(
            aLogInitTaskHook = logInitHooks,
            networkInitTaskHook = networkInitHooks,
            settingsInitTaskHook = settingsInitHooks,
            slardarInitTaskHook = slardarInitHooks,
            npthInitTaskHook = npthInitHooks,
            reportInitTaskHook = reportInitHooks
        )
    }

    private fun builderHeader(): MutableMap<String, String> {
        val params = mutableMapOf<String, String>()
        params["lan"] = ""
        params["pf"] = ""
        params["vr"] = ""
        params["sysvr"] = ""
        params["os-version"] = ""
        params["ch"] = ""
        params["uid"] = ""
        params["COMPRESSED"] = ""
        params["did"] = ""
        params["loc"] = ""
        params["model"] = ""
        params["manu"] = ""
        params["ssid"] = ""
        params["appvr"] = ""
        params["HDR-TDID"] = ""
        params["HDR-TIID"] = ""
        params["HDR-Device-Time"] = ""
        return params
    }

    class SettingsNetwork : INetwork {
        override fun executeGet(url: String): String = ""
    }
}
