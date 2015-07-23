/*
 * Copyright (c) 2015. Tiou Lims, All rights reserved.
 */

package info.dourok.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

/**
 * Helper methods that make logging more consistent throughout the app.
 */
public class LogUtils {

    private static final int LOG_PREFIX_LENGTH = Config.LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        String prefix = Config.LOG_PREFIX;
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return prefix
                    + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH
                    - 1);
        }

        return prefix + str;
    }

    /**
     * WARNING: Don't use this when obfuscating class names with Proguard!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
        if (Config.DEBUG/* Log.isLoggable(tag, Log.DEBUG) */) {
            Log.d(tag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (Config.DEBUG/* Log.isLoggable(tag, Log.DEBUG) */) {
            Log.d(tag, message, cause);
        }
    }

    public static void LOGV(final String tag, String message) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (Config.DEBUG/* && Log.isLoggable(tag, Log.VERBOSE) */) {
            Log.v(tag, message);
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (Config.DEBUG /* && Log.isLoggable(tag, Log.VERBOSE) */) {
            Log.v(tag, message, cause);
        }
    }

    public static void LOGI(final String tag, String message) {
        Log.i(tag, message);
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        Log.i(tag, message, cause);
    }

    public static void LOGW(final String tag, String message) {
        Log.w(tag, message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(tag, message, cause);
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(tag, message, cause);
    }

    private LogUtils() {
    }

    public static String dumpIntent(Intent i) {
        StringBuilder sb = new StringBuilder();
        if (i == null) {
            sb.append(i);
        } else {
            sb.append("Dumping Intent start");
            sb.append("\nData:" + i.getData());
            sb.append("\nAction:" + i.getAction());
            sb.append("\n");
            Bundle bundle = i.getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    sb.append("[" + key + "=" + bundle.get(key) + "]");
                    sb.append("\n");
                }
            }
            sb.append("Dumping Intent end");
        }
        return sb.toString();

    }

    public static String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();

        StackTraceElement e = stacktrace[3];
        String methodName = e.getMethodName();
        return methodName;
    }
}
