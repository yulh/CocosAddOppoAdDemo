package com.asc.sdk.platform;

public class AppConfigs {

    public static final boolean isHasMoreGame = false; // 暂时只有oppo 超休闲有更多游戏

    public static final String FIRST_OPEN_APP = "FIRST_OPEN_APP"; // 第一次打开APP

    public static final ChannelController.ChannelNameEnum CHANNER_NAME = ChannelController.ChannelNameEnum.Oppo; //发布平台
    public static final String AD_CONTROLLER_DATA = "AD_CONTROLLER_DATA"; // 广告控制参数
    public static final String SP_IS_FIRST_GET_INTERS_FLAG = "SP_IS_FIRST_GET_INTERS_FLAGA";  //首次打开app,是否展示插屏， 0：不展示，1：不展示保存的时间与showStart_time 时间比较，>1：与showInters_time比较
    public static final String SP_INTERS_OR_INTERVIDEO_LOCAL_TIME = "SP_INTERS_OR_INTERVIDEO_LOCAL_TIME"; //插屏或插屏视频的显示时间

    public static final String GAME_SDK_APP_ID = "437712f053054d239ec185028b8ad1c5"; // 游戏appID

    public static final String UMENG_SDK_APP_KEY = "5f029174167edd385400034a";
    public static final String UMENG_SDK_CHANNEL = AppConfigs.CHANNER_NAME.name();

    // region 广告AppID 与 广告位ID
    public static final String APP_ID = "30248200";//应用ID
    public static final String BANNER_POS_ID = "162326";//Banner广告位ID
    public static final String INTERSTITIAL_POS_ID = "162327";//插屏广告位ID
    //
    public static final String SPLASH_POS_ID = "162328";//开屏广告位ID
    public static final String REWARD_VIDEO_POS_ID = "162329";//激励视频广告位ID
    public static final String NATIVE_ADVANCE_POS_ID = "233513";//512X512图文下载类原生广告样式广告位ID

    // endregion
}
