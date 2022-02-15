package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;

import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds;
import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds.GodotSignals;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import org.godotengine.godot.GodotLib;
import org.godotengine.godot.plugin.GodotPlugin;

public class AdmobAds {

    private AdmobInterstitial interstitial;
    private AdmobRewarded rewarded;

    private boolean isInitialized = false;

    private final Activity activity;
    private AdRequest adRequest;
    private GodotAndroidAds pluginInstance;

    public AdmobAds(Activity activity){
        this.activity = activity;
    }

    public void initialize(GodotAndroidAds pluginInstance){
        this.pluginInstance = pluginInstance;
        this.adRequest = new AdRequest.Builder().build();

        MobileAds.initialize(activity, initializationStatus -> {
            this.pluginInstance.emitPluginSignal(GodotSignals.LOG_MESSAGE.getValue(),
                    "Admob Initialized");
            isInitialized = true;
        });

        initializeInterstitial();
        initializeRewarded();
    }


    public void loadInterstitial(String adId){

        if(!this.isInitialized){
            // logger.log()
            return;
        }

        activity.runOnUiThread(() -> interstitial.load(adId, adRequest));
    }


    public void showInterstitial(){
        if(!this.isInitialized){
            // logger.log()
            return;
        }

        activity.runOnUiThread(() -> interstitial.show());
    }


    public void loadRewarded(String adId){

        if(!this.isInitialized){
            // logger.log()
            return;
        }

        activity.runOnUiThread(() -> rewarded.load(adId, adRequest));
    }


    public void showRewarded(){
        if(!this.isInitialized){
            // logger.log()
            return;
        }

        activity.runOnUiThread(() -> rewarded.show());
    }


    private void initializeInterstitial(){
        interstitial = new AdmobInterstitial(this.activity, new AdListeners.InterstitialListener() {
            @Override
            public void onInterstitialLoaded(int adsProvider) {
                pluginInstance.emitPluginSignal(GodotSignals.INTERSTITIAL_LOADED.getValue(), adsProvider);
            }

            @Override
            public void onInterstitialFailedToLoad(int adsProvider, int errorCode, String errorMessage) {
                pluginInstance.emitPluginSignal(GodotSignals.INTERSTITIAL_FAILED_TO_LOAD.getValue(),
                        adsProvider, errorCode, errorMessage);
            }

            @Override
            public void onInterstitialOpened(int adsProvider) {
                pluginInstance.emitPluginSignal(GodotSignals.INTERSTITIAL_OPENED.getValue(), adsProvider);
            }

            @Override
            public void onInterstitialClosed(int adsProvider) {
                pluginInstance.emitPluginSignal(GodotSignals.INTERSTITIAL_CLOSED.getValue(), adsProvider);
            }
        });
    }


    private void initializeRewarded(){
        rewarded = new AdmobRewarded(this.activity, new AdListeners.RewardedListener(){

            @Override
            public void onRewardedLoaded(int adsProvider) {
                pluginInstance.emitPluginSignal(GodotSignals.REWARDED_LOADED.getValue(),
                        adsProvider);
            }

            @Override
            public void onRewardedFailedToLoad(int adsProvider, int errorCode, String errorMessage) {
                pluginInstance.emitPluginSignal(GodotSignals.REWARDED_FAILED_TO_LOAD.getValue(),
                        adsProvider, errorCode, errorMessage);
            }

            @Override
            public void onRewardedOpened(int adsProvider) {
                pluginInstance.emitPluginSignal(GodotSignals.REWARDED_OPENED.getValue(),
                        adsProvider);
            }

            @Override
            public void onRewardedClosed(int adsProvider) {
                pluginInstance.emitPluginSignal(GodotSignals.REWARDED_CLOSED.getValue(),
                        adsProvider);
            }

            @Override
            public void onReward(int adsProvider, String type, int amount) {
                pluginInstance.emitPluginSignal(GodotSignals.REWARD.getValue(), adsProvider,
                        type, amount);
            }
        });
    }

}
