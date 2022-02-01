package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners.InterstitialListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdmobInterstitial {


    private final InterstitialListener listener;
    private final Activity activity;


    AdmobInterstitial(Activity activity, InterstitialListener listener){
        this.listener = listener;
        this.activity = activity;
    }

    public void load(String adId, AdRequest adRequest){
        InterstitialAd.load(this.activity, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                listener.onInterstitialLoaded();
                setInterstitialCallbacks(interstitialAd);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                listener.onInterstitialFailedToLoad(loadAdError.getCode(), loadAdError.getMessage());
            }
        });
    }


    private void setInterstitialCallbacks(InterstitialAd interstitial){
        interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                listener.onInterstitialOpened();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                listener.onInterstitialClosed();
            }
        });
    }



}
