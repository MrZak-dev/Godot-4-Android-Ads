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

    private boolean isInitialized = false;

    private final Activity activity;
    private AdRequest adRequest;
    private GodotAndroidAds pluginInstance;

    public AdmobAds(Activity activity){
        this.activity = activity;
    }

    public void initialize(GodotAndroidAds pluginInstance){

        MobileAds.initialize(activity, initializationStatus -> isInitialized = true);

        this.pluginInstance = pluginInstance;
        this.adRequest = new AdRequest.Builder().build();

        initializeInterstitial();

    }


    public void loadInterstitial(String adId){

        if(this.isInitialized){
            // logger.log()
            return;
        }

        interstitial.load(adId, adRequest);
    }


    public void showInterstitial(){
        if(this.isInitialized){
            // logger.log()
            return;
        }

        interstitial.show();
    }


    private void initializeInterstitial(){
        interstitial = new AdmobInterstitial(this.activity, new AdListeners.InterstitialListener() {
            @Override
            public void onInterstitialLoaded(int adsProvider) {
                pluginInstance.emitSignal(GodotSignals.INTERSTITIAL_LOADED.getValue(), adsProvider);
            }

            @Override
            public void onInterstitialFailedToLoad(int adsProvider, int errorCode, String errorMessage) {
                pluginInstance.emitSignal(GodotSignals.INTERSTITIAL_FAILED_TO_LOAD.getValue(),
                        adsProvider, errorCode, errorMessage);
            }

            @Override
            public void onInterstitialOpened(int adsProvider) {
                pluginInstance.emitSignal(GodotSignals.INTERSTITIAL_OPENED.getValue(), adsProvider);
            }

            @Override
            public void onInterstitialClosed(int adsProvider) {
                pluginInstance.emitSignal(GodotSignals.INTERSTITIAL_CLOSED.getValue(), adsProvider);
            }
        });
    }

}
