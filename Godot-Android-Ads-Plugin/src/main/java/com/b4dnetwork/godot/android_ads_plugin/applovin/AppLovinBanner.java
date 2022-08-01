package com.b4dnetwork.godot.android_ads_plugin.applovin;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdInterfaces.BannerAd;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners;
import com.b4dnetwork.godot.android_ads_plugin.shared.Utils;


import java.util.HashMap;

public class AppLovinBanner implements BannerAd {

    private final Activity activity;
    private final AdListeners.BannerListener listener;
    private final FrameLayout layout;
    private boolean isVisible = false;

    private String currentVisible = null;
    private final HashMap<String, MaxAdView> bannerInstances = new HashMap<>();
    private final HashMap<String, Boolean> bannerLoadStatus = new HashMap<>();

    AppLovinBanner(Activity activity, FrameLayout layout, AdListeners.BannerListener listener){
        this.activity = activity;
        this.listener = listener;
        this.layout = layout;
    }


    public void load(String adName, String adId){
        initializeBanner(adName, adId);
    }


    public void show(String adName, boolean isOnTop){
        if (!Utils.mapHasKey(bannerLoadStatus, adName)){
            return;
        }

        @SuppressWarnings("ConstantConditions") boolean isLoaded = bannerLoadStatus.get(adName);

        if (!isLoaded || isVisible){
            listener.onLogMessage(
                    Utils.LOG_TYPE.ERROR.getValue(),
                    String.format("AppLovin Banner %s is not loaded or already displayed", adName));
            return;
        }

        int pixels = (int) (50 * activity.getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams frameParam = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                pixels
        );

        frameParam.gravity = isOnTop ? Gravity.TOP : Gravity.BOTTOM;

        if (!Utils.mapHasKey(bannerInstances, adName)){
            listener.onLogMessage(
                    Utils.LOG_TYPE.ERROR.getValue(),
                    String.format("no AppLovin Banner with name %s loaded or exist", adName));
            return;
        }

        MaxAdView bannerView = bannerInstances.get(adName);

        //noinspection ConstantConditions
        bannerView.setLayoutParams(frameParam);

        layout.removeAllViews(); // Remove displayed banners first
        layout.addView(bannerView);

        isVisible = true;
        currentVisible = adName;

        listener.onBannerShow(getProvider(), adName);
    }


    public void hide(){
        bannerLoadStatus.put(currentVisible, false);
        bannerInstances.remove(currentVisible);

        isVisible = false;

        layout.removeAllViews();
        listener.onBannerHide(getProvider(), currentVisible);
    }


    private void initializeBanner(String adName, String adId){
        MaxAdView adView = new MaxAdView( adId, activity);

        adView.setBackgroundColor(Color.WHITE);
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                bannerLoadStatus.put(adName, true);
                listener.onBannerLoaded(getProvider(), adName);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                adView.setExtraParameter( "allow_pause_auto_refresh_immediately", "true" );
                adView.stopAutoRefresh();

                bannerLoadStatus.put(adName, false);
                bannerInstances.remove(adName);

                listener.onBannerFailedToLoad(getProvider(), adName,
                        error.getCode(), error.getMessage());
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });

        bannerInstances.put(adName, adView);
        adView.loadAd();

    }



    @Override
    public int getProvider() {
        return GodotAndroidAds.ADS_PROVIDER.AppLovin.getValue();
    }
}
