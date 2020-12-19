//package com.asc.sdk;
//
//import android.app.Activity;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.asc.sdk.platform.Constant;
//import com.asc.sdk.platform.LogUtil;
//import com.bumptech.glide.Glide;
//import com.heytap.msp.mobad.api.ad.NativeAd;
//import com.heytap.msp.mobad.api.listener.INativeAdListener;
//import com.heytap.msp.mobad.api.params.INativeAdData;
//import com.heytap.msp.mobad.api.params.NativeAdError;
//import com.xplaygame.yudeyibaizhongsifa.nearme.gamecenter.R;
//
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class NativeAdsManager {
//
//    public Activity mActivity;
//    private static NativeAdsManager mInstance;
//    private NativeAd mNativeAds;
//    private INativeAdData mINativeAdData;
//
//    public NativeAdsManager() {
//    }
//
//    public static NativeAdsManager getInstance() {
//        if (mInstance == null) {
//            mInstance = new NativeAdsManager();
//        }
//        return mInstance;
//    }
//
//    public void initNativeAdsSdk(Activity activity) {
//        mActivity = activity;
//
//
//        initNativeAds();
//
//    }
//
//
//    private void initNativeAds() {
//
//
//        /**
//         *构造NativeAd对象。
//         */
//        mNativeAds = new NativeAd(mActivity, Constant.NATIVE_640X320_TEXT_IMG_POS_ID, new INativeAdListener() {
//            @Override
//            public void onAdSuccess(List<INativeAdData> list) {
//                LogUtil.log_E("mNativeAd==========================onAdSuccess");
//
//
//                mIconNativeAdIntersFlag = false;
//                mIconNativeAdFlag = false;
//                LogUtil.log_E("mNativeAd==========================onAdSuccess");
//                if (list != null && list.size() > 0 && list.get(0) != null) {
//                    mINativeAdData = list.get(0);
//
//                    if (null != mINativeAdData.getIconFiles() && mINativeAdData.getIconFiles().size() > 0) {
//                        LogUtil.log_E("mNativeAd==========================onAdSuccess==有icon");
//                        mIconNativeAdFlag = true;
//                        mIconNativeAdIntersFlag = true;
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                changeNativeIconView();
//                            }
//                        });
//                    } else if (null != mINativeAdData.getImgFiles() && mINativeAdData.getImgFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdData.getImgFiles().get(0).getUrl())) {
//                        mIconNativeAdIntersFlag = true;
//                    }
//                } else {
//                    LogUtil.log_E("NOADReturn");
//                }
//
//            }
//
//            @Override
//            public void onAdFailed(NativeAdError nativeAdError) {
//                mIconNativeAdFlag = false;
//                mIconNativeAdIntersFlag = false;
//                LogUtil.log_E("mNativeAd==========================onAdFailed" + nativeAdError.code + "===" + nativeAdError.msg);
//            }
//
//            @Override
//            public void onAdError(NativeAdError nativeAdError, INativeAdData iNativeAdData) {
//                mIconNativeAdFlag = false;
//                mIconNativeAdIntersFlag = false;
//                LogUtil.log_E("mNativeAd==========================onAdError");
//            }
//        });
//
//        loadNativeAd();
//        TimerTaskSave();
//    }
//
//    public void loadNativeAd() {
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                //mNativeAds = new VivoNativeAd(mActivity, nativeAdParams, mNativeAdListener);
//                mNativeAds.loadAd();
//            }
//        });
//    }
//
//    // region 原生Icon广告
//
//
//    LinearLayout native_ad_container_icon;
//    boolean mIconNativeAdFlag = false;
//    View mNativeIconAdsView;
//    RelativeLayout native_ad_icon_view;
//    int icon_size;
//    int viewX;
//    int viewY;
//
//    public void changeNativeIconView() {
//
//        if (mNativeIconAdsView != null) {
//            ViewGroup viewGroup = (ViewGroup) mNativeIconAdsView.getParent();
//            if (viewGroup != null) {
//                viewGroup.removeView(mNativeIconAdsView);
//            }
//        }
//
//        LogUtil.log_E("1111==================222");
//
//        /**
//         *在展示原生广告前调用isAdValid判断当前广告是否有效，否则即使展示了广告，也是无效曝光、点击，不计费的
//         *注意：每个INativeAdData对象只有一次有效曝光、一次有效点击；多次曝光，多次点击都只扣一次费。
//         */
//        mNativeIconAdsView = LayoutInflater.from(mActivity).inflate(R.layout.activity_native_icon, null);
//        native_ad_container_icon = mNativeIconAdsView.findViewById(R.id.native_ad_container);
//        native_ad_icon_view = mNativeIconAdsView.findViewById(R.id.native_ad_icon_view);
//        TextView title_tv = mNativeIconAdsView.findViewById(R.id.title_tv);
//        ImageView close_iv = mNativeIconAdsView.findViewById(R.id.close_iv);
//
//        LinearLayout.LayoutParams layoutParamsIconView = new LinearLayout.LayoutParams(icon_size, icon_size);
//        native_ad_icon_view.setLayoutParams(layoutParamsIconView);
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(viewX, viewY, 0, 0);
//        mActivity.addContentView(mNativeIconAdsView, layoutParams);
//
//        if (null != mINativeAdData) {
//            LogUtil.log_E("1111==================333");
//            native_ad_container_icon.setVisibility(View.VISIBLE);
//
//            /**
//             *展示icon
//             */
//
//            if (mINativeAdData != null && mINativeAdData.getIconFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdData.getIconFiles().get(0).getUrl())) {
//                showImage(mINativeAdData.getIconFiles().get(0).getUrl(), (ImageView) mNativeIconAdsView.findViewById(R.id.img_iv));
//            }
//
//            /**
//             * 判断是否需要展示“广告”Logo标签
//             */
//            //if (null != mINativeAdData.getAdMarkUrl()) {
//            //showImage(mINativeAdData.getAdMarkUrl(), (ImageView) mNativeIconAdsView.findViewById(R.id.logo_iv));
//            //}
//
//            title_tv.setText(null != mINativeAdData.getTitle() ? mINativeAdData.getTitle() : "");
//
//
//            close_iv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    native_ad_container_icon.setVisibility(View.GONE);
//
//                }
//            });
//
//            native_ad_container_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    /**
//                     *原生广告被点击时必须调用onAdClick方法通知SDK进行点击统计；
//                     * 注意：onAdClick方法必须在onAdShow方法之后再调用才有效，否则是无效点击。
//                     */
//                    mINativeAdData.onAdClick(v);
//                }
//            });
//            /**
//             * 原生广告曝光时必须调用onAdShow方法通知SDK进行曝光统计，否则就没有曝光数据。
//             */
//            mINativeAdData.onAdShow(mNativeIconAdsView.findViewById(R.id.native_ad_container));
//        }
//    }
//
//    public void showNativeAdsIcon(int icon_size, int viewX, int viewY) {
//
//        this.icon_size = icon_size;
//        this.viewX = viewX;
//        this.viewY = viewY;
//
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                changeNativeIconView();
//            }
//        });
//
//
//        if (mNativeIconAdsView != null) {
//            LogUtil.log_E("1111==================111");
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mNativeIconAdsView.setVisibility(View.VISIBLE);
//                }
//            });
//
//        }
//
//
//    }
//
//
//    public boolean getIconNativeAd() {
//
//        if (null != mINativeAdData && mIconNativeAdFlag) {
//            return true;
//        }
//
//        if (mNativeIconAdsView != null) {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mNativeIconAdsView.setVisibility(View.GONE);
//                }
//            });
//
//        }
//
//        // loadNativeAdIcon();
//
//        return false;
//    }
//
//    public void hideNativeAdsIcon() {
//
//        if (mNativeIconAdsView != null) {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mNativeIconAdsView.setVisibility(View.GONE);
//                }
//            });
//        }
//    }
//
//    public void TimerTaskSave() {
//
//        final Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                LogUtil.log_E("TimerTaskSave ================= limit_cnt : ");
//
//                loadNativeAd();
//            }
//        };
//
//
//        long delay = 20000;
//        long intevalPeriod = 30 * 1000;
//        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
//    }
//
//    //endregion
//
//
//    //region 原生插屏
//
//
//    boolean mIconNativeAdIntersFlag = false;
//    View mNativeIconAdsIntersView;
//    PopupWindow mNativeAdsIntersPop;
//    FrameLayout fl_native_inters_ad_main;
//
//
//    public void showNativeAdsInters() {
//        LogUtil.log_E("====================1");
//        /**
//         *在展示原生广告前调用isAdValid判断当前广告是否有效，否则即使展示了广告，也是无效曝光、点击，不计费的
//         *注意：每个INativeAdData对象只有一次有效曝光、一次有效点击；多次曝光，多次点击都只扣一次费。
//         */
//        mNativeIconAdsIntersView = LayoutInflater.from(mActivity).inflate(R.layout.activity_native_inters, null);
//        fl_native_inters_ad_main = mNativeIconAdsIntersView.findViewById(R.id.fl_native_inters_ad_main);
//        //native_ad_container_inters = mNativeIconAdsIntersView.findViewById(R.id.native_ad_container);
//        //mNativeIconAdsIntersView.setVisibility(View.VISIBLE);
//
//
//        LogUtil.log_E("====================2");
//
//        // if (null != mINativeAdData && mINativeAdData.isAdValid()) {
//        if (null != mINativeAdData) {
//            mNativeIconAdsIntersView.setVisibility(View.VISIBLE);
//            /**
//             *展示icon
//             */
//            if (mINativeAdData.getIconFiles() != null && mINativeAdData.getIconFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdData.getIconFiles().get(0).getUrl())) {
//                showImage(mINativeAdData.getIconFiles().get(0).getUrl(), (ImageView) mNativeIconAdsIntersView.findViewById(R.id.iv_native_inters_pic));
//            }
//
//            /**
//             *展示主图、大小为640X320。
//             */
//            if (null != mINativeAdData.getImgFiles() && mINativeAdData.getImgFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdData.getImgFiles().get(0).getUrl())) {
//
//                showImage(mINativeAdData.getImgFiles().get(0).getUrl(), (ImageView) mNativeIconAdsIntersView.findViewById(R.id.iv_native_inters_pic));
//            }
//
//            TextView title_tv = mNativeIconAdsIntersView.findViewById(R.id.tv_native_inters_title);
//            TextView desc_tv = mNativeIconAdsIntersView.findViewById(R.id.tv_native_inters_dsc);
//            title_tv.setText(null != mINativeAdData.getTitle() ? mINativeAdData.getTitle() : "");
//            desc_tv.setText(null != mINativeAdData.getDesc() ? mINativeAdData.getDesc() : "");
//            /**
//             * 处理“关闭”按钮交互行为
//             */
//            ImageView close_iv = mNativeIconAdsIntersView.findViewById(R.id.iv_native_inters_close);
//            Button click_bn = mNativeIconAdsIntersView.findViewById(R.id.btn_native_inters_click);
//
//            close_iv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //mNativeIconAdsIntersView.setVisibility(View.GONE);
//                    if (mNativeAdsIntersPop != null && mNativeAdsIntersPop.isShowing()) {
//                        mNativeAdsIntersPop.dismiss();
//                        loadNativeAd();
//                    }
//                }
//            });
//
//
//            fl_native_inters_ad_main.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mINativeAdData.onAdClick(v);
//                }
//            });
//
//            click_bn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mINativeAdData.onAdClick(v);
//                }
//            });
//
//
//            /**
//             * 原生广告曝光时必须调用onAdShow方法通知SDK进行曝光统计，否则就没有曝光数据。
//             */
//            mINativeAdData.onAdShow(mNativeIconAdsIntersView);
//
//            mNativeAdsIntersPop = new PopupWindow();
//            mNativeAdsIntersPop.setContentView(mNativeIconAdsIntersView);
//            mNativeAdsIntersPop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
//            mNativeAdsIntersPop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
//            mNativeAdsIntersPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            //设置这个，popuwindow才能显示到刘海屏区域
//            mNativeAdsIntersPop.setClippingEnabled(false);
//            //mNativeAdsIntersPop.setBackgroundDrawable(ContextCompat.getDrawable(mActivity,R.drawable.nativeintersbg));
//            mNativeAdsIntersPop.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        }
//    }
//
//
//    public boolean getIntersNativeAd() {
//
//        if (null != mINativeAdData && mIconNativeAdIntersFlag) {
//            return true;
//        }
//
//        return false;
//    }
//
//    //endregion
//
//
//    private void showImage(String url, final ImageView imageView) {
//        LogUtil.log_E("=================" + url);
//        //ImageLoader.getInstance().displayImage(url, imageView);
//        Glide.with(mActivity)
//                .load(url)
////                .listener(new RequestListener<Drawable>() {
////                    @Override
////                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                        return false;
////                    }
////
////                    @Override
////                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////                        //加载图片适应窗口大小并且居中显示
////                        //设置Img的背景
////                        imageView.setImageDrawable(resource);
////
////                        //获取画布的大小
////                        int imageWidth = resource.getIntrinsicWidth();
////                        int imageHeight = resource.getIntrinsicHeight();
////
////                        //只有当xml layout 配置为match_parent才可以调用nw和nh获取视图的高度和宽度，否则不行
////                        int nw = imageView.getWidth();
////                        int nh = imageView.getHeight();
////                        if (imageWidth > nw) {
////
////                            Matrix matrix = new Matrix();
////                            //设置放大比例
////                            float fScale = nw * 1.0f / imageWidth;
////
////                            //计算垂直的居中距离
////                            float fTranslateY = 0;
////                            imageHeight = (int) (fScale * imageHeight);
////                            if (nh > 0 && nh > imageHeight) {
////                                fTranslateY = (nh - imageHeight) / 2;
////                            }
////
////                            matrix.postScale(fScale, fScale);
////                            matrix.postTranslate(0, fTranslateY);
////                            imageView.setImageMatrix(matrix);
////                        }
////                        return true;
////                    }
////                })
//                //.centerCrop()
//                .error(R.mipmap.ic_launcher)
//                .placeholder(R.mipmap.ic_launcher)
//                .into(imageView);
//    }
//}
//


package com.asc.sdk;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asc.sdk.platform.AppConfigs;
import com.asc.sdk.platform.LogUtil;
import com.asc.sdk.platform.ScreenUtils;
import com.bumptech.glide.Glide;
import com.heytap.msp.mobad.api.ad.NativeAdvanceAd;
import com.heytap.msp.mobad.api.listener.INativeAdvanceInteractListener;
import com.heytap.msp.mobad.api.listener.INativeAdvanceLoadListener;
import com.heytap.msp.mobad.api.params.INativeAdvanceData;
import com.heytap.msp.mobad.api.params.NativeAdvanceContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NativeAdsManager {

    public Activity mActivity;
    private static NativeAdsManager mInstance;
    private NativeAdvanceAd mNativeAdvanceAd;
    private INativeAdvanceData mINativeAdIconData;
    private INativeAdvanceData mINativeAdIntersData;
    private INativeAdvanceData mINativeAdPasterData;
    public boolean nativeAdIsShowing = false;
    private boolean nativeAdClose = false;

    public NativeAdsManager() {
    }

    public static NativeAdsManager getInstance() {
        if (mInstance == null) {
            mInstance = new NativeAdsManager();
        }
        return mInstance;
    }

    public void initNativeAdsSdk(Activity activity) {
        mActivity = activity;


        initNativeAds();

        initNativeIntersView();
        initNativepasterView();
    }


    private void initNativeAds() {

        /**
         *构造NativeAd对象。
         */
        mNativeAdvanceAd = new NativeAdvanceAd(mActivity, AppConfigs.NATIVE_ADVANCE_POS_ID, new INativeAdvanceLoadListener() {
            @Override
            public void onAdSuccess(List<INativeAdvanceData> list) {
                mIconNativeAdFlag = false;
                mIconNativeAdIntersFlag = false;
                mIconNativeAdPasterFlag = false;

                if (list != null && list.size() > 0 && list.get(0) != null) {

                    list.get(0).setInteractListener(iNativeAdvanceInteractListener);

                    if (null != list.get(0).getIconFiles() && list.get(0).getIconFiles().size() > 0) {

                        mIconNativeAdFlag = true;
                        mINativeAdIconData = list.get(0);

                        mIconNativeAdIntersFlag = true;
                        mINativeAdIntersData = list.get(0);

                        mIconNativeAdPasterFlag = true;
                        mINativeAdPasterData = list.get(0);
                    }

                    if (null != list.get(0).getImgFiles() && list.get(0).getImgFiles().size() > 0 && !TextUtils.isEmpty(list.get(0).getImgFiles().get(0).getUrl())) {
                        mIconNativeAdIntersFlag = true;
                        mINativeAdIntersData = list.get(0);

                        mINativeAdPasterData = list.get(0);
                        mIconNativeAdPasterFlag = true;
                    }


                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (mIconNativeAdFlag && MAdsManager.getInstance().gameBean.isNativeIconOpen()) {
                                changeNativeIconView();
                            }

                            if (mIconNativeAdPasterFlag && MAdsManager.getInstance().gameBean.isNativePasterOpen()) {
                                changeNativepasterView();
                            }

                            if (mIconNativeAdIntersFlag) {
                                changeNativeIntersView();
                            }


                        }
                    });


                } else {
                    LogUtil.log_E("NOADReturn");
                }
            }

            @Override
            public void onAdFailed(int i, String s) {
                mIconNativeAdFlag = false;
                mIconNativeAdPasterFlag = false;
                mIconNativeAdIntersFlag = false;
                LogUtil.log_E("mNativeAd==========================onAdFailed code：" + i + "=errorMsg：" + s);
            }
        });

        loadNativeAd();
        TimerTaskSave();
    }

    INativeAdvanceInteractListener iNativeAdvanceInteractListener = new INativeAdvanceInteractListener() {
        @Override
        public void onClick() {

            if (native_inters_close_iv != null) {
                native_inters_close_iv.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onShow() {
        }

        @Override
        public void onError(int i, String s) {
            LogUtil.log_E("iNativeAdvanceInteractListener===onError，ret:" + i + ",msg:" + s);
            if (mNativeIconAdsView != null) {
                mNativeIconAdsView.setVisibility(View.GONE);
            }
        }
    };

    public void loadNativeAd() {

        if (nativeAdIsShowing) {
            return;
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mNativeAds = new VivoNativeAd(mActivity, nativeAdParams, mNativeAdListener);
                mNativeAdvanceAd.loadAd();
            }
        });
    }


    public void TimerTaskSave() {

        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LogUtil.log_E("TimerTaskSave ================= limit_cnt : ");

                if (nativeAdClose) {
                    nativeAdClose = false;
                    return;
                }
                loadNativeAd();
            }
        };


        long delay = 20000;
        long intevalPeriod = Integer.parseInt(MAdsManager.getInstance().gameBean.getShowBanner_time().equals("0") ? "20" : MAdsManager.getInstance().gameBean.getShowBanner_time()) * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    // region 原生Icon广告

    NativeAdvanceContainer native_ad_container_icon;
    boolean mIconNativeAdFlag = false;
    View mNativeIconAdsView;
    RelativeLayout native_ad_icon_view;
    int icon_size;
    int viewX;
    int viewY;

    public void changeNativeIconView() {

        if (mNativeIconAdsView != null) {
            ViewGroup viewGroup = (ViewGroup) mNativeIconAdsView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mNativeIconAdsView);
            }
        }

        /**
         *在展示原生广告前调用isAdValid判断当前广告是否有效，否则即使展示了广告，也是无效曝光、点击，不计费的
         *注意：每个INativeAdData对象只有一次有效曝光、一次有效点击；多次曝光，多次点击都只扣一次费。
         */
        mNativeIconAdsView = LayoutInflater.from(mActivity).inflate(R.layout.activity_native_icon, null);
        native_ad_container_icon = mNativeIconAdsView.findViewById(R.id.native_ad_container);
        native_ad_icon_view = mNativeIconAdsView.findViewById(R.id.native_ad_icon_view);
        TextView title_tv = mNativeIconAdsView.findViewById(R.id.title_tv);
        ImageView close_iv = mNativeIconAdsView.findViewById(R.id.close_iv);

        LinearLayout.LayoutParams layoutParamsIconView = new LinearLayout.LayoutParams(icon_size, icon_size);
        native_ad_icon_view.setLayoutParams(layoutParamsIconView);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(viewX, viewY, 0, 0);
        mActivity.addContentView(mNativeIconAdsView, layoutParams);

        if (null != mINativeAdIconData) {
            native_ad_container_icon.setVisibility(View.VISIBLE);

            /**
             *展示icon
             */

            if (mINativeAdIconData != null && mINativeAdIconData.getIconFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdIconData.getIconFiles().get(0).getUrl())) {
                showImage(mINativeAdIconData.getIconFiles().get(0).getUrl(), (ImageView) mNativeIconAdsView.findViewById(R.id.img_iv));
            }

            /**
             * 判断是否需要展示“广告”Logo标签
             */
            //if (null != mINativeAdData.getAdMarkUrl()) {
            //showImage(mINativeAdData.getAdMarkUrl(), (ImageView) mNativeIconAdsView.findViewById(R.id.logo_iv));
            //}

            title_tv.setText(null != mINativeAdIconData.getTitle() ? mINativeAdIconData.getTitle() : "");


            close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNativeIconAdsView != null) {
                        mNativeIconAdsView.setVisibility(View.GONE);
                        nativeAdClose = true;
                    }

                }
            });

            List<View> clickViewList = new ArrayList<>();
            /**
             * 响应广告点击事件的按钮
             */
            clickViewList.add(mNativeIconAdsView);
            /**
             * 绑定广告点击事件与点击按钮
             */
            mINativeAdIconData.bindToView(mActivity, native_ad_container_icon, clickViewList);
        }
    }

    public void showNativeAdsIcon(int icon_size, int viewX, int viewY) {

        this.icon_size = icon_size;
        this.viewX = viewX;
        this.viewY = viewY;

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeNativeIconView();
            }
        });


        if (mNativeIconAdsView != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNativeIconAdsView.setVisibility(View.VISIBLE);
                }
            });

        }
    }


    public boolean getIconNativeAd() {

        if (null != mINativeAdIconData && mIconNativeAdFlag) {
            return true;
        }

        if (mNativeIconAdsView != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNativeIconAdsView.setVisibility(View.GONE);
                    mIconNativeAdFlag = false;
                }
            });

        }

        return false;
    }

    public void hideNativeAdsIcon() {

        if (mNativeIconAdsView != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNativeIconAdsView.setVisibility(View.GONE);
                    mIconNativeAdFlag = false;
                }
            });
        }
    }

    //endregion

    //region 原生插屏


    NativeAdvanceContainer native_ad_container_inters;
    boolean mIconNativeAdIntersFlag = false;
    View mNativeIconAdsIntersView;
    PopupWindow mNativeAdsIntersPop;
    FrameLayout fl_native_inters_ad_main;
    TextView native_inters_title_tv;
    TextView native_inters_desc_tv;
    ImageView native_inters_close_iv;
    ImageView iv_native_inters_close_jia;
    Button native_inters_click_bn;
    List<View> nativeIntersClickViewList;
    Random random;

    private void initNativeIntersView() {
        /**
         *在展示原生广告前调用isAdValid判断当前广告是否有效，否则即使展示了广告，也是无效曝光、点击，不计费的
         *注意：每个INativeAdData对象只有一次有效曝光、一次有效点击；多次曝光，多次点击都只扣一次费。
         */
        mNativeIconAdsIntersView = LayoutInflater.from(mActivity).inflate(R.layout.activity_native_inters, null);
        fl_native_inters_ad_main = mNativeIconAdsIntersView.findViewById(R.id.fl_native_inters_ad_main);
        native_ad_container_inters = mNativeIconAdsIntersView.findViewById(R.id.native_ad_container);
        //mNativeIconAdsIntersView.setVisibility(View.VISIBLE);
        native_inters_title_tv = mNativeIconAdsIntersView.findViewById(R.id.tv_native_inters_title);
        native_inters_desc_tv = mNativeIconAdsIntersView.findViewById(R.id.tv_native_inters_dsc);
        /**
         * 处理“关闭”按钮交互行为
         */
        native_inters_close_iv = mNativeIconAdsIntersView.findViewById(R.id.iv_native_inters_close);
        native_inters_click_bn = mNativeIconAdsIntersView.findViewById(R.id.btn_native_inters_click);
        iv_native_inters_close_jia = mNativeIconAdsIntersView.findViewById(R.id.iv_native_inters_close_jia);

        nativeIntersClickViewList = new ArrayList<>();
        /**
         * 响应广告点击事件的按钮
         */
        nativeIntersClickViewList.add(fl_native_inters_ad_main);
        nativeIntersClickViewList.add(native_inters_click_bn);
        nativeIntersClickViewList.add(iv_native_inters_close_jia);

        mNativeAdsIntersPop = new PopupWindow();
        mNativeAdsIntersPop.setContentView(mNativeIconAdsIntersView);
        mNativeAdsIntersPop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        mNativeAdsIntersPop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mNativeAdsIntersPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置这个，popuwindow才能显示到刘海屏区域
        mNativeAdsIntersPop.setClippingEnabled(false);
        //mNativeAdsIntersPop.setBackgroundDrawable(ContextCompat.getDrawable(mActivity,R.drawable.nativeintersbg));

        random = new Random();
    }


    public void changeNativeIntersView() {

        if (mNativeIconAdsIntersView == null) {
            initNativeIntersView();
        }

        // if (null != mINativeAdData && mINativeAdData.isAdValid()) {
        if (null != mINativeAdIntersData) {
            mNativeIconAdsIntersView.setVisibility(View.VISIBLE);
            /**
             *展示icon
             */
            if (mINativeAdIntersData.getIconFiles() != null && mINativeAdIntersData.getIconFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdIntersData.getIconFiles().get(0).getUrl())) {
                showImage(mINativeAdIntersData.getIconFiles().get(0).getUrl(), (ImageView) mNativeIconAdsIntersView.findViewById(R.id.iv_native_inters_pic));
            }

            /**
             *展示主图、大小为640X320。
             */
            if (null != mINativeAdIntersData.getImgFiles() && mINativeAdIntersData.getImgFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdIntersData.getImgFiles().get(0).getUrl())) {

                showImage(mINativeAdIntersData.getImgFiles().get(0).getUrl(), (ImageView) mNativeIconAdsIntersView.findViewById(R.id.iv_native_inters_pic));
            }


            native_inters_title_tv.setText(null != mINativeAdIntersData.getTitle() ? mINativeAdIntersData.getTitle() : "");
            native_inters_desc_tv.setText(null != mINativeAdIntersData.getDesc() ? mINativeAdIntersData.getDesc() : "");

            native_inters_close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mNativeIconAdsIntersView.setVisibility(View.GONE);
                    if (mNativeAdsIntersPop != null && mNativeAdsIntersPop.isShowing()) {
                        mNativeAdsIntersPop.dismiss();
                        nativeAdIsShowing = false;
                        loadNativeAd();
                        MAdsManager.getInstance().intersShowingIsShowbanner = true;
                        MAdsManager.getInstance().showBanner();
                        NativeAdsManager.getInstance().showNativeAdPaster();
                    }
                }
            });

            if (random != null) {
                int n5 = random.nextInt(100);
                double n6 = n5 / Double.parseDouble("100");
                if (n6 < Double.parseDouble(MAdsManager.getInstance().gameBean.getErrorClickProbability())) {
                    native_inters_close_iv.setVisibility(View.GONE);
                } else {
                    native_inters_close_iv.setVisibility(View.VISIBLE);
                }

            }
        }
    }


    private boolean nativeIntersFlag = false;

    public void showNativeAdsInters() {

        if (mActivity != null && mNativeAdsIntersPop != null && mIconNativeAdIntersFlag) {

            nativeAdIsShowing = true;

            MAdsManager.getInstance().intersShowingIsShowbanner = false;
            MAdsManager.getInstance().hideBanner();

            NativeAdsManager.getInstance().hideNativeAdPaster();

            /**
             * 绑定广告点击事件与点击按钮
             */
            mINativeAdIntersData.bindToView(mActivity, native_ad_container_inters, nativeIntersClickViewList);
            mNativeAdsIntersPop.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

            LinearLayout.LayoutParams fl_native_inters_ad_main_param = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mActivity) / 10 * 8, ScreenUtils.getScreenHeight(mActivity) / 2);

            if (nativeIntersFlag) {
                nativeIntersFlag = false;
                fl_native_inters_ad_main_param.setMargins(0, ScreenUtils.getScreenHeight(mActivity) / 2, 0, 0);
                fl_native_inters_ad_main.setLayoutParams(fl_native_inters_ad_main_param);
            } else {
                nativeIntersFlag = true;
                fl_native_inters_ad_main_param.setMargins(0, ScreenUtils.getScreenHeight(mActivity) / 3, 0, 0);
                fl_native_inters_ad_main.setLayoutParams(fl_native_inters_ad_main_param);
            }

        }
    }


    public boolean getIntersNativeAd() {

        if (null != mINativeAdIntersData && mIconNativeAdIntersFlag) {
            return true;
        }
        nativeAdIsShowing = false;
        loadNativeAd();
        return false;
    }

    //endregion

    // region  原生贴片


    NativeAdvanceContainer native_ad_container_paster;
    boolean mIconNativeAdPasterFlag = false;
    View mNativeIconAdsPasterView;
    TextView native_paster_title_tv;
    TextView native_paster_desc_tv;
    Button native_paster_click_bn;
    List<View> nativepasterClickViewList;
    ImageView native_ad_paster_close;

    private void initNativepasterView() {
        mNativeIconAdsPasterView = LayoutInflater.from(mActivity).inflate(R.layout.native_ad_paster, null);
        native_ad_container_paster = mNativeIconAdsPasterView.findViewById(R.id.native_ad_paster_container);
        //mNativeIconAdsIntersView.setVisibility(View.VISIBLE);
        native_paster_title_tv = mNativeIconAdsPasterView.findViewById(R.id.native_ad_paster_title);
        native_paster_desc_tv = mNativeIconAdsPasterView.findViewById(R.id.native_ad_paster_desc);

        native_inters_click_bn = mNativeIconAdsPasterView.findViewById(R.id.native_ad_paster_bn);
        native_ad_paster_close = mNativeIconAdsPasterView.findViewById(R.id.native_ad_paster_close);

        nativepasterClickViewList = new ArrayList<>();
        /**
         * 响应广告点击事件的按钮
         */
        nativepasterClickViewList.add(native_inters_click_bn);
        nativepasterClickViewList.add(native_ad_container_paster);


//        RelativeLayout.LayoutParams layoutParamsIconView = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(mActivity), new Double(ScreenUtils.getScreenWidth(mActivity) * Double.parseDouble("0.2")).intValue());
//        native_ad_container_paster.setLayoutParams(layoutParamsIconView);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mActivity.addContentView(mNativeIconAdsPasterView, layoutParams);
        mNativeIconAdsPasterView.setVisibility(View.GONE);
    }


    public void changeNativepasterView() {

        if (mNativeIconAdsPasterView == null) {
            initNativepasterView();
        }

        // if (null != mINativeAdData && mINativeAdData.isAdValid()) {
        if (null != mINativeAdPasterData) {

            /**
             *展示icon
             */
            if (mINativeAdPasterData.getIconFiles() != null && mINativeAdPasterData.getIconFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdPasterData.getIconFiles().get(0).getUrl())) {
                showImage(mINativeAdPasterData.getIconFiles().get(0).getUrl(), (ImageView) mNativeIconAdsPasterView.findViewById(R.id.native_ad_paster_icon));
            }

            /**
             *展示主图、大小为640X320。
             */
            if (null != mINativeAdPasterData.getImgFiles() && mINativeAdPasterData.getImgFiles().size() > 0 && !TextUtils.isEmpty(mINativeAdPasterData.getImgFiles().get(0).getUrl())) {

                showImage(mINativeAdPasterData.getImgFiles().get(0).getUrl(), (ImageView) mNativeIconAdsPasterView.findViewById(R.id.native_ad_paster_icon));
            }


            native_paster_title_tv.setText(null != mINativeAdPasterData.getTitle() ? mINativeAdPasterData.getTitle() : "");
            native_paster_desc_tv.setText(null != mINativeAdPasterData.getDesc() ? mINativeAdPasterData.getDesc() : "");

            native_ad_paster_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideNativeAdPaster();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MAdsManager.getInstance().intersShowingIsShowbanner = true;
                            MAdsManager.getInstance().showBanner();
                        }
                    }, 3000);
                }
            });

            mINativeAdPasterData.bindToView(mActivity, native_ad_container_paster, nativepasterClickViewList);
            showNativeAdPaster();
        }
    }

    public void showNativeAdPaster() {
        if (mActivity != null && mIconNativeAdPasterFlag && mINativeAdPasterData != null && mNativeIconAdsPasterView != null && !nativeAdIsShowing) {
            mNativeIconAdsPasterView.setVisibility(View.VISIBLE);

            mIconNativeAdPasterFlag = false;

            MAdsManager.getInstance().intersShowingIsShowbanner = false;
            MAdsManager.getInstance().hideBanner();

        }
    }

    public void hideNativeAdPaster() {
        if (mActivity != null && mNativeIconAdsPasterView != null) {
            mNativeIconAdsPasterView.setVisibility(View.GONE);
        }
    }

    //endregion


    private void showImage(String url, final ImageView imageView) {
        if (mActivity == null) {
            return;
        }
        if (mActivity.isDestroyed()) {
            return;
        }
        LogUtil.log_E("=================" + url);
        //ImageLoader.getInstance().displayImage(url, imageView);
        Glide.with(mActivity)
                .load(url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }
}

