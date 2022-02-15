package com.b4dnetwork.godot.android_ads_plugin.admob;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.b4dnetwork.godot.android_ads_plugin.GodotAndroidAds.AdsProvider;
import com.b4dnetwork.godot.android_ads_plugin.shared.AdListeners.RewardedListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdmobRewarded {

    private final RewardedListener listener;
    private final Activity activity;
    private RewardedAd rewardedInstance;

    private boolean isLoaded = false;

    AdmobRewarded(Activity activity, RewardedListener listener){
        this.activity = activity;
        this.listener = listener;
    }

    public void load(String adId, AdRequest adRequest){
        RewardedAd.load(this.activity, adId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                rewardedInstance = rewardedAd;
                isLoaded = true;

                listener.onRewardedLoaded(AdsProvider.ADMOB.getValue());
                setRewardedCallbacks();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                rewardedInstance = null;
                isLoaded = false;

                listener.onRewardedFailedToLoad(AdsProvider.ADMOB.getValue(),
                        loadAdError.getCode(), loadAdError.getMessage());
            }
        });
    }

    public void show(){
        if(isLoaded){
            rewardedInstance.show(this.activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    listener.onReward(AdsProvider.ADMOB.getValue(),
                            rewardItem.getType(), rewardItem.getAmount());
                }
            });
        }
    }


    private void setRewardedCallbacks(){
        rewardedInstance.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                listener.onRewardedOpened(AdsProvider.ADMOB.getValue());
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                listener.onRewardedClosed(AdsProvider.ADMOB.getValue());
            }
        });
    }
}
