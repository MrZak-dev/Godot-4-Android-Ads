package com.b4dnetwork.godot.android_ads_plugin.shared;

public class AdListeners {
    /**
     * Interstitial ad listener interface
     */
    public interface InterstitialListener {
        public void onInterstitialLoaded();
        public void onInterstitialFailedToLoad(int errorCode, String errorMessage);
        public void onInterstitialOpened();
        public void onInterstitialClosed();
    }


    public interface RewardedListener {
        public void onRewardedLoaded();
        public void onRewardedFailedToLoad(int errorCode, String errorMessage);
        public void onRewardedOpened();
        public void onRewardedClosed();
        public void onReward(String type, int amount);
    }


    public interface BannerListener {
        public void onBannerLoaded();
        public void onBannerFailedToLoad(int errorCode, String errorMessage);
        public void onBannerShow();
        public void onBannerHide();
    }

}

