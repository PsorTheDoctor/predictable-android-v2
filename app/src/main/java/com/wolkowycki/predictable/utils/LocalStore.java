package com.wolkowycki.predictable.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class LocalStore {

    // Key is currencyKey
    // It can be a currency, e.g. "bitcoin" to store fresh prices
    // A concatenated currency + date, e.g. "bitcoin&date=01-07-2020" to store past
    // Or a concatenated currency + nDaysForward, e.g. "bitcoin&nDaysForward=1" to store predicted
    public static void savePrice(Activity root, String currencyKey, float price) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(currencyKey, price);
        editor.apply();
    }

    // Analogical to above
    // Key is currencyKey
    // It can be a currency, e.g. "bitcoin" to store fresh prices
    // A concatenated currency + date, e.g. "bitcoin&date=01-07-2020" to store past
    // Or a concatenated currency + nDaysForward, e.g. "bitcoin&nDaysForward=1" to store predicted
    public static float loadPrice(Activity root, String currencyKey) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        return prefs.getFloat(currencyKey, 0.0f);
    }

    // Key is "balance"
    public static void saveBalance(Activity root, String key, float balance) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, balance);
        editor.apply();
    }

    // Key is "balance"
    public static float loadBalance(Activity root, String key) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        return prefs.getFloat(key, 100.0f);
    }
}
