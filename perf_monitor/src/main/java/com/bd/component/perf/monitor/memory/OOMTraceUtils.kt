package com.vega.core.utils

import android.os.Debug
import android.os.Process
import java.io.Closeable
import java.io.File
import java.io.FileInputStream

class OOMTraceUtils {

    private val INVALID_VALUE = -1L
    private val READ_BUFFER_SIZE = 1024
    private val DEVICEINFO_UNKNOWN = -1L

    private var memoryMax: Long = 0L
    private var memoryTotal: Long = 0L
    private var memoryFree: Long = 0
    private var memoryMalloc: Long = 0L
    private var fdCount: Long = 0L
    private var fdMax: Long = 0L
    private var threadCount: Long = 0L
    private var threadMax: Long = 0L
    private var vmSize: Long = 0L
    private var vmPeak: Long = 0L
    private var vmData: Long = 0L
    private var vmStack: Long = 0L

    fun collect(map: MutableMap<String, String>) {
        handleCollect()
        map["fdCount"] = fdCount.toString()
        map["fdMax"] = fdMax.toString()
        map["threadCount"] = threadCount.toString()
        map["memoryMax"] = memoryMax.toString()
        map["memoryTotal"] = memoryTotal.toString()
        map["memoryFree"] = memoryFree.toString()
        map["memoryMalloc"] = memoryMalloc.toString()
        map["vmData"] = "${vmData}kb"
        map["vmStk"] = "${vmStack}kb"
        map["vmPeak"] = "${vmPeak}kb"
        map["vmSize"] = "${vmSize}kb"
        var sb = StringBuilder()
        sb.append("fdCount: $fdCount")
        sb.append("\t fdMax: $fdMax")
        sb.append("\t threadCount: $threadCount")
        sb.append("\t memoryMax: $memoryMax")
        sb.append("\t memoryTotal: $memoryTotal")
        sb.append("\t memoryFree: $memoryFree")
        sb.append("\t memoryMalloc: $memoryMalloc")
        android.util.Log.e("sliver", sb.toString())
    }

    fun getMemoryTrace(): MutableMap<String, String> {
        var map = mutableMapOf<String, String>()
        try {
            val startTime = System.currentTimeMillis()
            fdCount = getFdCount()
            if (fdMax < 1) {
                fdMax = getMaxFdCount()
            }
            val fdTime = System.currentTimeMillis() - startTime
            threadCount = getThreadCount()
            val threadTime = System.currentTimeMillis() - startTime - fdTime
            memoryMax = Runtime.getRuntime().maxMemory()
            memoryTotal = Runtime.getRuntime().totalMemory()
            memoryFree = Runtime.getRuntime().freeMemory()
            memoryMalloc = (memoryTotal - memoryFree)
            vmSize = getVmSize()
            vmPeak = getVmPeak()
            vmData = getVmData()
            vmStack = getVmStk()
            val memoryTime = System.currentTimeMillis() - startTime - threadTime - fdTime
            map["fdCount"] = fdCount.toString()
            map["fdMax"] = fdMax.toString()
            map["threadCount"] = threadCount.toString()
            map["memoryMax"] = memoryMax.toString()
            map["memoryTotal"] = memoryTotal.toString()
            map["memoryFree"] = memoryFree.toString()
            map["memoryMalloc"] = memoryMalloc.toString()
            map["fdTime"] = fdTime.toString()
            map["threadTime"] = threadTime.toString()
            map["memoryTime"] = memoryTime.toString()
            map["vmData"] = "${vmData / 1024}"
            map["vmStk"] = "${vmStack / 1024}"
            map["vmPeak"] = "${vmPeak / 1024}"
            map["vmSize"] = "${vmSize / 1024}"
            return map
        } catch (e: Exception) {
        }
        return map
    }

