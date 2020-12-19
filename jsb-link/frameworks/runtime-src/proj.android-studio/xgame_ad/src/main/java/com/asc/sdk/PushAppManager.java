package com.asc.sdk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.asc.sdk.bean.PushControllerBean;
import com.asc.sdk.platform.AppConfigs;
import com.asc.sdk.platform.AppUtils;
import com.asc.sdk.platform.LogUtil;
import com.asc.sdk.platform.ScreenUtils;
import com.asc.sdk.platform.StoreUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PushAppManager {
    private static boolean isPushAppIconOpen = true;
    private static boolean isPushAppGroupOpen = true;
    private static boolean isPushAppSettleOpen = true;
    private static PushAppManager instance;
    private Activity mainAct;


    public PushAppManager() {
    }

    public static PushAppManager getInstance() {
        if (instance == null) {
            instance = new PushAppManager();
        }
        return instance;
    }


    public void setActivity(Activity activity) {
        this.mainAct = activity;
        navigateIconListener = new NavigateIconListener();
    }

    public void changeData() {
        if (mainAct == null) {
            return;
        }

        String hutuiPush = StoreUtils.getString(mainAct, "pushSetting");

        if (!TextUtils.isEmpty(hutuiPush)) {
            try {

                Gson gson = new Gson();
                PushControllerBean huTuiControllerBean = gson.fromJson(hutuiPush, PushControllerBean.class);

                isHutuiOpen = false;
                if (huTuiControllerBean != null && huTuiControllerBean.getSetting() != null && huTuiControllerBean.getSetting().getSwitch_douyin().equals("1")) {
                    isHutuiOpen = true;
                }

                //互推关闭的时候不进行初始化
                if (!isHutuiOpen) {
                    return;
                }

                gameBeansUse = new ArrayList<>();
                if (huTuiControllerBean.getGame() != null) {
                    List<PushControllerBean.GameBean> gameBeans = huTuiControllerBean.getGame();
                    for (PushControllerBean.GameBean gameBean : gameBeans) {
                        if (gameBean.getChannel().equals(AppConfigs.CHANNER_NAME.name().toLowerCase())) {
                            if (!gameBean.getBundleName().equals(AppUtils.getPackageName(mainAct))) {
                                gameBeansUse.add(gameBean);
                            } else {
                                pushTime = Integer.parseInt(gameBean.getPushTime());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.log_E("initHutui======解析异常");
            }
        }
    }

    //region 互推单个icon、展示、隐藏、定时更换
    private ImageView ivImage = null;
    private String singleIcon;
    private boolean isHutuiOpen = false;
    private List<PushControllerBean.GameBean> gameBeansUse;
    private int navitateIconFlag = 0;
    private int pushTime = 20;
    private View navigateIconView;
    RelativeLayout.LayoutParams paramsNavigateIcon;


    private void initNavigateIcon(final int leftMargin, final int topMargin) {

        changeData();
        //没有可推送的应用，则不展示互推
        if (gameBeansUse == null || gameBeansUse.size() == 0) {
            return;
        }

        paramsNavigateIcon = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = LayoutInflater.from(mainAct);
        paramsNavigateIcon.setMargins(leftMargin, topMargin, 0, 0);

        navigateIconView = inflater.inflate(R.layout.activity_hutui_single, null);

        mainAct.addContentView(navigateIconView, paramsNavigateIcon);
        ivImage = (ImageView) navigateIconView.findViewById(R.id.iv_image);

        navitateIconFlag = 0;
        handler.sendEmptyMessage(1);


        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(singleIcon)) {
                    return;
                }
                Uri uri = Uri.parse(singleIcon);    //设置跳转的网站
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mainAct.startActivity(intent);
            }
        });

        TimerTaskNavigateIcon();
    }


    public void showNavigateIcon(int leftMargin, int topMargin) {

        if (mainAct == null || !isPushAppIconOpen) {
            return;
        }

        if (navigateIconView != null) {
            ViewGroup vg = (ViewGroup) navigateIconView.getParent();
            vg.removeView(navigateIconView);
        }
        initNavigateIcon(ScreenUtils.getScreenWidth(mainAct) * leftMargin / 100,
                ScreenUtils.getScreenHeight(mainAct) * topMargin / 100);
    }

    public void hideNavigateIcon() {
        if (navigateIconView != null) {
            navigateIconView.setVisibility(View.GONE);
        }

    }

    public void TimerTaskNavigateIcon() {

        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (gameBeansUse != null && gameBeansUse.size() != 0) {
                    navitateIconFlag++;
                    if (navitateIconFlag >= gameBeansUse.size()) {
                        navitateIconFlag = 0;
                    }

                    LogUtil.log_E("当前显示index=" + navitateIconFlag + "=列表总长度=" + gameBeansUse.size());
                    handler.sendEmptyMessage(1);
                }

            }
        };


        long delay = 20000;
        LogUtil.log_E("===pushTime===" + pushTime);
        long intevalPeriod = pushTime * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }
    //endregion


    //region  横版、竖版互推
    private ImageView iv_left = null;
    private ImageView iv_right = null;
    private ImageView iv_image_left_1 = null;
    private ImageView iv_image_left_2 = null;
    private ImageView iv_image_left_3 = null;
    private ImageView iv_image_left_4 = null;
    private NavigateIconListener navigateIconListener;
    private boolean openOrClose = false;
    private RelativeLayout relativeLayout_group;
    private LinearLayout linearLayout_group;

    private int navigateGroupFlag = 0;
    private List<ImageView> listImageView = new ArrayList<>();
    private List<String> listGroupUrl = new ArrayList<>();
    RelativeLayout.LayoutParams paramsNavigateGroup;

    private void initNavigateGroup(int type, int slide) {

        changeData();

        //没有可推送的应用，则不展示互推
        if (gameBeansUse == null || gameBeansUse.size() == 0) {
            return;
        }

        paramsNavigateGroup = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = LayoutInflater.from(mainAct);
        View view = inflater.inflate(R.layout.activity_hutui_multiple, null);


        if (view == null) {
            return;
        }

        mainAct.addContentView(view, paramsNavigateGroup);

        LinearLayout ll_app_icon = view.findViewById(R.id.ll_app_icon);
        if (type == 0) {  // type 0 横板，1  竖版
            ll_app_icon.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            ll_app_icon.setOrientation(LinearLayout.VERTICAL);
        }

        RelativeLayout.LayoutParams paramsLine = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        linearLayout_group = view.findViewById(R.id.linearLayout_group);
        paramsLine.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        iv_left = (ImageView) view.findViewById(R.id.iv_left);
        iv_right = (ImageView) view.findViewById(R.id.iv_right);

        iv_image_left_1 = (ImageView) view.findViewById(R.id.iv_image_left_1);
        iv_image_left_2 = (ImageView) view.findViewById(R.id.iv_image_left_2);
        iv_image_left_3 = (ImageView) view.findViewById(R.id.iv_image_left_3);
        iv_image_left_4 = (ImageView) view.findViewById(R.id.iv_image_left_4);

        listImageView.add(iv_image_left_1);
        listImageView.add(iv_image_left_2);
        listImageView.add(iv_image_left_3);
        listImageView.add(iv_image_left_4);

        if (slide == 0) {  //slide 0 向左滑出，1 向右滑出
            paramsLine.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            iv_left.setVisibility(View.GONE);
            iv_right.setVisibility(View.VISIBLE);
        } else {
            paramsLine.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            iv_left.setVisibility(View.VISIBLE);
            iv_right.setVisibility(View.GONE);
        }

        linearLayout_group.setLayoutParams(paramsLine);

        iv_image_left_1.setVisibility(View.GONE);
        iv_image_left_2.setVisibility(View.GONE);
        iv_image_left_3.setVisibility(View.GONE);
        iv_image_left_4.setVisibility(View.GONE);

        handler.sendEmptyMessage(2);

        if (navigateIconListener == null) {
            return;
        }
        iv_left.setOnClickListener(navigateIconListener);
        iv_right.setOnClickListener(navigateIconListener);
        iv_image_left_1.setOnClickListener(navigateIconListener);
        iv_image_left_2.setOnClickListener(navigateIconListener);
        iv_image_left_3.setOnClickListener(navigateIconListener);
        iv_image_left_4.setOnClickListener(navigateIconListener);

        TimerTaskNavigateGroup();

    }

    public void showNavigateGroup(int type, int slide) {

        if (mainAct == null || !isPushAppGroupOpen) {
            return;
        }
        if (linearLayout_group != null) {
            linearLayout_group.setVisibility(View.VISIBLE);
        } else {
            initNavigateGroup(type, slide);
        }

    }

    public void hideNavigateGroup() {
        if (linearLayout_group != null) {
            linearLayout_group.setVisibility(View.GONE);
        }
    }


    public void TimerTaskNavigateGroup() {

        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (gameBeansUse != null && gameBeansUse.size() != 0) {
                    navigateGroupFlag++;
                    if (navigateGroupFlag >= gameBeansUse.size()) {
                        navigateGroupFlag = 0;
                    }

                    LogUtil.log_E("当前显示index=" + navigateGroupFlag + "=列表总长度=" + gameBeansUse.size());
                    handler.sendEmptyMessage(2);
                }

            }
        };


        long delay = 20000;
        LogUtil.log_E("===pushTime===" + pushTime);
        long intevalPeriod = pushTime * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    //endregion

    //region 结算页互推
    View navigateSettleView;
    ImageView settle_one_icon1;
    ImageView settle_one_icon2;
    ImageView settle_one_icon3;
    ImageView settle_one_icon4;
    ImageView settle_one_icon5;
    ImageView settle_one_icon6;
    ImageView settle_two_icon1;
    ImageView settle_two_icon2;
    ImageView settle_two_icon3;
    ImageView settle_two_icon4;
    ImageView settle_three_icon1;
    ImageView settle_three_icon2;
    ImageView settle_three_icon3;
    ImageView settle_three_icon4;
    ImageView settle_three_icon5;

    private List<ImageView> listSettleImageView = new ArrayList<>();
    private List<String> listSettleUrl = new ArrayList<>();
    private List<Integer> listSttleFlag = new ArrayList<>();
    LinearLayout.LayoutParams paramsNavigateSettle;

    public void initNavigateSettle(int type, final int viewX, final int viewY) {

        changeData();
        //没有可推送的应用，则不展示互推
        if (gameBeansUse == null || gameBeansUse.size() == 0) {
            return;
        }

        paramsNavigateSettle = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = LayoutInflater.from(mainAct);

        listSettleImageView.clear();

        if (type == 1) {
            navigateSettleView = inflater.inflate(R.layout.pop_navigate_settle_one, null);
            settle_one_icon1 = navigateSettleView.findViewById(R.id.settle_one_icon1);
            settle_one_icon2 = navigateSettleView.findViewById(R.id.settle_one_icon2);
            settle_one_icon3 = navigateSettleView.findViewById(R.id.settle_one_icon3);
            settle_one_icon4 = navigateSettleView.findViewById(R.id.settle_one_icon4);
            settle_one_icon5 = navigateSettleView.findViewById(R.id.settle_one_icon5);
            settle_one_icon6 = navigateSettleView.findViewById(R.id.settle_one_icon6);
            if (navigateIconListener != null) {
                settle_one_icon1.setOnClickListener(navigateIconListener);
                settle_one_icon2.setOnClickListener(navigateIconListener);
                settle_one_icon3.setOnClickListener(navigateIconListener);
                settle_one_icon4.setOnClickListener(navigateIconListener);
                settle_one_icon5.setOnClickListener(navigateIconListener);
                settle_one_icon6.setOnClickListener(navigateIconListener);
            }
            listSettleImageView.add(settle_one_icon1);
            listSettleImageView.add(settle_one_icon2);
            listSettleImageView.add(settle_one_icon3);
            listSettleImageView.add(settle_one_icon4);
            listSettleImageView.add(settle_one_icon5);
            listSettleImageView.add(settle_one_icon6);
        } else if (type == 2) {
            navigateSettleView = inflater.inflate(R.layout.pop_navigate_settle_two, null);
            settle_two_icon1 = navigateSettleView.findViewById(R.id.settle_two_icon1);
            settle_two_icon2 = navigateSettleView.findViewById(R.id.settle_two_icon2);
            settle_two_icon3 = navigateSettleView.findViewById(R.id.settle_two_icon3);
            settle_two_icon4 = navigateSettleView.findViewById(R.id.settle_two_icon4);
            if (navigateIconListener != null) {
                settle_two_icon1.setOnClickListener(navigateIconListener);
                settle_two_icon2.setOnClickListener(navigateIconListener);
                settle_two_icon3.setOnClickListener(navigateIconListener);
                settle_two_icon4.setOnClickListener(navigateIconListener);
            }
            listSettleImageView.add(settle_two_icon1);
            listSettleImageView.add(settle_two_icon2);
            listSettleImageView.add(settle_two_icon3);
            listSettleImageView.add(settle_two_icon4);
        } else if (type == 3) {
            navigateSettleView = inflater.inflate(R.layout.pop_navigate_settle_three, null);
            settle_three_icon1 = navigateSettleView.findViewById(R.id.settle_three_icon1);
            settle_three_icon2 = navigateSettleView.findViewById(R.id.settle_three_icon2);
            settle_three_icon3 = navigateSettleView.findViewById(R.id.settle_three_icon3);
            settle_three_icon4 = navigateSettleView.findViewById(R.id.settle_three_icon4);
            settle_three_icon5 = navigateSettleView.findViewById(R.id.settle_three_icon5);
            if (navigateIconListener != null) {
                settle_three_icon1.setOnClickListener(navigateIconListener);
                settle_three_icon2.setOnClickListener(navigateIconListener);
                settle_three_icon3.setOnClickListener(navigateIconListener);
                settle_three_icon4.setOnClickListener(navigateIconListener);
                settle_three_icon5.setOnClickListener(navigateIconListener);
            }
            listSettleImageView.add(settle_three_icon1);
            listSettleImageView.add(settle_three_icon2);
            listSettleImageView.add(settle_three_icon3);
            listSettleImageView.add(settle_three_icon4);
            listSettleImageView.add(settle_three_icon5);
        }

        if (navigateSettleView == null) {
            return;
        }

        paramsNavigateSettle.setMargins(viewX, viewY, 0, 0);
        mainAct.addContentView(navigateSettleView, paramsNavigateSettle);

        handler.sendEmptyMessage(3);
        TimerTaskNavigateSettle();
    }


    public void showNavigateSettle(int type, int viewX, int viewY) {

        if (mainAct == null || !isPushAppIconOpen) {
            return;
        }

        if (navigateSettleView != null) {
            ViewGroup vg = (ViewGroup) navigateSettleView.getParent();
            vg.removeView(navigateSettleView);

        }
        initNavigateIcon(viewX, viewY);
    }

    public void hideNavigateSettle() {
        if (navigateSettleView != null) {
            navigateSettleView.setVisibility(View.GONE);
        }
    }

    public void TimerTaskNavigateSettle() {

        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(3);
            }
        };


        long delay = 20000;
        LogUtil.log_E("===pushTime===" + pushTime);
        long intevalPeriod = pushTime * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    //endregion


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    singleIcon = gameBeansUse.get(navitateIconFlag).getGameUrl();
                    Glide.with(mainAct)
                            .load(gameBeansUse.get(navitateIconFlag).getIcon())
                            .centerCrop()
                            .error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(ivImage);
                    break;
                case 2:
                    if (navigateGroupFlag >= gameBeansUse.size()) {
                        navigateGroupFlag = 0;
                    }
                    if (navigateGroupFlag < gameBeansUse.size()) {
                        listGroupUrl.add(gameBeansUse.get(navigateGroupFlag).getGameUrl());
                        Glide.with(mainAct)
                                .load(gameBeansUse.get(navigateGroupFlag).getIcon())
                                .centerCrop()
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(listImageView.get(0));
                    }
                    navigateGroupFlag++;
                    if (navigateGroupFlag >= gameBeansUse.size()) {
                        navigateGroupFlag = 0;
                    }
                    if (navigateGroupFlag < gameBeansUse.size()) {
                        listGroupUrl.add(gameBeansUse.get(navigateGroupFlag).getGameUrl());
                        Glide.with(mainAct)
                                .load(gameBeansUse.get(navigateGroupFlag).getIcon())
                                .centerCrop()
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(listImageView.get(1));
                    }

                    navigateGroupFlag++;
                    if (navigateGroupFlag >= gameBeansUse.size()) {
                        navigateGroupFlag = 0;
                    }
                    if (navigateGroupFlag < gameBeansUse.size()) {
                        listGroupUrl.add(gameBeansUse.get(navigateGroupFlag).getGameUrl());
                        Glide.with(mainAct)
                                .load(gameBeansUse.get(navigateGroupFlag).getIcon())
                                .centerCrop()
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(listImageView.get(2));
                    }

                    navigateGroupFlag++;
                    if (navigateGroupFlag >= gameBeansUse.size()) {
                        navigateGroupFlag = 0;
                    }
                    if (navigateGroupFlag < gameBeansUse.size()) {
                        listGroupUrl.add(gameBeansUse.get(navigateGroupFlag).getGameUrl());
                        Glide.with(mainAct)
                                .load(gameBeansUse.get(navigateGroupFlag).getIcon())
                                .centerCrop()
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(listImageView.get(3));
                    }

                    break;
                case 3:
                    listSttleFlag.clear();

                    while (listSttleFlag.size() < listSettleImageView.size()) {
                        int rad = new Random().nextInt(gameBeansUse.size());
                        listSttleFlag.add(rad);
                        LogUtil.log_E("=======1111======" + rad);
                    }

                    for (int i = 0; i < listSttleFlag.size(); i++) {

                        listSettleUrl.add(gameBeansUse.get(listSttleFlag.get(i)).getGameUrl());
                        Glide.with(mainAct)
                                .load(gameBeansUse.get(listSttleFlag.get(i)).getIcon())
                                .centerCrop()
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher)
                                .into(listSettleImageView.get(i));

                    }

                    break;
            }
        }
    };

    class NavigateIconListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Uri uri;
            Intent intent;


            if (v.getId() == R.id.iv_left) {
                if (openOrClose) {
                    openOrClose = false;
                    iv_image_left_1.setVisibility(View.GONE);
                    iv_image_left_2.setVisibility(View.GONE);
                    iv_image_left_3.setVisibility(View.GONE);
                    iv_image_left_4.setVisibility(View.GONE);
                } else {
                    openOrClose = true;
                    iv_image_left_1.setVisibility(View.VISIBLE);
                    iv_image_left_2.setVisibility(View.VISIBLE);
                    iv_image_left_3.setVisibility(View.VISIBLE);
                    iv_image_left_4.setVisibility(View.VISIBLE);
                }
                return;
            }

            if (v.getId() == R.id.iv_right) {
                if (openOrClose) {
                    openOrClose = false;
                    iv_image_left_1.setVisibility(View.GONE);
                    iv_image_left_2.setVisibility(View.GONE);
                    iv_image_left_3.setVisibility(View.GONE);
                    iv_image_left_4.setVisibility(View.GONE);
                } else {
                    openOrClose = true;
                    iv_image_left_1.setVisibility(View.VISIBLE);
                    iv_image_left_2.setVisibility(View.VISIBLE);
                    iv_image_left_3.setVisibility(View.VISIBLE);
                    iv_image_left_4.setVisibility(View.VISIBLE);
                }
                return;
            }

            if (v.getId() == R.id.iv_image_left_1) {
                if (listGroupUrl.size() > 0) {
                    uri = Uri.parse(listGroupUrl.get(0));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }

            if (v.getId() == R.id.iv_image_left_2) {
                if (listGroupUrl.size() > 1) {
                    uri = Uri.parse(listGroupUrl.get(1));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.iv_image_left_3) {
                if (listGroupUrl.size() > 2) {
                    uri = Uri.parse(listGroupUrl.get(3));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.iv_image_left_4) {
                if (listGroupUrl.size() > 3) {
                    uri = Uri.parse(listGroupUrl.get(3));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_one_icon1) {
                if (listSettleUrl.size() > 0) {
                    uri = Uri.parse(listSettleUrl.get(0));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }


            if (v.getId() == R.id.settle_one_icon2) {
                if (listSettleUrl.size() > 1) {
                    uri = Uri.parse(listSettleUrl.get(1));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_one_icon3) {
                if (listSettleUrl.size() > 2) {
                    uri = Uri.parse(listSettleUrl.get(2));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }


            if (v.getId() == R.id.settle_one_icon4) {
                if (listSettleUrl.size() > 3) {
                    uri = Uri.parse(listSettleUrl.get(3));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_one_icon5) {
                if (listSettleUrl.size() > 4) {
                    uri = Uri.parse(listSettleUrl.get(4));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }

            if (v.getId() == R.id.settle_one_icon6) {
                if (listSettleUrl.size() > 5) {
                    uri = Uri.parse(listSettleUrl.get(5));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_two_icon1) {
                if (listSettleUrl.size() > 0) {
                    uri = Uri.parse(listSettleUrl.get(0));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_two_icon2) {
                if (listSettleUrl.size() > 1) {
                    uri = Uri.parse(listSettleUrl.get(1));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_two_icon3) {
                if (listSettleUrl.size() > 2) {
                    uri = Uri.parse(listSettleUrl.get(2));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_two_icon4) {
                if (listSettleUrl.size() > 3) {
                    uri = Uri.parse(listSettleUrl.get(3));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_three_icon1) {
                if (listSettleUrl.size() > 0) {
                    uri = Uri.parse(listSettleUrl.get(0));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_three_icon2) {
                if (listSettleUrl.size() > 1) {
                    uri = Uri.parse(listSettleUrl.get(1));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_three_icon3) {
                if (listSettleUrl.size() > 2) {
                    uri = Uri.parse(listSettleUrl.get(2));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_three_icon4) {
                if (listSettleUrl.size() > 3) {
                    uri = Uri.parse(listSettleUrl.get(3));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
            if (v.getId() == R.id.settle_three_icon5) {
                if (listSettleUrl.size() > 4) {
                    uri = Uri.parse(listSettleUrl.get(4));    //设置跳转的网站
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    mainAct.startActivity(intent);
                }
                return;
            }
        }
    }
}
