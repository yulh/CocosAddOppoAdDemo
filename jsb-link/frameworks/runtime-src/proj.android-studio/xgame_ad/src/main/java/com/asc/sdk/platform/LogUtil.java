package com.asc.sdk.platform;

import android.util.Log;

public class LogUtil {
    public static final String TAG = "GLink =>";
    public static final boolean flag = true;

    public LogUtil() {
    }

    public static void log_D(String var0) {
        if(!flag){
            return;
        }
        Log.d("GLink =>", var0);
    }

    public static void log_E(String var0) {
        if(!flag){
            return;
        }
        Log.e("GLink =>", var0);
    }

    public static void log_I(String var0) {
        if(!flag){
            return;
        }
        Log.i("GLink =>", var0);
    }

    public static void log_W(String var0) {
        if(!flag){
            return;
        }
        Log.w("GLink =>", var0);
    }

    public static void log_V(String var0) {
        if(!flag){
            return;
        }
        Log.v("GLink =>", var0);
    }

    public static void privateLog(String var0) {
//        if (a.a) {
//            Log.e("GLink =>", var0);
//        }

    }

}
