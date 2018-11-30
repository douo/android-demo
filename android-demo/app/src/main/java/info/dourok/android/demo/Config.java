/*
 * Copyright (c) 2015. Tiou Lims, All rights reserved.
 */

package info.dourok.android.demo;

/**
 * Config 的值只能在应用创建的时候进行配置
 * WRANNING: 这只是一个约定
 *
 * @author Tiou
 */
public class Config {

    public static boolean DEBUG = true;
    public static String LOG_PREFIX = "";


    public static boolean JUDGE_ENABLE = false;


    /**
     * weixin
     */
    public static String WEIXIN_APP_ID = "";


    /**
     * umeng
     */
    public static boolean UMENG_ENABLE = true; // 友盟统计开关
    public static boolean UMENG_AUTO_UPDATE = false; // 友盟自动更新

    /**
     * ga
     */
    public static boolean GA_ENABLE = true;


    /**
     * http
     */
    public static String USER_AGENT = "info.dourok.android";
    public static int TIME_OUT = 30 * 1000;

    /**
     * sentry
     */
    public static boolean SENTRY_ENABLE = true;
    public static boolean SENTRY_DEBUG = false;
    public static String DSN = "";


    /**
     * prefs
     */
    public static final String PREFS_NAME = "douo";


}
