package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds.ADS_PROVIDER;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners.RewardedListener;
import com.b4dnetwork.godot.android_ads_plugin.shared.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.HashMap;
import java.util.Objects;

public class AdmobRewarded {

    private final RewardedListener listener;
    private final Activity activity;

    private final HashMap<String, RewardedAd> rewardedInstances = new HashMap<>();
    private final HashMap<String, Boolean> rewardedLoadStatus = new HashMap<>();


    AdmobRewarded(Activity activity, RewardedListener listener){
        this.activity = activity;
        this.listener = listener;
    }


    public void load(String adName, String adId, AdRequest adRequest){
        RewardedAd.load(this.activity, adId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                rewardedLoadStatus.put(adName, true);
                rewardedInstances.put(adName, rewardedAd);

                setRewardedCallbacks(adName);
                listener.onRewardedLoaded(ADS_PROVIDER.ADMOB.getValue(), adName);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                rewardedLoadStatus.put(adName, false);
                rewardedInstances.remove(adName);

                listener.onRewardedFailedToLoad(ADS_PROVIDER.ADMOB.getValue(), adName,
                        loadAdError.getCode(), loadAdError.getMessage());
            }
        });
    }


    public void show(String adName){
        if (!Utils.mapHasKey(rewardedInstances, adName)){
            listener.onLogMessage(
                    Utils.LOG_TYPE.ERROR.getValue(),
                    String.format("no Admob Rewarded ad with name %s loaded or exist", adName));
            return;
        }

        //noinspection ConstantConditions
        if(rewardedLoadStatus.get(adName)){
            //noinspection ConstantConditions
            rewardedInstances.get(adName).show(
                    this.activity, rewardItem -> listener.onReward(ADS_PROVIDER.ADMOB.getValue(),
                            adName, rewardItem.getType(), rewardItem.getAmount()));
        }
    }


    private void setRewardedCallbacks(String adName){
        Objects.requireNonNull(rewardedInstances.get(adName)).setFullScreenContentCallback(
                new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                listener.onRewardedOpened(ADS_PROVIDER.ADMOB.getValue(), adName);
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                listener.onRewardedClosed(ADS_PROVIDER.ADMOB.getValue(), adName);
            }
        });
    }
}
