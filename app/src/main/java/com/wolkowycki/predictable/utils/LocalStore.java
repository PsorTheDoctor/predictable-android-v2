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
        editor.putFloat(currencyKey, price).apply();
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
        editor.putFloat(key, balance).apply();
    }

    // Key is "balance"
    public static float loadBalance(Activity root, String key) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        return prefs.getFloat(key, 100.0f);
    }

    // Key is "news&idx=...&value=..." where
    // idx is an index of an entry and value is name of a field
    // For example: "news&idx=0&value=header"
    public static void saveNews(Activity root, String key, String value) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value).apply();
    }

    // Analogical to above
    public static String loadNews(Activity root, String key) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        return prefs.getString(key, "Sth went wrong. Refresh an app and try again");
    }

    public static boolean containsNews(Activity root, String key) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        return prefs.contains(key);
    }

    public static void removeNews(Activity root, String key) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key).apply();
    }

    // Key is "article&idx=..." where
    // idx is an index of an entry
    // For example: "article&idx=0"
    public static void saveArticle(Activity root, String key, String value) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value).apply();
    }

    // Analogical to above
    public static String loadArticle(Activity root, String key) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        return prefs.getString(key, "Sth went wrong. Refresh an app and try again");
    }
}
