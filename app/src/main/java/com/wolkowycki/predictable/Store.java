package com.wolkowycki.predictable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Store {
    private static final String CG = "https://api.coingecko.com/api/v3";
    private static final String API = "https://predictable-api.herokuapp.com";
    private RequestQueue queue;

    public Store(Activity root) {
        queue = Volley.newRequestQueue(root);
    }

    // Key is currencyKey
    // It can be a currency, e.g. "bitcoin" to store fresh prices
    // A concatenated currency + date, e.g. "bitcoin&date=01-07-2020" to store past
    // Or a concatenated currency + nDaysForward, e.g. "bitcoin&nDaysForward=1" to store predicted
    private void savePrice(Activity root, String currencyKey, float price) {
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
    public float loadPrice(Activity root, String currencyKey) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        return prefs.getFloat(currencyKey, 0.0f);
    }

    public void fetchFreshPrice(final Activity root,
                              final String currency,
                              final String vsCurrency) {

        String url = CG + "/simple/price?ids=" + currency + "&vs_currencies=" + vsCurrency;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(currency);
                            float price = (float) jsonObject.getDouble(vsCurrency);
                            savePrice(root, currency, price);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public void fetchPastPrice(final Activity root,
                             final String currency,
                             final String vsCurrency,
                             final String date) {

        String url = CG + "/coins/" + currency + "/history?date=" + date;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject marketData = response.getJSONObject("market_data");
                            JSONObject currentPrice = marketData.getJSONObject("current_price");
                            float price = (float) currentPrice.getDouble(vsCurrency);
                            savePrice(root, currency + "&date=" + date, price);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public void fetchFuturePrice(final Activity root,
                                 final String currency,
                                 final int nDaysForward) {

        String url = API + "/future-prices/" + currency + "&" + nDaysForward;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            float price = (float) response.getDouble("value");
                            savePrice(root, currency + "&nDaysForward=" + nDaysForward, price);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
