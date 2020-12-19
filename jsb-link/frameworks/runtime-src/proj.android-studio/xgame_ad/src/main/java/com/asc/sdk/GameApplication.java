package com.asc.sdk;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.asc.sdk.platform.AppConfigs;
import com.asc.sdk.platform.AppUtils;
import com.asc.sdk.platform.LogUtil;
import com.heytap.msp.mobad.api.InitParams;
import com.heytap.msp.mobad.api.MobAdManager;
import com.nearme.game.sdk.GameCenterSDK;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

//新建Application，继承QuickSdkApplication
public class GameApplication extends Application {

    public static Application application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        GameCenterSDK.init(AppConfigs.GAME_SDK_APP_ID, this);

//        /**
//         * 应用必须加入这行代码，初始化广告SDK
//         */
        initSdk();
        /**
         * 解决Android9.0以上出现Detected problems with API compatibility的弹框
         */
        closeAndroidPDialog();

        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(this, AppConfigs.UMENG_SDK_APP_KEY, AppConfigs.UMENG_SDK_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);

        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(false);

        // 选用合适的页面采集模式，这里以LEGACY_MANUAL为例
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true);
    }


    private void initSdk() {
        InitParams initParams = new InitParams.Builder()
                .setDebug(false)//true打开SDK日志，当应用发布Release版本时，必须注释掉这行代码的调用，或者设为false
                .build();
        /**
         * 调用这行代码初始化广告SDK
         */
        MobAdManager.getInstance().init(this, AppConfigs.APP_ID, initParams);
    }

    private void closeAndroidPDialog() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
