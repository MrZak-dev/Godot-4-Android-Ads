package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners.InterstitialListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds.AdsProvider;

public class AdmobInterstitial {


    private final InterstitialListener listener;
    private final Activity activity;
    private InterstitialAd interstitialInstance;

    private boolean isLoaded = false;


    AdmobInterstitial(Activity activity, InterstitialListener listener){
        this.listener = listener;
        this.activity = activity;
    }

    public void load(String adId, AdRequest adRequest){
        InterstitialAd.load(this.activity, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                isLoaded = true;
                interstitialInstance = interstitialAd;

                listener.onInterstitialLoaded(AdsProvider.ADMOB.getValue());
                setInterstitialCallbacks(interstitialAd);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                isLoaded = false;
                interstitialInstance = null;

                listener.onInterstitialFailedToLoad(AdsProvider.ADMOB.getValue(),
                        loadAdError.getCode(),
                        loadAdError.getMessage());
            }
        });
    }


    public void show(){
        if(isLoaded){
            interstitialInstance.show(activity);
        }
        // Log info ( interstitial not loaded yet call load to load it)
    }


    private void setInterstitialCallbacks(InterstitialAd interstitial){
        interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                listener.onInterstitialOpened(AdsProvider.ADMOB.getValue());
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                listener.onInterstitialClosed(AdsProvider.ADMOB.getValue());
            }
        });
    }



}
