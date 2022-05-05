package com.b4dnetwork.godot.android_ads_plugin.shared;

import java.util.HashMap;

public class Utils {

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean mapHasKey(HashMap<String,?> map, String key){
        return map.containsKey(key);
    }

    public enum LOG_TYPE {
        WARNING(0),
        ERROR(1),
        NOT_FOUND(2),
        INFO(3);


        private final int value;

        LOG_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
