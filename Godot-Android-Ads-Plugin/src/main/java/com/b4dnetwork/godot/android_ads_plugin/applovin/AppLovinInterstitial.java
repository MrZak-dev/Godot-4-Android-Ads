package com.b4dnetwork.godot.android_ads_plugin.applovin;

import android.app.Activity;


import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdInterfaces.InterstitialAd;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners;
import com.b4dnetwork.godot.android_ads_plugin.shared.Utils;

import java.util.HashMap;


public class AppLovinInterstitial implements InterstitialAd {
    private final AdListeners.InterstitialListener listener;
    private final Activity activity;

    private final HashMap<String, MaxInterstitialAd> interstitialInstances = new HashMap<>();
    private final HashMap<String, Boolean> interstitialLoadStatus = new HashMap<>();


    AppLovinInterstitial(Activity activity, AdListeners.InterstitialListener listener){
        this.listener = listener;
        this.activity = activity;
    }


    public void load(String adName, String adId){
        MaxInterstitialAd interstitialAd = new MaxInterstitialAd( adId, activity);

        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                interstitialLoadStatus.put(adName, true);
                interstitialInstances.put(adName, interstitialAd);

                listener.onInterstitialLoaded(getProvider(), adName);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                listener.onInterstitialOpened(getProvider(), adName);
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                listener.onInterstitialClosed(getProvider(), adName);
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                interstitialLoadStatus.put(adName, false);
                interstitialInstances.remove(adName);

                listener.onInterstitialFailedToLoad(
                        getProvider(),
                        adName,
                        error.getCode(),
                        error.getMessage());
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });

        interstitialAd.loadAd();

    }


    public void show(String adName){
        if (!Utils.mapHasKey(interstitialInstances, adName)){
            listener.onLogMessage(
                    Utils.LOG_TYPE.ERROR.getValue(),
                    String.format("no AppLovin Interstitial with name %s loaded or exist", adName));
            return;
        }
        //noinspection ConstantConditions

        if(interstitialLoadStatus.get(adName)){
            //noinspection ConstantConditions
            if (interstitialInstances.get(adName).isReady()){
                //noinspection ConstantConditions
                interstitialInstances.get(adName).showAd();
            }
        }
    }

    @Override
    public int getProvider() {
        return GodotAndroidAds.ADS_PROVIDER.AppLovin.getValue();
    }

}
