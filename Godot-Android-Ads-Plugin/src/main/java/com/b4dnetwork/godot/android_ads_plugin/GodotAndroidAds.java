package com.b4dnetwork.godot.android_ads_plugin;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.b4dnetwork.godot.android_ads_plugin.admob.AdmobAds;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.Set;

public class GodotAndroidAds extends GodotPlugin {

    private final Activity activity;

    private AdmobAds admobInstance = null;

    public GodotAndroidAds(Godot godot) {
        super(godot);
        this.activity = getActivity();
    }


    @UsedByGodot
    public void initializeAdmob(){
        admobInstance = new AdmobAds(this.activity);
        admobInstance.initialize(this);
    }


    @UsedByGodot
    public void loadAdmobInterstitial(String adId){
        if (admobInstance == null) {
            emitSignal(GodotSignals.LOG_MESSAGE.getValue(), "Admob is not initialized");
            return;
        }

        admobInstance.loadInterstitial(adId);
    }

    @UsedByGodot
    public void showAdmobInterstitial(){
        if (admobInstance == null) {
            emitSignal(GodotSignals.LOG_MESSAGE.getValue(), "Admob is not initialized");
            return;
        }
        admobInstance.showInterstitial();
    }


    @UsedByGodot
    public void loadAdmobRewarded(String adId){
        if(admobInstance == null){
            emitSignal(GodotSignals.LOG_MESSAGE.getValue(), "Admob is not initialized");
            return;
        }

        admobInstance.loadRewarded(adId);
    }


    @UsedByGodot
    public void showAdmobRewarded(){
        if(admobInstance == null){
            emitSignal(GodotSignals.LOG_MESSAGE.getValue(), "Admob is not initialized");
            return;
        }

        admobInstance.showRewarded();
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
        signals.add(new SignalInfo(GodotSignals.INTERSTITIAL_LOADED.getValue(), Integer.class));
        signals.add(new SignalInfo(GodotSignals.INTERSTITIAL_FAILED_TO_LOAD.getValue(),
                Integer.class, Integer.class, String.class));
        signals.add(new SignalInfo(GodotSignals.INTERSTITIAL_OPENED.getValue(), Integer.class));
        signals.add(new SignalInfo(GodotSignals.INTERSTITIAL_CLOSED.getValue(), Integer.class));

        // Rewarded signals
        signals.add(new SignalInfo(GodotSignals.REWARDED_LOADED.getValue(), Integer.class));
        signals.add(new SignalInfo(GodotSignals.REWARDED_FAILED_TO_LOAD.getValue(), Integer.class,
                Integer.class, String.class));
        signals.add(new SignalInfo(GodotSignals.REWARDED_OPENED.getValue(), Integer.class));
        signals.add(new SignalInfo(GodotSignals.REWARDED_CLOSED.getValue(), Integer.class));
        signals.add(new SignalInfo(GodotSignals.REWARD.getValue(), Integer.class, String.class,
                Integer.class));

        // General signals
        signals.add(new SignalInfo(GodotSignals.LOG_MESSAGE.getValue(), String.class));

        return signals;
    }


    public void emitPluginSignal(String signalName , Object... signalArgs){
        emitSignal(signalName, signalArgs);
    }


    public enum AdsProvider {
        ADMOB(0),
        UNITY(1),
        AppLovin(2);

        private final int value;

        AdsProvider(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum GodotSignals {
        INTERSTITIAL_LOADED("_on_interstitial_loaded"),
        INTERSTITIAL_FAILED_TO_LOAD("_on_interstitial_failed_to_load"),
        INTERSTITIAL_OPENED("_on_interstitial_opened"),
        INTERSTITIAL_CLOSED("_on_interstitial_closed"),

        REWARDED_LOADED("_on_rewarded_loaded"),
        REWARDED_FAILED_TO_LOAD("_on_rewarded_failed_to_load"),
        REWARDED_OPENED("_on_rewarded_opened"),
        REWARDED_CLOSED("_on_rewarded_closed"),
        REWARD("_on_reward"),

        LOG_MESSAGE("_on_log_message");


        private final String value;

        GodotSignals(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
