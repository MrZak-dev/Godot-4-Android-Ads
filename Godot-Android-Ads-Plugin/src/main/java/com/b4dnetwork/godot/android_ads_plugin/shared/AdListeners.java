package com.b4dnetwork.godot.android_ads_plugin.shared;

public class AdListeners {

    /**
     * Interstitial ad listener interface
     */
    public interface InterstitialListener {
        void onInterstitialLoaded(int adsProvider);
        void onInterstitialFailedToLoad(int adsProvider, int errorCode, String errorMessage);
        void onInterstitialOpened(int adsProvider);
        void onInterstitialClosed(int adsProvider);
    }


    public interface RewardedListener {
        void onRewardedLoaded(int adsProvider);
        void onRewardedFailedToLoad(int adsProvider, int errorCode, String errorMessage);
        void onRewardedOpened(int adsProvider);
        void onRewardedClosed(int adsProvider);
        void onReward(int adsProvider, String type, int amount);
    }


    public interface BannerListener {
        void onBannerLoaded(int adsProvider);
        void onBannerFailedToLoad(int adsProvider, int errorCode, String errorMessage);
        void onBannerShow(int adsProvider);
        void onBannerHide(int adsProvider);
    }

}


