package com.izdo.mynetease.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by iZdo on 2017/9/5.
 */

public class SharedPrefrencesUtil {

    public static final String XML_FILE_NAME = "cache";

    public static void saveString(Context context, String title, String content) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(title, content);
        // 立马写入
        //        editor.commit();
        // 以后再写入
        editor.apply();
    }

    public static String getString(Context context, String title) {
        String content;
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        content = sharedPreferences.getString(title, "");
        return content;
    }

    public static void saveInt(Context context, String title, int content) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(title, content);
        // 立马写入
        //        editor.commit();
        // 以后再写入
        editor.apply();
    }

    public static int getInt(Context context, String title) {
        int content;
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        content = sharedPreferences.getInt(title, 0);
        return content;
    }

    public static void saveLong(Context context, String title, long content) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(title, content);
        // 立马写入
        //        editor.commit();
        // 以后再写入
        editor.apply();
    }

    public static long getLong(Context context, String title) {
        long content;
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_FILE_NAME, Context.MODE_PRIVATE);
        content = sharedPreferences.getLong(title, 0);
        return content;
    }
}
