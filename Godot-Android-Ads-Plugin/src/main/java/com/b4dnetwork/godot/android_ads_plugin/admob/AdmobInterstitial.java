package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners.InterstitialListener;
import com.b4dnetwork.godot.android_ads_plugin.shared.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds.AdsProvider;

import java.util.HashMap;
import java.util.Objects;

public class AdmobInterstitial {


    private final InterstitialListener listener;
    private final Activity activity;

    private final HashMap<String,InterstitialAd> interstitialInstances = new HashMap<>();
    private final HashMap<String, Boolean> interstitialLoadStatus = new HashMap<>();


    AdmobInterstitial(Activity activity, InterstitialListener listener){
        this.listener = listener;
        this.activity = activity;
    }

    public void load(String adName, String adId, AdRequest adRequest){
        InterstitialAd.load(this.activity, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                interstitialLoadStatus.put(adName, true);
                interstitialInstances.put(adName, interstitialAd);
                setInterstitialCallbacks(adName);

                listener.onInterstitialLoaded(AdsProvider.ADMOB.getValue(), adName);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                interstitialLoadStatus.put(adName, false);
                interstitialInstances.remove(adName);

                listener.onInterstitialFailedToLoad(
                        AdsProvider.ADMOB.getValue(),
                        adName,
                        loadAdError.getCode(),
                        loadAdError.getMessage());
            }
        });
    }


    public void show(String adName){
        if (!Utils.mapHasKey(interstitialInstances, adName)){
            // TODO : log message no ad with name available
            return;
        }
        if(interstitialLoadStatus.get(adName)){
            interstitialInstances.get(adName).show(activity);
        }
        // Log info ( interstitial not loaded yet call load to load it)
    }


    private void setInterstitialCallbacks(String adName){
        Objects.requireNonNull(interstitialInstances.get(adName))
                .setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    listener.onInterstitialOpened(AdsProvider.ADMOB.getValue(), adName);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    listener.onInterstitialClosed(AdsProvider.ADMOB.getValue(), adName);
                }
        });
    }



}
