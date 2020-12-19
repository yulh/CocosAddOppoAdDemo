package com.asc.sdk.platform;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.asc.sdk.SecActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AdControllerUtil {

    public static void getAdControllerData(final Activity conS, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //String urlStr = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/douyin_ad_control.json";
                BufferedReader in = null;
                InputStream input = null;
                HttpURLConnection conn = null;
                try {
                    /*
                     * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
                     * <uses-permission android:name="android.permission.INTERNET" />
                     */
                    URL url = new URL(ChannelController.getAdChanner());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setReadTimeout(10 * 1000);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() != 200) {
                        LogUtil.log_E("GetAd====获取服务器数据失败====" + conn.getResponseCode());
                        handler.sendEmptyMessage(SecActivity.GET_PUSH_APP_SETTING);
                        return;
                    }
                    // 取得inputStream，并进行读取
                    input = conn.getInputStream();
                    in = new BufferedReader(new InputStreamReader(input));
                    String line = null;
                    StringBuffer sb = new StringBuffer();
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }

                    LogUtil.log_E("AdControllerUtil===============" + sb.toString());
                    if (!TextUtils.isEmpty(sb.toString())) {
                        StoreUtils.putString(conS, AppConfigs.AD_CONTROLLER_DATA, sb.toString());
                    }

                } catch (MalformedURLException e) {
                    LogUtil.log_E("GetAd====MalformedURLException");
                    e.printStackTrace();
                } catch (Exception e) {
                    LogUtil.log_E("GetAd====Exception");
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (input != null) {
                            input.close();
                        }

                        if (conn != null) {
                            conn.disconnect();
                        }

                    } catch (Exception e) {
                        LogUtil.log_E("GetAd====流关闭异常");
                        e.printStackTrace();
                    }
                    LogUtil.log_E("GetAd====获取pushApp数据");
                    handler.sendEmptyMessage(SecActivity.GET_PUSH_APP_SETTING);
                }

            }
        }).start();

    }
}
