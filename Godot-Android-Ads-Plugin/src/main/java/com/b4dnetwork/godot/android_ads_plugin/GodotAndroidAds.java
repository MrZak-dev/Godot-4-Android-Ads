package com.b4dnetwork.godot.android_ads_plugin;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.b4dnetwork.godot.android_ads_plugin.admob.AdmobAds;
import com.b4dnetwork.godot.android_ads_plugin.shared.Utils;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.Set;

public class GodotAndroidAds extends GodotPlugin {

    private final Activity activity;
    private FrameLayout layout = null;

    private AdmobAds admobInstance = null;

    public GodotAndroidAds(Godot godot) {
        super(godot);
        this.activity = getActivity();
    }

    @Override
    public View onMainCreate(Activity activity) {
        layout = new FrameLayout(activity);
        return layout;
    }


    public FrameLayout getLayout(){
        return layout;
    }


    @UsedByGodot
    public void initializeAdmob(boolean gdprEnabled, boolean ccpaEnabled){
        admobInstance = new AdmobAds(this.activity);
        admobInstance.initialize(this, gdprEnabled, ccpaEnabled);
    }


    @UsedByGodot
    public void loadAdmobInterstitial(String adName, String adId){
        if (admobInstance == null) {
            emitSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }

        admobInstance.loadInterstitial(adName, adId);
    }

    @UsedByGodot
    public void showAdmobInterstitial(String adName){
        if (admobInstance == null) {
            emitSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }
        admobInstance.showInterstitial(adName);
    }


    @UsedByGodot
    public void loadAdmobRewarded(String adName, String adId){
        if(admobInstance == null){
            emitSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }

        admobInstance.loadRewarded(adName, adId);
    }


    @UsedByGodot
    public void showAdmobRewarded(String adName){
        if(admobInstance == null){
            emitSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }

        admobInstance.showRewarded(adName);
    }


    @UsedByGodot
    public void loadAdmobBanner(String adName, String adId, int adSize){
        if(admobInstance == null){
            emitSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }
        admobInstance.loadBanner(adName, adId, adSize);
    }


    @UsedByGodot
    public void showAdmobBanner(String adName, boolean isOnTop){
        if(admobInstance == null){
            emitSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }
        admobInstance.showBanner(adName, isOnTop);
    }


    @UsedByGodot
    public void hideAdmobBanner() {
        if(admobInstance == null){
            emitSignal(GODOT_SIGNAL.LOG_MESSAGE.getValue(),
                    Utils.LOG_TYPE.ERROR.getValue(), "Admob is not initialized");
            return;
        }
        admobInstance.hideBanner();
    }


    @NonNull
    @Override
    public String getPluginName() {
        return "GodotAndroidAds";
    }


    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = new ArraySet<>();

        // Interstitial signals
        signals.add(new SignalInfo(GODOT_SIGNAL.INTERSTITIAL_LOADED.getValue(),
                Integer.class, String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.INTERSTITIAL_FAILED_TO_LOAD.getValue(),
                Integer.class, String.class, Integer.class, String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.INTERSTITIAL_OPENED.getValue(), Integer.class,
                String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.INTERSTITIAL_CLOSED.getValue(), Integer.class,
                String.class));

        // Rewarded signals
        signals.add(new SignalInfo(GODOT_SIGNAL.REWARDED_LOADED.getValue(), Integer.class,
                String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.REWARDED_FAILED_TO_LOAD.getValue(), Integer.class,
                String.class, Integer.class, String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.REWARDED_OPENED.getValue(), Integer.class,
                String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.REWARDED_CLOSED.getValue(), Integer.class,
                String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.REWARD.getValue(), Integer.class,String.class,
                String.class,
                Integer.class));

        //Banner signals
        signals.add(new SignalInfo(GODOT_SIGNAL.BANNER_LOADED.getValue(), Integer.class,
                String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.BANNER_FAILED_TO_LOAD.getValue(), Integer.class,
                String.class, Integer.class, String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.BANNER_SHOW.getValue(), Integer.class,
                String.class));
        signals.add(new SignalInfo(GODOT_SIGNAL.BANNER_HIDE.getValue(), Integer.class,
                String.class));

        // General signals
        signals.add(new SignalInfo(GODOT_SIGNAL.LOG_MESSAGE.getValue(), Integer.class, String.class));

        return signals;
    }


    public void emitPluginSignal(String signalName , Object... signalArgs){
        emitSignal(signalName, signalArgs);
    }


    public enum ADS_PROVIDER {
        ADMOB(0),
        UNITY(1),
        AppLovin(2);

        private final int value;

        ADS_PROVIDER(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum GODOT_SIGNAL {
        INTERSTITIAL_LOADED("_on_interstitial_loaded"),
        INTERSTITIAL_FAILED_TO_LOAD("_on_interstitial_failed_to_load"),
        INTERSTITIAL_OPENED("_on_interstitial_opened"),
        INTERSTITIAL_CLOSED("_on_interstitial_closed"),

        REWARDED_LOADED("_on_rewarded_loaded"),
        REWARDED_FAILED_TO_LOAD("_on_rewarded_failed_to_load"),
        REWARDED_OPENED("_on_rewarded_opened"),
        REWARDED_CLOSED("_on_rewarded_closed"),
        REWARD("_on_reward"),

        BANNER_LOADED("_on_banner_loaded"),
        BANNER_FAILED_TO_LOAD("_on_banner_failed_to_load"),
        BANNER_SHOW("_on_banner_show"),
        BANNER_HIDE("_on_banner_hide"),


        LOG_MESSAGE("_on_log_message");


        private final String value;

        GODOT_SIGNAL(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
