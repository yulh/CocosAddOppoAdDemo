package com.smarx.notchlib;

import android.app.Activity;
import android.graphics.Rect;

public interface INotchScreen {

    boolean hasNotch(Activity activity);

    void setDisplayInNotch(Activity activity);

    void getNotchRect(Activity activity, NotchSizeCallback callback);

    interface NotchSizeCallback {
        void onResult(java.util.List<Rect> notchRects);
    }

    interface HasNotchCallback {
        void onResult(boolean hasNotch);
    }

    interface NotchScreenCallback {
        void onResult(NotchScreenInfo notchScreenInfo);
    }

    class NotchScreenInfo {
        public boolean hasNotch;
        public java.util.List<Rect> notchRects;
    }
}
