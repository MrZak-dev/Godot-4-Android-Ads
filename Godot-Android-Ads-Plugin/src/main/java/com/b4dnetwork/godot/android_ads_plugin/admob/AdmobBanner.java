package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds.AdsProvider;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners.BannerListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class AdmobBanner {
    private final Activity activity;
    private final BannerListener listener;
    private final FrameLayout layout;
    private boolean isLoaded = false;
    private boolean isOnTop = false;
    private boolean isVisible = false;

    private AdView adView;

    AdmobBanner(Activity activity, FrameLayout layout, BannerListener listener){
        this.activity = activity;
        this.listener = listener;
        this.layout = layout;
    }

    public void load(String adId, int adSize, boolean isTop, AdRequest adRequest){
        initializeBanner(adId, adSize, isTop, adRequest);
    }


    public void show(){
        if (!isLoaded || isVisible){
            return;
        }

        FrameLayout.LayoutParams frameParam = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        frameParam.gravity = isOnTop ? Gravity.TOP : Gravity.BOTTOM;

        adView.setLayoutParams(frameParam);

        layout.addView(adView);

        isVisible = true;
        listener.onBannerShow(AdsProvider.ADMOB.getValue());
    }


    public void hide(){
        isLoaded = false;
        isVisible = false;
        layout.removeAllViews();
        adView = null;
        listener.onBannerHide(AdsProvider.ADMOB.getValue());
    }


    private void initializeBanner(String adId, int adSize, boolean isTop, AdRequest adRequest){
        isOnTop = isTop;
        adView = new AdView(activity);
        adView.setAdUnitId(adId);
        adView.setAdSize(getAdSize(adSize));
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                isLoaded = false;
                listener.onBannerFailedToLoad(AdsProvider.ADMOB.getValue(), loadAdError.getCode(),
                        loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded() {
                isLoaded = true;
                listener.onBannerLoaded(AdsProvider.ADMOB.getValue());
            }

        });


        adView.loadAd(adRequest);

    }

    private AdSize getAdSize(int adSize){
        switch (adSize){
            case 0:
                return AdSize.BANNER;
            case 1:
                return AdSize.LARGE_BANNER;
            case 2:
                return AdSize.MEDIUM_RECTANGLE;
            case 3:
                return AdSize.FULL_BANNER;
            case 4:
                return AdSize.LEADERBOARD;
            default:
                return AdSize.BANNER;
        }
    }

}
