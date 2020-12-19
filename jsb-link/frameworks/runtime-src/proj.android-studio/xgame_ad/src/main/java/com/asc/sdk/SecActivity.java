package com.asc.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.asc.sdk.ndk.AndroidNDKHelper;
import com.asc.sdk.platform.AdControllerUtil;
import com.asc.sdk.platform.AppConfigs;
import com.asc.sdk.platform.AppUtils;
import com.asc.sdk.platform.LogUtil;
import com.asc.sdk.platform.PushControllerUtil;
import com.asc.sdk.platform.ScreenUtils;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.common.model.ApiResult;
import com.nearme.game.sdk.common.model.biz.ReportUserGameInfoParam;
import com.nearme.game.sdk.common.model.biz.ReqUserInfoParam;
import com.smarx.notchlib.NotchScreenManager;
import com.umeng.analytics.MobclickAgent;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class SecActivity extends Cocos2dxActivity {

    private static Activity conS;
    public static final int GET_AD_SETTING = 1;
    public static final int GET_PUSH_APP_SETTING = 2;
    public static final int GET_INIT_AD_SDK = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conS = this;
        AndroidNDKHelper.SetNDKReceiver(this);
        NotchScreenManager.getInstance().setDisplayInNotch(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(GET_AD_SETTING);
            }
        }, 1000);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MAdsManager.getInstance().releaseAdsObject();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 退出
     */
    private void exit() {
        GameCenterSDK.getInstance().onExit(SecActivity.this, new GameExitCallback() {
            @Override
            public void exitGame() {
                System.exit(0);
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //这里写需要重写的方法

            exit();
            return false;
        }
        return false;
    }


    /**
     * 向渠道提交用户信息。 在创建游戏角色、进入游戏和角色升级3个地方调用此接口，当创建角色时最后一个参数值为true，其他两种情况为false。
     * GameRoleInfo所有字段均不能传null，游戏没有的字段请传一个默认值或空字符串。
     */
    public void setUserInfo() {
        GameCenterSDK.getInstance().doReportUserGameInfoData(
                new ReportUserGameInfoParam("default", "default", 0, "default", "default", "default", null), new ApiCallback() {

                    public void onSuccess(String resultMsg) {
                        Toast.makeText(SecActivity.this, "success",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String resultMsg, int resultCode) {
                        Toast.makeText(SecActivity.this, resultMsg,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * 设置通知，用于监听初始化，登录，注销，支付及退出功能的返回值
     */
    private void initQkNotifiers() {

        GameCenterSDK.getInstance().doLogin(SecActivity.this, new ApiCallback() {

            @Override
            public void onSuccess(String arg0) {
                doGetTokenAndSsoid();
            }

            @Override
            public void onFailure(String arg0, int arg1) {
                Toast.makeText(SecActivity.this, arg0, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void doGetTokenAndSsoid() {
        GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {

            @Override
            public void onSuccess(String resultMsg) {
                try {
                    JSONObject json = new JSONObject(resultMsg);
                    String token = json.getString("token");
                    String ssoid = json.getString("ssoid");
                    doGetUserInfoByCpClient(token, ssoid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content, int resultCode) {

            }
        });
    }

    private void doGetUserInfoByCpClient(String token, String ssoid) {
        GameCenterSDK.getInstance().doGetUserInfo(
                new ReqUserInfoParam(token, ssoid), new ApiCallback() {

                    @Override
                    public void onSuccess(String resultMsg) {
                        setUserInfo();
                        Toast.makeText(SecActivity.this, resultMsg,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String resultMsg, int resultCode) {

                    }
                });
    }

    // region 游戏调用接口

    /**
     * 退出游戏
     *
     * @param msg
     */
    public static void exit(String msg) {
        LogUtil.log_E("SecActivity===============exit");
        MAdsManager.getInstance().exitGame();
    }

    /**
     * 显示banner
     */
    public static void showBanner(String msg) {
        LogUtil.log_E("SecActivity===============showBanner");
        MAdsManager.getInstance().showBanner();
    }

    /**
     * 隐藏banner
     */
    public static void hideBanner(String msg) {
        LogUtil.log_E("SecActivity===============hideBanner");
        MAdsManager.getInstance().hideBanner();
    }


    /**
     * 显示插屏
     *
     * @param index
     */
    public static void showInters(final String index) {
        LogUtil.log_E("SecActivity===============showInters");
        MAdsManager.getInstance().showInters();
    }

    /**
     * 插屏是否存在
     *
     * @return
     */
    public static boolean getIntersFlag() {
        LogUtil.log_E("SecActivity===============getIntersFlag");
        return MAdsManager.getInstance().getIntersFlag();
    }


    /**
     * 显示激励视频
     *
     * @param msg
     */
    public static void showVideo(String msg) {
        LogUtil.log_E("SecActivity===============showVideo");
        MAdsManager.getInstance().showVideo();
    }

    /**
     * 激励视频是否存在
     *
     * @return
     */
    public static boolean getVideoFlag() {
        LogUtil.log_E("SecActivity===============getVideoFlag");
        return MAdsManager.getInstance().getVideoFlag();
    }


    /**
     * Oppo进入游戏中心
     *
     * @param msg
     */
    public static void jumpGameCenter(String msg) {
        LogUtil.log_E("SecActivity===============jumpGameCenter");
        //Extend.getInstance().callFunction(conS, 304);
    }

    /**
     * 展示插屏视频
     *
     * @param msg
     */
    public static void showVideoInters(String msg) {
        LogUtil.log_E("SecActivity==========showVideoInters");
        //MAdsManager.getInstance().showVideoInsert();
    }

    /**
     * 插屏视频是否存在
     *
     * @return
     */
    public static boolean getVideoIntersFlag() {
        LogUtil.log_E("SecActivity==========getVideoIntersFlag");
        //return MAdsManager.getInstance().getVideoInsertFlag();
        return false;
    }


    /**
     * 显示互推Icon
     *
     * @param icon_Size
     */
    public static void showNavigateIcon(String icon_Size) {
        LogUtil.log_E("SecActivity==========showNavigateIcon==icon_Size:" + icon_Size);
        PushAppManager.getInstance().showNavigateIcon(72, 5);
    }

    /**
     * 隐藏互推Icon
     *
     * @param msg
     */
    public static void hideNavigateIcon(String msg) {
        LogUtil.log_E("SecActivity==========hideNavigateIcon");
        PushAppManager.getInstance().hideNavigateIcon();
    }

    /**
     * 显示互推列表
     *
     * @param msg
     */
    public static void showNavigateGroup(String msg) {
        LogUtil.log_E("SecActivity==========showNavigateGroup");
        //PushAppManager.getInstance().showNavigateGroup(1, 0);
    }

    /**
     * 隐藏互推列表
     *
     * @param msg
     */
    public static void hideNavigateGroup(String msg) {
        LogUtil.log_E("SecActivity==========hideNavigateGroup");
        //PushAppManager.getInstance().hideNavigateGroup();
    }

    /**
     * 显示结算页互推
     *
     * @param msg
     */
    public static void showNavigateSettle(String msg) {
        LogUtil.log_E("SecActivity==========showNavigateSettle");
        //PushAppManager.getInstance().showNavigateSettle(1, 0, 0);
    }

    /**
     * 隐藏结算页互推
     *
     * @param msg
     */
    public static void hideNavigateSettle(String msg) {
        LogUtil.log_E("SecActivity==========hideNavigateSettle");
        //PushAppManager.getInstance().hideNavigateSettle();
    }

    /**
     * Android原生广告
     */
    public static void showNativeAd(String msg) {
        LogUtil.log_E("========================showNativeAd");
    }


    /**
     * Oppo 超休闲
     */
    public static void showOPPOMoreGame(String msg) {
        LogUtil.log_E("=============== secActivity 进入游戏中心");
        GameCenterSDK.getInstance().jumpLeisureSubject();
    }

    /**
     * 判断是否显示更多游戏
     */
    public static boolean getShowMoreGameFlag() {
        LogUtil.log_E("secActivity=============== 判断是否显示更多游戏");
        return AppConfigs.isHasMoreGame;
    }

    /**
     * 震动
     *
     * @param msg
     */
    public void shakePhone(String msg) {

        Vibrator vibrator;
        Log.e("-------------震动-----", msg);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (msg.equals("short")) {
            vibrator.vibrate(50);
        } else {
            vibrator.vibrate(500);
        }

    }

    /**
     * 退出游戏
     */
    public static void exitGame() {
        MAdsManager.getInstance().exitGame();
    }

    /**
     * 游客体验
     *
     * @param msg
     */
    public void TouristMode(String msg) {
        LogUtil.log_E("游客体验");
    }

    /**
     * 实名认证
     *
     * @param msg
     */
    public void showAutentication(String msg) {
        LogUtil.log_E("实名认证");
    }


    /**
     * 自定义事件上报
     *
     * @param msg
     */
    public static void reportAnalytics(String msg) {
        LogUtil.log_E("SecActivity==========reportAnalytics");

        String eventId = "";
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject(msg);
            if (jsonObject.has("eventId")) {
                eventId = jsonObject.getString("eventId");
            }
            if (jsonObject.has("data")) {
                data = jsonObject.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(eventId) || TextUtils.isEmpty(data)) {
            return;
        }

        LogUtil.log_E("自定义事件上报");
        MobclickAgent.onEventObject(conS, eventId, AppUtils.jsonToMap(data));
    }

    /**
     * 原生Icon 判断是否存在原生Icon
     */
    public static boolean getIconNativeFlag() {
        return NativeAdsManager.getInstance().getIconNativeAd();
    }

    /**
     * 展示原生Icon
     */
    public static void showNativeIcon(String msg) {
        String icon_size = null;
        String viewX = null;
        String viewY = null;

        LogUtil.log_E("==================" + msg);

        try {
            JSONObject jsonObject = new JSONObject(msg);

            if (jsonObject.has("icon_size")) {
                icon_size = jsonObject.getString("icon_size");
            }

            if (jsonObject.has("viewX")) {
                viewX = jsonObject.getString("viewX");
            }

            if (jsonObject.has("viewY")) {
                viewY = jsonObject.getString("viewY");
            }

            NativeAdsManager.getInstance().showNativeAdsIcon(new Double(ScreenUtils.getScreenWidth(conS) * Double.parseDouble(icon_size)).intValue(),
                    new Double(ScreenUtils.getScreenWidth(conS) * Double.parseDouble(viewX)).intValue(),
                    new Double(ScreenUtils.getScreenHeight(conS) * Double.parseDouble(viewY)).intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏原生Icon
     */
    public static void hideNativeIcon(String msg) {
        NativeAdsManager.getInstance().hideNativeAdsIcon();
    }


    //endregion

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_AD_SETTING:
                    AdControllerUtil.getAdControllerData(conS, handler);
                    break;
                case GET_PUSH_APP_SETTING:
                    //获取服务互推数据
                    PushControllerUtil.getPushControllerData(conS, handler);
                    break;
                case GET_INIT_AD_SDK:
                    //初始化广告
                    MAdsManager.getInstance().initMadsSdk(SecActivity.this);
                    PushAppManager.getInstance().setActivity(SecActivity.this);
                    break;
            }
        }
    };

    /**
     * 防沉迷
     */
    private void getVerifiedInfo() {
        GameCenterSDK.getInstance().doGetVerifiedInfo(new ApiCallback() {
            @Override
            public void onSuccess(String resultMsg) {
                try {
                    //解析年龄age
                    int age = Integer.parseInt(resultMsg);
                    if (age < 18) {
                        LogUtil.log_E("已实名但未成年，CP开始处理防沉迷");
                    } else {
                        LogUtil.log_E("已实名且已成年，尽情玩游戏吧~");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String resultMsg, int resultCode) {
                if (resultCode == ApiResult.RESULT_CODE_VERIFIED_FAILED_AND_RESUME_GAME) {
                    LogUtil.log_E(resultMsg + "，还可以继续玩游戏");
                } else if (resultCode == ApiResult.RESULT_CODE_VERIFIED_FAILED_AND_STOP_GAME) {
                    LogUtil.log_E(resultMsg + ",CP自己处理退出游戏");
                }
            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
            MAdsManager.getInstance().exitGame();
        }
        return super.dispatchKeyEvent(event);
    }
}
