package com.asc.sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.asc.sdk.platform.LogUtil;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.log_E("1111=============== 闪屏1");
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
			LogUtil.log_E("=============== 闪屏2");
            finish();
            return;
        }

		LogUtil.log_E("onSplashStop =====================跳转开屏广告");
		Intent intent = new Intent(this, ChannelSplashActivity.class);
		startActivity(intent);
		finish();
    }
}
