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

public class PushControllerUtil {

    public static void getPushControllerData(final Activity conS, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //String urlStr = "http://tencentcnd.minigame.xplaymobile.com/DengJie/adIntervalController.json";
                //String urlStr = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/Android_pushControl.json";
                //long a = System.currentTimeMillis();
                BufferedReader in = null;
                InputStream input = null;
                HttpURLConnection conn = null;
                try {
                    /*
                     * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
                     * <uses-permission android:name="android.permission.INTERNET" />
                     */
                    URL url = new URL(ChannelController.getPushChanner());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setReadTimeout(10 * 1000);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() != 200) {
                        LogUtil.log_E("pushApp===获取服务器数据失败====" + conn.getResponseCode());
                        handler.sendEmptyMessage(SecActivity.GET_INIT_AD_SDK);
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
                    //System.out.println(sb.toString());
                    //newVersion = Integer.parseInt(sb.toString());

                    LogUtil.log_E("hutui---push===============" + sb.toString());
                    if (!TextUtils.isEmpty(sb.toString())) {
                        StoreUtils.putString(conS, "pushSetting", sb.toString());
                    }


                } catch (MalformedURLException e) {
                    LogUtil.log_E("pushApp===MalformedURLException");
                    e.printStackTrace();
                } catch (Exception e) {
                    LogUtil.log_E("pushApp===Exception");
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
                        LogUtil.log_E("pushApp===流关闭异常");
                        e.printStackTrace();
                    }
                    LogUtil.log_E("pushApp===初始化广告SDk");
                    handler.sendEmptyMessage(SecActivity.GET_INIT_AD_SDK);
                }

            }
        }).start();

    }
}
