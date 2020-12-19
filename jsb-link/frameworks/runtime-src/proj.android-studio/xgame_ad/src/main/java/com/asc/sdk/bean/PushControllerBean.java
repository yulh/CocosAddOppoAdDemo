package com.asc.sdk.bean;

import java.util.List;

public class PushControllerBean {


    /**
     * Game : [{"name":"全民欢乐球球","bundleName":"com.xplaygame.jiezhouqiuqiu.douyin","pushTime":"20","icon":"http://tencentcnd.minigame.xplaymobile.com/DengJie/Android/jiezouqiuqiu_Android.png","gameUrl":"https://ad.toutiao.com/advertiser_package/dl/fa2c4c6e_1664821095381005_1592213439061","channel":"douyin"},{"name":"指尖节奏3D","bundleName":"com.xplaygame.zhijianjiezousand.douyin","pushTime":"20","icon":"http://tencentcnd.minigame.xplaymobile.com/DengJie/Android/zhiJianJieZou_Android.png","gameUrl":"https://ad.toutiao.com/advertiser_package/dl/822fe16b_1664821095381005_1588068162112","channel":"douyin"},{"name":"炫彩节奏3D","bundleName":"com.xplaygame.xuancaijiezousand.douyin","pushTime":"20","icon":"http://tencentcnd.minigame.xplaymobile.com/DengJie/Android/xuanCaiJieZou_Android.png","gameUrl":"https://ad.toutiao.com/advertiser_package/dl/c00f7b8d_1664821095381005_1587715793988","channel":"douyin"}]
     * setting : {"switch_douyin":"1","switch_kuaishou":"1"}
     */

    private SettingBean setting;
    private List<GameBean> Game;

    public SettingBean getSetting() {
        return setting;
    }

    public void setSetting(SettingBean setting) {
        this.setting = setting;
    }

    public List<GameBean> getGame() {
        return Game;
    }

    public void setGame(List<GameBean> Game) {
        this.Game = Game;
    }

    public static class SettingBean {
        /**
         * switch_douyin : 1
         * switch_kuaishou : 1
         */

        private String switch_douyin;
        private String switch_kuaishou;

        public String getSwitch_douyin() {
            return switch_douyin;
        }

        public void setSwitch_douyin(String switch_douyin) {
            this.switch_douyin = switch_douyin;
        }

        public String getSwitch_kuaishou() {
            return switch_kuaishou;
        }

        public void setSwitch_kuaishou(String switch_kuaishou) {
            this.switch_kuaishou = switch_kuaishou;
        }
    }

    public static class GameBean {
        /**
         * name : 全民欢乐球球
         * bundleName : com.xplaygame.jiezhouqiuqiu.douyin
         * pushTime : 20
         * icon : http://tencentcnd.minigame.xplaymobile.com/DengJie/Android/jiezouqiuqiu_Android.png
         * gameUrl : https://ad.toutiao.com/advertiser_package/dl/fa2c4c6e_1664821095381005_1592213439061
         * channel : douyin
         */

        private String name;
        private String bundleName;
        private String pushTime;
        private String icon;
        private String gameUrl;
        private String channel;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBundleName() {
            return bundleName;
        }

        public void setBundleName(String bundleName) {
            this.bundleName = bundleName;
        }

        public String getPushTime() {
            return pushTime;
        }

        public void setPushTime(String pushTime) {
            this.pushTime = pushTime;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getGameUrl() {
            return gameUrl;
        }

        public void setGameUrl(String gameUrl) {
            this.gameUrl = gameUrl;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }
}