    fun getDebugMemoryInfo(): MutableMap<String, String>? {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val time = System.currentTimeMillis()
            var memInfo = Debug.MemoryInfo()
            Debug.getMemoryInfo(memInfo)
            val debugCostTime = System.currentTimeMillis() - time
            val map = memInfo.memoryStats
            map["getMemoryInfoCost"] = debugCostTime.toString()
            return map
        }
        return mutableMapOf()
    }

    private fun handleCollect() {
        try {
            fdCount = getFdCount()
            fdMax = getMaxFdCount()
            threadCount = getThreadCount()
            vmSize = getVmSize()
            vmPeak = getVmPeak()
            vmData = getVmData()
            vmStack = getVmStk()
            memoryMax = Runtime.getRuntime().maxMemory()
            memoryTotal = Runtime.getRuntime().totalMemory()
            memoryFree = Runtime.getRuntime().freeMemory()
            memoryMalloc = (memoryTotal - memoryFree)
        } catch (e: Exception) {
        }
    }

    private fun getFdCount(): Long {
        val fd = File("/proc/" + Process.myPid() + "/fd")
        if (fd.isDirectory) {
            val files = fd.listFiles()
            return files?.size?.toLong() ?: INVALID_VALUE
        }
        return INVALID_VALUE
    }

    private fun getThreadCount(): Long {
        var stream: FileInputStream? = null
        var threads = INVALID_VALUE
        try {
            stream = FileInputStream("/proc/" + Process.myPid() + "/status")
            threads = parseFileForValue("Threads", stream)
        } catch (e: Throwable) {
        } finally {
            closeSafety(stream)
        }
        return threads
    }

    private fun getMaxFdCount(): Long {
        var stream: FileInputStream? = null
        var maxOpenFileCount = INVALID_VALUE
        try {
            stream = FileInputStream("/proc/" + Process.myPid() + "/limits")
            maxOpenFileCount = parseFileForValue("Max open files", stream)
        } catch (e: Throwable) {
        } finally {
            closeSafety(stream)
        }
        return maxOpenFileCount
    }

    /**
     * Helper method for reading values from system files, using a minimised buffer.
     *
     * @param textToMatch - Text in the system files to read for.
     * @param stream - FileInputStream of the system file being read from.
     * @return A numerical value following textToMatch in specified the system file.
     * -1 in the event of a failure.
     */
    private fun parseFileForValue(textToMatch: String, stream: FileInputStream): Long {
        val buffer = ByteArray(READ_BUFFER_SIZE)
        try {
            val length = stream.read(buffer)
            var i = 0
            while (i < length) {
                if (buffer[i] == '\n'.toByte() || i == 0) {
                    if (buffer[i] == '\n'.toByte()) {
                        i++
                    }
                    for (j in i until length) {
                        val textIndex = j - i
                        // Text doesn't match query at some point.
                        if (buffer[j] != textToMatch[textIndex].toByte()) {
                            break
                        }
                        // Text matches query here.
                        if (textIndex == textToMatch.length - 1) {
                            return extractValue(buffer, j)
                        }
                    }
                }
                i++
            }
        } catch (e: Throwable) {
            // Ignore any exceptions and fall through to return unknown value.
        }

        return DEVICEINFO_UNKNOWN
    }

    /**
     * Helper method used by [parseFileForValue][.parseFileForValue]. Parses
     * the next available number after the match in the file being read and returns it as an integer.
     *
     * @param index - The index in the buffer array to begin looking.
     * @return The next number on that line in the buffer, returned as an int. Returns
     * DEVICEINFO_UNKNOWN = -1 in the event that no more numbers exist on the same line.
     */
    private fun extractValue(buffer: ByteArray, index: Int): Long {
        var index = index
        while (index < buffer.size && buffer[index] != '\n'.toByte()) {
            if (Character.isDigit(buffer[index].toInt())) {
                val start = index
                index++
                while (index < buffer.size && Character.isDigit(buffer[index].toInt())) {
                    index++
                }
                val str = String(buffer, start, index - start)
                return java.lang.Long.parseLong(str)
            }
            index++
        }
        return DEVICEINFO_UNKNOWN
    }

    private fun closeSafety(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (throwable: Throwable) {
            // ignore
        }
    }

    private fun getVmPeak(): Long {
        var stream: FileInputStream? = null
        var vmPeak = INVALID_VALUE
        try {
            stream = FileInputStream("/proc/" + Process.myPid() + "/status")
            vmPeak = parseFileForValue("VmPeak:", stream)
        } catch (e: Throwable) {
        } finally {
            closeSafety(stream)
        }
        return vmPeak
    }

    private fun getVmData(): Long {
        var stream: FileInputStream? = null
        var vmPeak = INVALID_VALUE
        try {
            stream = FileInputStream("/proc/" + Process.myPid() + "/status")
            vmPeak = parseFileForValue("VmData:", stream)
        } catch (e: Throwable) {
        } finally {
            closeSafety(stream)
        }
        return vmPeak
    }

    private fun getVmStk(): Long {
        var stream: FileInputStream? = null
        var vmPeak = INVALID_VALUE
        try {
            stream = FileInputStream("/proc/" + Process.myPid() + "/status")
            vmPeak = parseFileForValue("VmStk:", stream)
        } catch (e: Throwable) {
        } finally {
            closeSafety(stream)
        }
        return vmPeak
    }

    private fun getVmSize(): Long {
        var stream: FileInputStream? = null
        var vmSize = 0L
        try {
            stream = FileInputStream("/proc/" + Process.myPid() + "/status")
            vmSize = parseFileForValue("VmSize:", stream)
        } catch (e: Throwable) {
        } finally {
            closeSafety(stream)
        }
        return vmSize
    }

    fun getExtraMemoryTrace(): MutableMap<String, String> {
        var map = mutableMapOf<String, String>()
        try {
            memoryMax = Runtime.getRuntime().maxMemory()
            memoryTotal = Runtime.getRuntime().totalMemory()
            memoryFree = Runtime.getRuntime().freeMemory()
            memoryMalloc = (memoryTotal - memoryFree)
            vmSize = getVmSize()
            vmPeak = getVmPeak()
            vmData = getVmData()
            map["memoryMax"] = memoryMax.toString()
            map["memoryTotal"] = memoryTotal.toString()
            map["memoryFree"] = memoryFree.toString()
            map["memoryMalloc"] = memoryMalloc.toString()
            map["vmData"] = "${vmData}kb"
            map["vmPeak"] = "${vmPeak}kb"
            map["vmSize"] = "${vmSize}kb"
            android.util.Log.e("sliver", "vmData: ${vmData}kb , vmPeak: ${vmPeak}kb, vmSize: ${vmSize}kb")
            return map
        } catch (e: Exception) {
        }
        return map
    }
}
