package com.asc.sdk.bean;

import java.util.List;

public class AdControllerBean {
    /**
     * Game : [{"gid":"1","showBanner":"1","showInters":"1","showInterVideo":"0","showBanner_time":"20","showInters_time":"10","showInterVideo_time":"0","showInters_frequency":"2","showInterVideo_frequency":"0","bundleName":"com.xplaygame.xuancaijiezousand.vivo","showStart_time":"30","name":"炫彩节奏3D","showNativeInters":"1","showNativeInters_time":"1","showNativeInters_frequency":"0"}]
     * setting : {"showBanner":"1","showInters":"1","showInterVideo":"1","showNativeInter":"1"}
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
         * showBanner : 1
         * showInters : 1
         * showInterVideo : 1
         * showNativeInter : 1
         */

        private String showBanner;
        private String showInters;
        private String showInterVideo;
        private String showNativeInters;

        public String getShowBanner() {
            return showBanner;
        }

        public void setShowBanner(String showBanner) {
            this.showBanner = showBanner;
        }

        public String getShowInters() {
            return showInters;
        }

        public void setShowInters(String showInters) {
            this.showInters = showInters;
        }

        public String getShowInterVideo() {
            return showInterVideo;
        }

        public void setShowInterVideo(String showInterVideo) {
            this.showInterVideo = showInterVideo;
        }

        public String getShowNativeInters() {
            return showNativeInters;
        }

        public void setShowNativeInters(String showNativeInters) {
            this.showNativeInters = showNativeInters;
        }

        public boolean isShowBanner() {
            if (showBanner == null) {
                return true;
            }
            return !showBanner.equals("0");
        }

        public boolean isShowInters() {
            if (showInters == null) {
                return true;
            }
            return !showInters.equals("0");
        }

        public boolean isShowIntersVideo() {
            if (showInterVideo == null) {
                return true;
            }
            return !showInterVideo.equals("0");
        }

        public boolean isShowNativeInters() {
            if (showNativeInters == null) {
                return true;
            }
            return !showNativeInters.equals("0");
        }
    }

    public static class GameBean {
        /**
         * gid : 1
         * showBanner : 1
         * showInters : 1
         * showInterVideo : 0
         * showBanner_time : 20
         * showInters_time : 10
         * showInterVideo_time : 0
         * showInters_frequency : 2
         * showInterVideo_frequency : 0
         * bundleName : com.xplaygame.xuancaijiezousand.vivo
         * showStart_time : 30
         * name : 炫彩节奏3D
         * showNativeInters : 1
         * showNativeInters_time : 1
         * showNativeInters_frequency : 0
         * nativeIntersDelayTime : 0
         * errorClickProbability : 0.5
         * intersFlagNum 10
         *  nativeIconOpen false
         *  nativePasterOpne false
         */

        private String showBanner;
        private String showInters;
        private String showInterVideo;
        private String showBanner_time = "20";
        private String showInters_time = "10";
        private String showInterVideo_time = "20";
        private String showInters_frequency = "0";
        private String showInterVideo_frequency = "0";
        private String bundleName;
        private String showStart_time = "20";

        private String gid;
        private String name;
        private String showNativeInters;
        private String showNativeInters_time = "0";
        private String showNativeInters_frequency = "0";
        private String nativeIntersDelayTime = "0";
        private String errorClickProbability = "0.01";
        private String intersFlagNum = "999";
        private boolean nativeIconOpen=true;
        private boolean nativePasterOpen =true;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getShowBanner() {
            return showBanner;
        }

        public void setShowBanner(String showBanner) {
            this.showBanner = showBanner;
        }

        public String getShowInters() {
            return showInters;
        }

        public void setShowInters(String showInters) {
            this.showInters = showInters;
        }

        public String getShowInterVideo() {
            return showInterVideo;
        }

        public void setShowInterVideo(String showInterVideo) {
            this.showInterVideo = showInterVideo;
        }

        public String getShowBanner_time() {
            return showBanner_time;
        }

        public void setShowBanner_time(String showBanner_time) {
            this.showBanner_time = showBanner_time;
        }

        public String getShowInters_time() {
            return showInters_time;
        }

        public void setShowInters_time(String showInters_time) {
            this.showInters_time = showInters_time;
        }

        public String getShowInterVideo_time() {
            return showInterVideo_time;
        }

        public void setShowInterVideo_time(String showInterVideo_time) {
            this.showInterVideo_time = showInterVideo_time;
        }

        public String getShowInters_frequency() {
            return showInters_frequency;
        }

        public void setShowInters_frequency(String showInters_frequency) {
            this.showInters_frequency = showInters_frequency;
        }

        public String getShowInterVideo_frequency() {
            return showInterVideo_frequency;
        }

        public void setShowInterVideo_frequency(String showInterVideo_frequency) {
            this.showInterVideo_frequency = showInterVideo_frequency;
        }

        public String getBundleName() {
            return bundleName;
        }

        public void setBundleName(String bundleName) {
            this.bundleName = bundleName;
        }

        public String getShowStart_time() {
            return showStart_time;
        }

        public void setShowStart_time(String showStart_time) {
            this.showStart_time = showStart_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShowNativeInters() {
            return showNativeInters;
        }

        public void setShowNativeInters(String showNativeInters) {
            this.showNativeInters = showNativeInters;
        }

        public String getShowNativeInters_time() {
            return showNativeInters_time;
        }

        public void setShowNativeInters_time(String showNativeInters_time) {
            this.showNativeInters_time = showNativeInters_time;
        }

        public String getShowNativeInters_frequency() {
            return showNativeInters_frequency;
        }

        public void setShowNativeInters_frequency(String showNativeInters_frequency) {
            this.showNativeInters_frequency = showNativeInters_frequency;
        }

        public String getNativeIntersDelayTime() {
            return nativeIntersDelayTime;
        }

        public void setNativeIntersDelayTime(String nativeIntersDelayTime) {
            this.nativeIntersDelayTime = nativeIntersDelayTime;
        }

        public String getErrorClickProbability() {
            return errorClickProbability;
        }

        public void setErrorClickProbability(String errorClickProbability) {
            this.errorClickProbability = errorClickProbability;
        }

        public String getIntersFlagNum() {
            return intersFlagNum;
        }

        public void setIntersFlagNum(String intersFlagNum) {
            this.intersFlagNum = intersFlagNum;
        }

        public boolean isNativeIconOpen() {
            return nativeIconOpen;
        }

        public void setNativeIconOpen(boolean nativeIconOpen) {
            this.nativeIconOpen = nativeIconOpen;
        }

        public boolean isNativePasterOpen() {
            return nativePasterOpen;
        }

        public void setNativePasterOpen(boolean nativePasterOpen) {
            this.nativePasterOpen = nativePasterOpen;
        }

        public boolean isShowBanner() {
            if (showBanner == null) {
                return true;
            }
            return !showBanner.equals("0");
        }

        public boolean isShowInters() {
            if (showInters == null) {
                return true;
            }
            return !showInters.equals("0");
        }

        public boolean isShowIntersVideo() {
            if (showInterVideo == null) {
                return true;
            }
            return !showInterVideo.equals("0");
        }

        public boolean isShowNativeInters() {
            if (showNativeInters == null) {
                return true;
            }
            return !showNativeInters.equals("0");
        }
    }
}
