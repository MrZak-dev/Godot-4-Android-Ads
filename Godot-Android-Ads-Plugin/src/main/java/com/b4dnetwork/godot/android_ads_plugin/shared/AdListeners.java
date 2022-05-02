package com.b4dnetwork.godot.android_ads_plugin.shared;

public class AdListeners {

    /**
     * Interstitial ad listener interface
     */
    public interface InterstitialListener {
        void onInterstitialLoaded(int adsProvider, String adName);
        void onInterstitialFailedToLoad(int adsProvider, String adName, int errorCode, String errorMessage);
        void onInterstitialOpened(int adsProvider, String adName);
        void onInterstitialClosed(int adsProvider, String adName);
    }


    public interface RewardedListener {
        void onRewardedLoaded(int adsProvider, String adName);
        void onRewardedFailedToLoad(int adsProvider, String adName, int errorCode, String errorMessage);
        void onRewardedOpened(int adsProvider, String adName);
        void onRewardedClosed(int adsProvider, String adName);
        void onReward(int adsProvider, String adName, String type, int amount);
    }


    public interface BannerListener {
        void onBannerLoaded(int adsProvider, String adName);
        void onBannerFailedToLoad(int adsProvider, String adName, int errorCode, String errorMessage);
        void onBannerShow(int adsProvider, String adName);
        void onBannerHide(int adsProvider, String adName);
    }

}


