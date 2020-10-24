package com.wolkowycki.predictable.utils;

import android.app.Activity;

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
    private RequestQueue queue;

    public Store(Activity root) {
        queue = Volley.newRequestQueue(root);
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
                            LocalStore.savePrice(root, currency, price);
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
                            LocalStore.savePrice(root, currency + "&date=" + date, price);
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

        String url = Constants.API + "/future-prices/" + currency + "&" + nDaysForward;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            float price = (float) response.getDouble("value");
                            LocalStore.savePrice(root, currency + "&nDaysForward=" + nDaysForward, price);
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
