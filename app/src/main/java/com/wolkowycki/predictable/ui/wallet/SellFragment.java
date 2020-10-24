package com.wolkowycki.predictable.ui.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.wolkowycki.predictable.utils.Constants;
import com.wolkowycki.predictable.R;
import com.wolkowycki.predictable.utils.LocalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView ordersRecycler;
    private OrdersAdapter ordersAdapter;
    private ArrayList<OrderItem> ordersList;
    private RequestQueue queue;

    public static SellFragment newInstance(int index) {
        SellFragment fragment = new SellFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sell, container, false);

        ordersRecycler = root.findViewById(R.id.orders_view);
        ordersRecycler.setHasFixedSize(true);
        ordersRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));

        ordersList = new ArrayList<>();
        queue = Volley.newRequestQueue(root.getContext());
        getOrders();
        return root;
    }

    private void getOrders() {
        String url = Constants.API + "/orders-list/123456";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject order = response.getJSONObject(i);

                                long orderId = order.getLong("id");
                                String currency = order.getString("currency");
                                float amount = (float) order.getDouble("amount");
                                float purchasePrice = (float) order.getDouble("purchase_price");
                                float currentPrice = amount * LocalStore.loadPrice(requireActivity(), currency);

                                ordersList.add(new OrderItem(orderId, currency, amount, purchasePrice, currentPrice));
                            }
                            ordersAdapter = new OrdersAdapter(requireActivity(), getContext(), ordersList);
                            ordersRecycler.setAdapter(ordersAdapter);
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