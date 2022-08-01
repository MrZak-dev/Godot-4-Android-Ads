package com.b4dnetwork.godot.android_ads_plugin.applovin;

import android.app.Activity;


import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdInterfaces.RewardedAd;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners;
import com.b4dnetwork.godot.android_ads_plugin.shared.Utils;

import java.util.HashMap;

public class AppLovinRewarded implements RewardedAd {
    private final AdListeners.RewardedListener listener;
    private final Activity activity;

    private final HashMap<String, MaxRewardedAd> rewardedInstances = new HashMap<>();
    private final HashMap<String, Boolean> rewardedLoadStatus = new HashMap<>();


    AppLovinRewarded(Activity activity, AdListeners.RewardedListener listener){
        this.activity = activity;
        this.listener = listener;
    }


    public void load(String adName, String adId){
        MaxRewardedAd rewardedAd = MaxRewardedAd.getInstance(adId, activity);


        rewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onRewardedVideoStarted(MaxAd ad) {

            }

            @Override
            public void onRewardedVideoCompleted(MaxAd ad) {

            }

            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {
                listener.onReward(getProvider(), adName, reward.getLabel(), reward.getAmount());
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                rewardedLoadStatus.put(adName, true);
                rewardedInstances.put(adName, rewardedAd);

                listener.onRewardedLoaded(getProvider(), adName);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                listener.onRewardedOpened(getProvider(), adName);
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                listener.onRewardedClosed(getProvider(), adName);
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                rewardedLoadStatus.put(adName, false);
                rewardedInstances.remove(adName);

                listener.onRewardedFailedToLoad(getProvider(), adName,
                        error.getCode(), error.getMessage());
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });

        rewardedAd.loadAd();

    }


    public void show(String adName){
        if (!Utils.mapHasKey(rewardedInstances, adName)){
            listener.onLogMessage(
                    Utils.LOG_TYPE.ERROR.getValue(),
                    String.format("no AppLovin Rewarded ad with name %s loaded or exist", adName));
            return;
        }

        //noinspection ConstantConditions
        if(rewardedLoadStatus.get(adName)){
            //noinspection ConstantConditions
            if(rewardedInstances.get(adName).isReady()){
                //noinspection ConstantConditions
                rewardedInstances.get(adName).showAd();
            }
        }
    }

    @Override
    public int getProvider() {
        return GodotAndroidAds.ADS_PROVIDER.AppLovin.getValue();
    }
}
