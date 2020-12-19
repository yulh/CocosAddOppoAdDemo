package com.asc.sdk;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.asc.sdk.platform.AppConfigs;
import com.asc.sdk.platform.LogUtil;
import com.heytap.msp.mobad.api.ad.SplashAd;
import com.heytap.msp.mobad.api.listener.ISplashAdListener;
import com.heytap.msp.mobad.api.params.SplashAdParams;

public class ChannelSplashActivity extends Activity implements ISplashAdListener {
    public FrameLayout splashView;
    public Activity cons;
    //
    private SplashAd mSplashAd;
    /**
     * 判断是否可以立刻跳转应用主页面。
     */
    private boolean mCanJump = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cons = this;

        //requestPermission();

        initAds();

    }

    public void requestPermission() {
        // 当targetVer大于23时 需要动态申请读写等权限 具体权限 具体而定
        try {
            // check权限
            if ((ContextCompat.checkSelfPermission(cons, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(cons, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                // 没有 ， 申请权限 权限数组
                ActivityCompat.requestPermissions(cons, new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                initAds();
                LogUtil.log_E("===============则执行初始化");
            }
        } catch (Exception e) {
            // 异常 继续申请
            ActivityCompat.requestPermissions(cons, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    //申请权限的回调（结果）
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        initAds();
        if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LogUtil.log_E("===============SecActivity  申请权限的回调（结果）");
        } else {
            LogUtil.log_E("=============== 失败  这里逻辑以游戏为准 ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 这里包含对于点击闪屏广告以后、然后返回闪屏广告页面立刻跳转应用主页面的处理。
         */
        if (mCanJump) {
            jumpGame();
        }
        mCanJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 这里包含对于点击闪屏广告以后、然后返回闪屏广告页面立刻跳转应用主页面的处理。
         */
        mCanJump = false;
    }


    public void initAds() {
        LogUtil.log_E("initAds ===================== initAds");
        try {
            /**
             * SplashAd初始化参数、这里可以设置获取广告最大超时时间，
             * setShowPreLoadPage方法可以设置是否启用SDK默认的等待页面；false不启用；true启用；
             * setBottomArea方法用来设置自定义的广告底部LOGO区域视图
             */
            LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View bottomArea = inflate.inflate(R.layout.splash_bottom_area, null);
            //
            SplashAdParams splashAdParams = new SplashAdParams.Builder()
                    .setFetchTimeout(3000)
                    .setShowPreLoadPage(false)
                    .setBottomArea(bottomArea)
                    .build();
            /**
             * 构造SplashAd对象
             * 注意：构造函数传入的几个形参都不能为空，否则将抛出NullPointerException异常。
             */
            mSplashAd = new SplashAd(this, AppConfigs.SPLASH_POS_ID, this, splashAdParams);
        } catch (Exception e) {
            LogUtil.log_E("SplashAD ===================== false");
            jumpGame();
        }
    }

    public void jumpGame() {


        if (mCanJump) {
            Intent intent = new Intent();
            intent.setClass(ChannelSplashActivity.this, AgreementActivity.class);
            startActivity(intent);
            cons.finish();
        } else {
            mCanJump = true;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 捕获back键，在展示广告期间按back键，不跳过广告
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            //GLinkADManager.onDestory(ChannelSplashActivity.this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onAdDismissed() {
        LogUtil.log_E("=================onAdDismissed");
        jumpGame();
    }

    @Override
    public void onAdShow() {
        LogUtil.log_E("=================onAdShow");
    }

    @Override
    public void onAdFailed(String s) {
        LogUtil.log_E("=================onAdFailed errorMsg：" + s);
    }

    @Override
    public void onAdFailed(int i, String s) {
        LogUtil.log_E("=================onAdFailed errorMsg：" + s + " errorCode：" + i);
        jumpGame();
    }

    @Override
    public void onAdClick() {
        LogUtil.log_E("=================onAdClick");
    }
}
