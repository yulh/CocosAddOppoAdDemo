package com.asc.sdk.platform;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class StoreUtils {

    private static SharedPreferences sp;

    public static SharedPreferences getSharedPreferences(Context context) {
        if (sp == null) {
            String pname = context.getPackageName() + "qpy_preferences";
            sp = context.getSharedPreferences(pname, Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static int getInt(Context context, String key, int defaultVal) {

        return getSharedPreferences(context).getInt(key, 0);
    }

    public static void putInt(Context context, String key, int val) {
        Editor edit = getSharedPreferences(context).edit();
        edit.putInt(key, val);
        edit.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultVal) {

        return getSharedPreferences(context).getBoolean(key, defaultVal);

    }

    public static void putBoolean(Context context, String key, boolean val) {
        Editor edit = getSharedPreferences(context).edit();
        edit.putBoolean(key, val);
        edit.commit();
    }

    public static String getString(Context context, String key) {

        return getSharedPreferences(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultvalue) {

        return getSharedPreferences(context).getString(key, defaultvalue);
    }

    public static void putString(Context context, String key, String value) {

        if (TextUtils.isEmpty(value) || TextUtils.isEmpty(key)) {
            return;
        }
        Editor edit = getSharedPreferences(context).edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static Long getLong(Context context, String key, Long defaultVal) {

        return getSharedPreferences(context).getLong(key, defaultVal);

    }

    public static void putLong(Context context, String key, Long val) {
        Editor edit = getSharedPreferences(context).edit();
        edit.putLong(key, val);
        edit.commit();
    }

}
