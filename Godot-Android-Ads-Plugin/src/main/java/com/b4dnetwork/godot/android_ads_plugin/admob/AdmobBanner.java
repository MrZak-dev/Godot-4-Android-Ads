package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds.AdsProvider;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners.BannerListener;
import com.b4dnetwork.godot.android_ads_plugin.shared.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;

import java.util.HashMap;

public class AdmobBanner {
    private final Activity activity;
    private final BannerListener listener;
    private final FrameLayout layout;
    private boolean isVisible = false;

    private String currentVisible = null;
    private final HashMap<String, AdView> bannerInstances = new HashMap<>();
    private final HashMap<String, Boolean> bannerLoadStatus = new HashMap<>();

    AdmobBanner(Activity activity, FrameLayout layout, BannerListener listener){
        this.activity = activity;
        this.listener = listener;
        this.layout = layout;
    }


    public void load(String adName, String adId, int adSize, AdRequest adRequest){
        initializeBanner(adName, adId, adSize, adRequest);
    }


    public void show(String adName, boolean isOnTop){
        boolean isLoaded = bannerLoadStatus.get(adName);

        if (!isLoaded || isVisible){
            return; // TODO : log message (banner not loaded yet)
        }

        FrameLayout.LayoutParams frameParam = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        frameParam.gravity = isOnTop ? Gravity.TOP : Gravity.BOTTOM;

        if (!Utils.isHashHasKy(bannerInstances, adName)){
            // TODO : log message no ad with name available
            return;
        }

        AdView bannerView = bannerInstances.get(adName);
        bannerView.setLayoutParams(frameParam);

        layout.removeAllViews(); // Remove displayed banners first
        layout.addView(bannerView);

        isVisible = true;
        currentVisible = adName;

        listener.onBannerShow(AdsProvider.ADMOB.getValue(), adName);
    }


    public void hide(){
        bannerLoadStatus.put(currentVisible, false);
        bannerInstances.remove(currentVisible);

        isVisible = false;

        layout.removeAllViews();
        listener.onBannerHide(AdsProvider.ADMOB.getValue(), currentVisible);
    }


    private void initializeBanner(String adName, String adId, int adSize, AdRequest adRequest){
        AdView adView = new AdView(activity);
        adView.setAdUnitId(adId);
        adView.setAdSize(getAdSize(adSize));

        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                bannerLoadStatus.put(adName, true);
                listener.onBannerLoaded(AdsProvider.ADMOB.getValue(), adName);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                bannerLoadStatus.put(adName, false);
                bannerInstances.remove(adName);

                listener.onBannerFailedToLoad(AdsProvider.ADMOB.getValue(), adName,
                        loadAdError.getCode(), loadAdError.getMessage());
            }
        });

        bannerInstances.put(adName, adView);
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
