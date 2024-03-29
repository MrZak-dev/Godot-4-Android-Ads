package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;
import android.os.Bundle;

import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds;
import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds.GODOT_SIGNAL;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners;
import com.b4dnetwork.godot.android_ads_plugin.shared.Utils;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;


public class Admob {

    private AdmobInterstitial interstitial;
    private AdmobRewarded rewarded;
    private AdmobBanner banner;

    private boolean isInitialized = false;

    private final Activity activity;
    private AdRequest adRequest;
    private GodotAndroidAds pluginInstance;

    public Admob(Activity activity){
        this.activity = activity;
    }

    public void initialize(GodotAndroidAds pluginInstance, boolean gdprEnabled, boolean ccpaEnabled){
        this.pluginInstance = pluginInstance;

        Bundle networkExtrasBundle = new Bundle();

        if (ccpaEnabled){
            networkExtrasBundle.putInt("rdp", 1); // Enable California Consumer Privacy Act (CCPA)
        }
        if (gdprEnabled){
            networkExtrasBundle.putString("npa", "1"); // Enable General Data Protection Regulation
        }

        this.adRequest = new AdRequest.Builder().addNetworkExtrasBundle(
                AdMobAdapter.class, networkExtrasBundle).build();

        MobileAds.initialize(activity, initializationStatus -> {
            this.pluginInstance.emitPluginSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.INFO.getValue(), "Admob Initialized");
            isInitialized = true;
        });

        initializeInterstitial();
        initializeRewarded();
        initializeBanner();
    }


    public void loadInterstitial(String adName, String adId){

        if(!this.isInitialized){
            pluginInstance.emitPluginSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }

        activity.runOnUiThread(() -> interstitial.load(adName, adId, adRequest));
    }


    public void showInterstitial(String adName){
        if(!this.isInitialized){
            pluginInstance.emitPluginSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }

        activity.runOnUiThread(() -> interstitial.show(adName));
    }


    public void loadRewarded(String adName, String adId){

        if(!this.isInitialized){
            pluginInstance.emitPluginSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }

        activity.runOnUiThread(() -> rewarded.load(adName, adId, adRequest));
    }


    public void showRewarded(String adName){
        if(!this.isInitialized){
            pluginInstance.emitPluginSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }

        activity.runOnUiThread(() -> rewarded.show(adName));
    }


    public void loadBanner(String adName, String adId, int adSize){
        activity.runOnUiThread(() -> banner.load(adName, adId, adSize, adRequest));
    }


    public void showBanner(String adName, boolean isOnTop){
        activity.runOnUiThread(() -> banner.show(adName, isOnTop));
    }


    public void hideBanner(){
        activity.runOnUiThread(() -> banner.hide());
    }


    private void initializeInterstitial(){
        interstitial = new AdmobInterstitial(this.activity, new AdListeners.InterstitialListener() {
            @Override
            public void onLogMessage(int logType, String message) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(), logType, message);
            }

            @Override
            public void onInterstitialLoaded(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(
                        GODOT_SIGNAL.INTERSTITIAL_LOADED.getValue(), adsProvider, adName);
            }

            @Override
            public void onInterstitialFailedToLoad(
                    int adsProvider, String adName,int errorCode, String errorMessage) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.INTERSTITIAL_FAILED_TO_LOAD.getValue(),
                        adsProvider, errorCode, errorMessage);
            }

            @Override
            public void onInterstitialOpened(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(
                        GODOT_SIGNAL.INTERSTITIAL_OPENED.getValue(), adsProvider, adName);
            }

            @Override
            public void onInterstitialClosed(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(
                        GODOT_SIGNAL.INTERSTITIAL_CLOSED.getValue(), adsProvider, adName);
            }
        });
    }


    private void initializeRewarded(){
        rewarded = new AdmobRewarded(this.activity, new AdListeners.RewardedListener(){
            @Override
            public void onLogMessage(int logType, String message) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(), logType, message);
            }

            @Override
            public void onRewardedLoaded(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.REWARDED_LOADED.getValue(),
                        adsProvider, adName);
            }

            @Override
            public void onRewardedFailedToLoad(int adsProvider, String adName, int errorCode, String errorMessage) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.REWARDED_FAILED_TO_LOAD.getValue(),
                        adsProvider, adName,errorCode, errorMessage);
            }

            @Override
            public void onRewardedOpened(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.REWARDED_OPENED.getValue(),
                        adsProvider, adName);
            }

            @Override
            public void onRewardedClosed(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.REWARDED_CLOSED.getValue(),
                        adsProvider, adName);
            }

            @Override
            public void onReward(int adsProvider, String adName, String type, int amount) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.REWARD.getValue(), adsProvider,
                        adName, type, amount);
            }
        });
    }


    private void initializeBanner(){
        banner = new AdmobBanner(activity, pluginInstance.getLayout(), new AdListeners.BannerListener() {
            @Override
            public void onLogMessage(int logType, String message) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(), logType, message);
            }

            @Override
            public void onBannerLoaded(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.BANNER_LOADED.getValue(), adsProvider, adName);
            }

            @Override
            public void onBannerFailedToLoad(int adsProvider, String adName, int errorCode, String errorMessage) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.BANNER_FAILED_TO_LOAD.getValue(),
                        adsProvider, adName, errorCode, errorMessage);
            }

            @Override
            public void onBannerShow(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.BANNER_SHOW.getValue(), adsProvider,
                        adName);
            }

            @Override
            public void onBannerHide(int adsProvider, String adName) {
                pluginInstance.emitPluginSignal(GODOT_SIGNAL.BANNER_HIDE.getValue(), adsProvider,
                        adName);
            }
        });
    }

}
