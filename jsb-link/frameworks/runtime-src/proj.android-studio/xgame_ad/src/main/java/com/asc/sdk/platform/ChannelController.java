package com.asc.sdk.platform;

public class ChannelController {

    public enum ChannelNameEnum {
        Douyin, Oppo, Vivo, Redmi
    }

    public static String getAdChanner() {
        String url;
        switch (AppConfigs.CHANNER_NAME) {
            case Oppo:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/oppo_ad_control.json";
                break;
            case Vivo:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/vivo_ad_control.json";
                break;
            case Redmi:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/douyin_ad_control.json";
                break;
            case Douyin:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/douyin_ad_control.json";
                break;
            default:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/douyin_ad_control.json";
                break;
        }
        return url;
    }

    public static String getPushChanner() {
        String url;
        switch (AppConfigs.CHANNER_NAME) {
            case Oppo:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/Android_pushControl.json";
                break;
            case Vivo:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/Android_pushControl.json";
                break;
            case Redmi:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/Android_pushControl.json";
                break;
            case Douyin:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/Android_pushControl.json";
                break;
            default:
                url = "https://minigame-1300797998.cos.ap-guangzhou.myqcloud.com/DengJie/Android/Android_pushControl.json";
                break;
        }
        return url;
    }
}
