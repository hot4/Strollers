package com.example.strollers.strollers.Helpers;

import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    public static SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final Double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public static Double getDouble(final SharedPreferences prefs, final String key) {
        return Double.longBitsToDouble(prefs.getLong(key, 0));
    }
}
