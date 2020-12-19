package com.asc.sdk;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.asc.sdk.bean.AdControllerBean;
import com.asc.sdk.platform.AppConfigs;
import com.asc.sdk.platform.AppUtils;
import com.asc.sdk.platform.LogUtil;
import com.asc.sdk.platform.StoreUtils;
import com.google.gson.Gson;
import com.heytap.msp.mobad.api.ad.BannerAd;
import com.heytap.msp.mobad.api.ad.InterstitialAd;
import com.heytap.msp.mobad.api.ad.RewardVideoAd;
import com.heytap.msp.mobad.api.listener.IBannerAdListener;
import com.heytap.msp.mobad.api.listener.IInterstitialAdListener;
import com.heytap.msp.mobad.api.listener.IRewardVideoAdListener;
import com.heytap.msp.mobad.api.params.RewardVideoAdParams;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.common.util.AppUtil;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MAdsManager {

    private Cocos2dxActivity mainAct;
    private boolean defaultOpen = true;
    private boolean bannerOpen = defaultOpen;
    private boolean intersOpen = defaultOpen;
    private boolean nativeIntersOpen = defaultOpen;
    private boolean intersVideo = defaultOpen;
    private boolean rewardVideoOpen = defaultOpen;

    private static MAdsManager instance;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private BannerAd banner;
    private FrameLayout bannerView = null;
    private int limit_cnt = 0;
    private boolean isCloseBanner = false;
    private int isCloseBannerCnt = 0;

    private InterstitialAd interstitial = null;
    private RewardVideoAd rewardRewardVideoAD = null;
    private boolean isRaward = false;
    private boolean isRawardBack = false;
    public AdControllerBean.GameBean gameBean = new AdControllerBean.GameBean();
    public AdControllerBean.SettingBean settingBean = new AdControllerBean.SettingBean();
    private int showInters_frequency_local = 1;  // 插屏展示次数
    private int showNativeInters_frequency_local = 1;  // 原生插屏展示次数
    private int showInterVideo_frequency_local = 1; //插屏视频展示次数
    private int nativeIntersFlag = 1; // 当展示原生插屏视频次数设置为0时，插屏调用showNativeInter_frequecy 次后展示一次
    private int intersFlag = 1; // 当展示插屏视频次数设置为0时，插屏调用showInter_frequecy 次后展示一次
    private boolean isNativeIntersReady = false; // 是否可以展示原生插屏
    private Long interstitialCloseTime;  // 插屏或插屏视频展示时间
    public boolean isFirstPalyVideoCallBack = false; // 每次 展示激励视频 只需要调用一次回调
    private boolean intersAdsIsReady = false; // 插屏广告是否预加载成功
    private boolean rewardVideoAdsIsReady = false; // 激励视频是否预加载成功
    private boolean nativeAdIntervalTimeIsReady = false;  // 原生插屏是否达到展示条件
    private boolean intersAdIntervalTimeIsReady = false;  // 插屏是否达到展示条件
    private boolean rewardVideoAdIntervalTimeIsReady = false;   // 插屏视频是否达到展示条件
    private int rewardVideoLoadErrorNum = 1;  // 激励视频加载错误次数
    private int settingAdErrorNum = 3;  // 设置 广告加载错误最高次数
    private boolean isFrequency = false;  // 当进入频繁加载时，getVideoFlag 返回false
    private boolean getVideoFlagGoOneLoadVideo = true;  // 多次触发getVideoFlag()  方法时 执行一次loadVideo()
    private boolean rewardVideoIsShowing = false; // 激励视频是否正在播放
    public boolean intersShowingIsShowbanner = true;
    public int intersGetNum = 1;

    private MAdsManager() {

    }

    public static MAdsManager getInstance() {
        if (instance == null) {
            instance = new MAdsManager();
        }
        return instance;
    }

    public void initMadsSdk(Cocos2dxActivity activity) {

        LogUtil.log_E("=============== init ads");

        this.mainAct = activity;

        try {

            Gson gson = new Gson();
            AdControllerBean adControllerBean = gson.fromJson(StoreUtils.getString(mainAct, AppConfigs.AD_CONTROLLER_DATA), AdControllerBean.class);
            if (adControllerBean != null) {

                if (adControllerBean.getSetting() != null) {
                    settingBean.setShowBanner(adControllerBean.getSetting().getShowBanner());
                    settingBean.setShowInters(adControllerBean.getSetting().getShowInters());
                    settingBean.setShowInterVideo(adControllerBean.getSetting().getShowInterVideo());
                    settingBean.setShowNativeInters(adControllerBean.getSetting().getShowNativeInters());
                }

                List<AdControllerBean.GameBean> gameBeans = adControllerBean.getGame();
                for (AdControllerBean.GameBean gameBean : gameBeans) {
                    if (gameBean.getBundleName().equals(AppUtils.getPackageName(mainAct))) {

                        this.gameBean.setShowBanner(gameBean.getShowBanner());
                        this.gameBean.setShowInters(gameBean.getShowInters());
                        this.gameBean.setShowInterVideo(gameBean.getShowInterVideo());
                        this.gameBean.setShowNativeInters(gameBean.getShowNativeInters());

                        this.gameBean.setGid(gameBean.getGid());
                        this.gameBean.setBundleName(gameBean.getBundleName());
                        this.gameBean.setName(gameBean.getName());

                        this.gameBean.setShowBanner_time(gameBean.getShowBanner_time().equals("0") ? "20" : gameBean.getShowBanner_time());
                        this.gameBean.setShowInters_time(gameBean.getShowInters_time());
                        this.gameBean.setShowInterVideo_time(gameBean.getShowInterVideo_time());
                        this.gameBean.setShowNativeInters_time(gameBean.getShowNativeInters_time());

                        this.gameBean.setShowInters_frequency(gameBean.getShowInters_frequency());
                        this.gameBean.setShowInterVideo_frequency(gameBean.getShowInterVideo_frequency());
                        this.gameBean.setShowNativeInters_frequency(gameBean.getShowNativeInters_frequency());

                        this.gameBean.setShowStart_time(gameBean.getShowStart_time());

                        this.gameBean.setNativeIntersDelayTime(gameBean.getNativeIntersDelayTime());
                        this.gameBean.setErrorClickProbability(gameBean.getErrorClickProbability());
                        this.gameBean.setIntersFlagNum(gameBean.getIntersFlagNum());

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StoreUtils.getInt(mainAct, AppConfigs.SP_IS_FIRST_GET_INTERS_FLAG, 0) == 0) {
            StoreUtils.putLong(mainAct, AppConfigs.SP_INTERS_OR_INTERVIDEO_LOCAL_TIME, System.currentTimeMillis());
            StoreUtils.putInt(mainAct, AppConfigs.SP_IS_FIRST_GET_INTERS_FLAG, 1);
        }

        // do something
        initDelay();
    }


    private void initDelay() {

        LogUtil.log_E("=============== init ads delay");

        if (bannerOpen && settingBean.isShowBanner() && gameBean.isShowBanner()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initBanner();
                    TimerTaskSave();
                }
            }, 100);
        }

        if (intersOpen && settingBean.isShowInters() && gameBean.isShowInters()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initInters();
                }
            }, 3 * 100);

        }

        if (rewardVideoOpen) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initVideo();
                }
            }, 6 * 100);
        }

        if (nativeIntersOpen && settingBean.isShowNativeInters() && gameBean.isShowNativeInters()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NativeAdsManager.getInstance().initNativeAdsSdk(mainAct);
                }
            }, 9 * 100);

        }

        //initNativeAd();
    }

    //region Banner初始化、加载、监听、展示、隐藏、定时更换
    private void initBanner() {

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = LayoutInflater.from(mainAct);
        View view = inflater.inflate(R.layout.activity_banner, null);
        mainAct.addContentView(view, params);
        bannerView = (FrameLayout) view.findViewById(R.id.app_layout_banner);

        if (bannerView == null) {
            return;
        }

        banner = new BannerAd(mainAct, AppConfigs.BANNER_POS_ID);
        banner.setAdListener(new IBannerAdListener() {
            @Override
            public void onAdReady() {
                LogUtil.log_E("IBannerAdListener=============onAdReady：");
                limit_cnt = 1;
            }

            @Override
            public void onAdClose() {
                LogUtil.log_E("IBannerAdListener=============onAdClose：");
                isCloseBanner = true;
            }

            @Override
            public void onAdShow() {
                LogUtil.log_E("IBannerAdListener=============onAdShow：");
            }

            @Override
            public void onAdFailed(String s) {
                LogUtil.log_E("IBannerAdListener=============onAdFailed errorMsg：" + s);
            }

            @Override
            public void onAdFailed(int i, String s) {
                LogUtil.log_E("IBannerAdListener=============onAdFailed errorMsg：" + s + " errorCode：" + i);
                limit_cnt++;
            }

            @Override
            public void onAdClick() {
                LogUtil.log_E("IBannerAdListener=============onAdClick：");
            }
        });
        /**
         * 获取Banner广告View，将View添加到你的页面上去
         *
         */
        View adView = banner.getAdView();
        /**
         * mBannerAd.getAdView()返回可能为空，判断后在添加
         */
        if (null != adView) {
            /**
             * 这里addView是可以自己指定Banner广告的放置位置【一般是页面顶部或者底部】
             */
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            bannerView.addView(adView, layoutParams);
        }

        loadBanner();
    }

    private void loadBanner() {
        mainAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (banner != null) {
                    banner.loadAd();
                }
            }
        });
    }

    public void showBanner() {
        if (bannerView != null && intersShowingIsShowbanner)
            bannerView.setVisibility(View.VISIBLE);
    }

    public void hideBanner() {
        if (bannerView != null)
            bannerView.setVisibility(View.GONE);
    }

    public void TimerTaskSave() {

        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LogUtil.log_E("TimerTaskSave ================= limit_cnt : " + limit_cnt);

                if (!intersShowingIsShowbanner) {
                    return;
                }

                if (limit_cnt > 5) {
                    return;
                }

                if (isCloseBanner) {
                    isCloseBannerCnt++;
                    if (isCloseBannerCnt > 2) {
                        isCloseBannerCnt = 0;
                        isCloseBanner = false;
                    }
                    return;
                }

                loadBanner();
            }
        };

        long delay = 20000;
        long intevalPeriod = Integer.parseInt(gameBean.getShowBanner_time().equals("0") ? "20" : gameBean.getShowBanner_time()) * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }
    //endregion

    // region 插屏 初始、加载、监听、展示、判断是否有插屏

    private void initInters() {

        if (interstitial != null) {
            interstitial.destroyAd();
        }


        /**
         * 构造 InterstitialAd.
         */
        interstitial = new InterstitialAd(mainAct, AppConfigs.INTERSTITIAL_POS_ID);
        /**
         * 设置插屏广告行为监听器.
         */
        interstitial.setAdListener(new IInterstitialAdListener() {
            @Override
            public void onAdReady() {
                intersAdsIsReady = true;
                LogUtil.log_E("Inters ===================== onReady ");
            }

            @Override
            public void onAdClose() {
                intersAdsIsReady = false;
                intersShowingIsShowbanner = true;
                MAdsManager.getInstance().showBanner();
                LogUtil.log_E("Inters ===================== onAdClose ");
                loadInters();

                NativeAdsManager.getInstance().nativeAdIsShowing = false;
                NativeAdsManager.getInstance().showNativeAdPaster();
            }

            @Override
            public void onAdShow() {
                intersShowingIsShowbanner = false;
                MAdsManager.getInstance().hideBanner();

                NativeAdsManager.getInstance().nativeAdIsShowing = true;
                NativeAdsManager.getInstance().hideNativeAdPaster();

                LogUtil.log_E("Inters ===================== onAdShow ");
            }

            @Override
            public void onAdFailed(String s) {
                intersAdsIsReady = false;
                LogUtil.log_E("Inters ===================== onAdFailed errorMsg：" + s);
            }

            @Override
            public void onAdFailed(int i, String s) {
                intersAdsIsReady = false;
                if (i == 10003) { // 广告过期
                    initInters();
                }

                LogUtil.log_E("Inters ===================== onAdFailed errorMsg：" + s + " errorCode：" + i);
            }

            @Override
            public void onAdClick() {
                LogUtil.log_E("Inters ===================== onAdClick ");
            }
        });

        loadInters();
    }

    private void loadInters() {
        mainAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (interstitial != null) {
                    interstitial.loadAd();
                }
            }
        });
    }

    public boolean getIntersFlag() {

        intersGetNum++;
        if (intersGetNum > Integer.parseInt(gameBean.getIntersFlagNum())) {
            intersGetNum = 1;
            Intent intent = new Intent(mainAct, SplashAdActivity.class);
            mainAct.startActivity(intent);
            return false;
        }

        LogUtil.log_E("=============== getIntersFlag");

        nativeAdIntervalTimeIsReady = false;
        intersAdIntervalTimeIsReady = false;
        rewardVideoAdIntervalTimeIsReady = false;

        interstitialCloseTime = StoreUtils.getLong(mainAct, AppConfigs.SP_INTERS_OR_INTERVIDEO_LOCAL_TIME, 0L);  //单位毫秒
        Long d = (System.currentTimeMillis() - interstitialCloseTime) / 1000; // 单位：秒

        LogUtil.log_E("d：" + d);
        LogUtil.log_E("interstitialCloseTime：" + interstitialCloseTime);
        LogUtil.log_E("gameBean.getShowStart_time：" + gameBean.getShowStart_time());
        LogUtil.log_E("gameBean.getShowNativeInters_time：" + gameBean.getShowNativeInters_time());
        LogUtil.log_E("gameBean.getShowInters_time：" + gameBean.getShowInters_time());
        LogUtil.log_E("gameBean.getShowInterVideo_time：" + gameBean.getShowInterVideo_time());

        if (StoreUtils.getInt(mainAct, AppConfigs.SP_IS_FIRST_GET_INTERS_FLAG, 1) > 1) {

            if (d >= Long.parseLong(gameBean.getShowNativeInters_time())) {
                nativeAdIntervalTimeIsReady = true;
            }
            if (d >= Long.parseLong(gameBean.getShowInters_time())) {
                intersAdIntervalTimeIsReady = true;
            }
            if (d >= Long.parseLong(gameBean.getShowInterVideo_time())) {
                rewardVideoAdIntervalTimeIsReady = true;
            }

        } else {
            if (d >= Long.parseLong(gameBean.getShowStart_time())) {
                nativeAdIntervalTimeIsReady = true;
                intersAdIntervalTimeIsReady = true;
                rewardVideoAdIntervalTimeIsReady = true;
                StoreUtils.putInt(mainAct, AppConfigs.SP_IS_FIRST_GET_INTERS_FLAG, 2);
            }
        }

        // 原生插屏 广告控制
        if (settingBean.isShowNativeInters() && gameBean.isShowNativeInters()) {

            boolean flag = true;

            if (!nativeAdIntervalTimeIsReady) {
                LogUtil.log_E("===============原生插屏时间间隔未到");
                flag = false;
            }

            if (!gameBean.getShowInters_frequency().equals("0")) {
                if (gameBean.getShowNativeInters_frequency().equals("0") || showNativeInters_frequency_local > Integer.parseInt(gameBean.getShowNativeInters_frequency())) {
                    flag = false;
                }
            } else {
                if (!gameBean.getShowNativeInters_frequency().equals("0") && nativeIntersFlag <= Integer.parseInt(gameBean.getShowNativeInters_frequency())) {
                    nativeIntersFlag++;
                    flag = false;
                }
            }

            if (flag) {
                if (NativeAdsManager.getInstance().getIntersNativeAd()) {
                    isNativeIntersReady = true;
                    return true;
                } else {
                    showInters_frequency_local = 1;
                }
            }
        }

        if (settingBean.isShowInters() && gameBean.isShowInters()) {

            if (!intersAdIntervalTimeIsReady) {
                LogUtil.log_E("===============插屏时间间隔未到");
                return false;
            }

            if (!gameBean.getShowNativeInters_frequency().equals("0")) {
                if (gameBean.getShowInters_frequency().equals("0") || showInters_frequency_local > Integer.parseInt(gameBean.getShowInters_frequency())) {
                    return false;
                }
            } else {
                if (!gameBean.getShowInters_frequency().equals("0") && intersFlag <= Integer.parseInt(gameBean.getShowInters_frequency())) {
                    intersFlag++;
                    return false;
                }
            }

            if (interstitial != null) {
                if (intersAdsIsReady) {
                    return true;
                } else {
                    showNativeInters_frequency_local = 1;
                }
            }
            loadInters();
        }
        return false;
    }


    public void showInters() {

        if (isNativeIntersReady) {
            nativeIntersFlag = 1;
            isNativeIntersReady = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NativeAdsManager.getInstance().showNativeAdsInters();
                }
            }, Integer.parseInt(gameBean.getNativeIntersDelayTime()));

            StoreUtils.putLong(mainAct, AppConfigs.SP_INTERS_OR_INTERVIDEO_LOCAL_TIME, System.currentTimeMillis());
            if (Integer.parseInt(gameBean.getShowNativeInters_frequency()) <= showNativeInters_frequency_local) {
                showInters_frequency_local = 1;
            }
            showNativeInters_frequency_local++;
            return;
        }

        intersFlag = 1;
        LogUtil.log_E("=============== showInters");
        mainAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (interstitial != null && intersAdsIsReady) {
                    interstitial.showAd();
                    StoreUtils.putLong(mainAct, AppConfigs.SP_INTERS_OR_INTERVIDEO_LOCAL_TIME, System.currentTimeMillis());
                    if (Integer.parseInt(gameBean.getShowInters_frequency()) <= showInters_frequency_local) {
                        showNativeInters_frequency_local = 1;
                    }
                    showInters_frequency_local++;

                    return;
                }
                loadInters();
            }
        });
    }

    //endregion

    //region 激励视频初始话、加载、监听、展示、判断是否有激励视频、回调
    private void initVideo() {
        LogUtil.log_E("=============== initVideo");

        if (rewardRewardVideoAD != null) {
            rewardRewardVideoAD.destroyAd();
        }

        /**
         * 构造激励视频广告对象
         */
        rewardRewardVideoAD = new RewardVideoAd(mainAct, AppConfigs.REWARD_VIDEO_POS_ID, new IRewardVideoAdListener() {
            @Override
            public void onAdSuccess() {
                isFrequency = false;
                rewardVideoLoadErrorNum = 1;
                rewardVideoAdsIsReady = true;
                getVideoFlagGoOneLoadVideo = true;
                LogUtil.log_E("initVideo========================onAdSuccess");
            }

            @Override
            public void onAdFailed(String s) {
                rewardVideoAdsIsReady = false;
                isRaward = false;
                PlayVideoCallBack();
                LogUtil.log_E("initVideo========================onAdFailed errorMsg：" + s);
            }

            @Override
            public void onAdFailed(int i, String s) {

                if (i == 10003) { // 广告过期
                    initVideo();
                } else {
                    isFrequency = true;
                    if (rewardVideoLoadErrorNum <= settingAdErrorNum) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadVideo();
                                rewardVideoLoadErrorNum++;
                            }
                        }, 10 * 1000);
                    } else {
                        isFrequency = false;
                        getVideoFlagGoOneLoadVideo = true;
                        rewardVideoLoadErrorNum = 1;
                    }
                }
                rewardVideoAdsIsReady = false;
                isRaward = false;
                PlayVideoCallBack();
                LogUtil.log_E("initVideo========================onAdFailed errorMsg：" + s + " errorCode：" + i);
            }

            @Override
            public void onAdClick(long l) {
                LogUtil.log_E("initVideo========================onAdClick");
            }

            @Override
            public void onVideoPlayStart() {
                LogUtil.log_E("initVideo========================onVideoPlayStart");
            }

            @Override
            public void onVideoPlayComplete() {
                rewardVideoIsShowing = false;
                LogUtil.log_E("initVideo========================onVideoPlayComplete");
            }

            @Override
            public void onVideoPlayError(String s) {
                rewardVideoIsShowing = false;
                rewardVideoAdsIsReady = false;
                isRaward = false;
                PlayVideoCallBack();
                LogUtil.log_E("initVideo========================onVideoPlayError");
            }

            @Override
            public void onVideoPlayClose(long l) {
                rewardVideoIsShowing = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PlayVideoCallBack();
                    }
                }, 500);
                LogUtil.log_E("initVideo========================onVideoPlayClose");
            }

            @Override
            public void onLandingPageOpen() {
                LogUtil.log_E("initVideo========================onLandingPageOpen");
            }

            @Override
            public void onLandingPageClose() {
                rewardVideoIsShowing = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PlayVideoCallBack();
                    }
                }, 500);
                LogUtil.log_E("initVideo========================onLandingPageClose");
            }

            @Override
            public void onReward(Object... objects) {
                LogUtil.log_E("initVideo========================onReward");
                isRaward = true;

            }
        });


        loadVideo();
    }

    private void loadVideo() {
        mainAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardRewardVideoAD != null) {
                    /**
                     * 调用loadAd方法请求激励视频广告;通过RewardVideoAdParams.setFetchTimeout方法可以设置请求
                     * 视频广告最大超时时间，单位毫秒；
                     */
                    RewardVideoAdParams rewardVideoAdParams = new RewardVideoAdParams.Builder()
                            .setFetchTimeout(3000)
                            .build();
                    rewardRewardVideoAD.loadAd(rewardVideoAdParams);
                }
            }
        });

    }

    public boolean getVideoFlag() {
        LogUtil.log_E("=============== getVideoFlag");
        if (rewardRewardVideoAD != null && rewardVideoAdsIsReady && rewardRewardVideoAD.isReady()) {
            rewardVideoIsShowing = true;
            return true;
        }

        if (getVideoFlagGoOneLoadVideo && !isFrequency && !rewardVideoIsShowing) {
            getVideoFlagGoOneLoadVideo = false;
            loadVideo();
        }
        return false;
    }

    public void showVideo() {
        LogUtil.log_E("=============== showVideo");
        mainAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardRewardVideoAD != null && rewardVideoAdsIsReady && rewardRewardVideoAD.isReady()) {
                    rewardRewardVideoAD.showAd();
                    rewardVideoAdsIsReady = false;
                    isFirstPalyVideoCallBack = true;
                    return;
                }
                loadVideo();
            }
        });
    }

    String cocosCallBackString;

    private void PlayVideoCallBack() {

        if (!isFirstPalyVideoCallBack) {
            return;
        }

        cocosCallBackString = "window.VideoCallback(\"0\");";

        if (isRaward) {
            LogUtil.log_E("video ===================== PlayVideoCallBack back two");
            cocosCallBackString = "window.VideoCallback(\"1\");";
        }

        mainAct.runOnGLThread(new Runnable() {

            @Override
            public void run() {
                //Cocos2dxJavascriptJavaBridge.evalString("cc.find(\"AdCtr\").getComponent(\"AdController\").AndroidFailCallback()");
                Cocos2dxJavascriptJavaBridge.evalString(cocosCallBackString);
            }
        });

        isFirstPalyVideoCallBack = false;
        isRawardBack = true;
        isRaward = false;
        loadVideo();
    }

    //endregion


    /**
     * 退出
     */
    public void exitGame() {

        LogUtil.log_E("=============== MAdsManager 退出");
        GameCenterSDK.getInstance().onExit(mainAct, new GameExitCallback() {
            @Override
            public void exitGame() {
                // CP 实现游戏退出操作，也可以直接调用
                // AppUtil工具类里面的实现直接强杀进程~
                AppUtil.exitGameProcess(mainAct);
            }
        });
    }


    public void runOnMainThread(Runnable runnable) {
        if (this.mainThreadHandler != null) {
            this.mainThreadHandler.post(runnable);
        } else {
            if (this.mainAct != null) {
                this.mainAct.runOnUiThread(runnable);
            }

        }
    }

    public void releaseAdsObject() {
        if (banner != null) {
            banner.destroyAd();
        }

        if (interstitial != null) {
            interstitial.destroyAd();
        }

        if (rewardRewardVideoAD != null) {
            rewardRewardVideoAD.destroyAd();
        }
    }
}

