package com.wolkowycki.predictable.ui.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wolkowycki.predictable.utils.Constants;
import com.wolkowycki.predictable.utils.LocalStore;
import com.wolkowycki.predictable.R;
import com.wolkowycki.predictable.utils.Store;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class BuyFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String KEY = "balance";

    private RequestQueue queue;

    private String currency = Constants.CURRENCIES[0];
    private float balance;
    private float balanceAfterTx;
    private float quantity = 0.0f;
    private float cost = 0.0f;
    private String userId = "123456";

    private boolean wheelScrolled = false;
    // private EditText currencyEdit;
    private TextView tempCostView;
    private SeekBar seekBar;

//    private TextView currencyView;
//    private TextView quantityView;
//    private TextView costView;
//    private TextView balanceView;
//    private TextView balanceAfterTxView;

    private Button buyBtn;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getBalanceAfterTx() {
        return balanceAfterTx;
    }

    public void setBalanceAfterTx(float balanceAfterTx) {
        this.balanceAfterTx = balanceAfterTx;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public static BuyFragment newInstance(int index) {
        BuyFragment fragment = new BuyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buy, container, false);

        final Store store = new Store(requireActivity());
        queue = Volley.newRequestQueue(requireActivity());

        userId = "123456"; // UUID.randomUUID().toString();

        initWheel(root);
        // currencyEdit = (EditText) root.findViewById(R.id.currency_edit);

        tempCostView = (TextView) root.findViewById(R.id.temp_cost);

//        currencyView = (TextView) root.findViewById(R.id.currency_view);
//        quantityView = (TextView) root.findViewById(R.id.quantity_view);
//        costView = (TextView) root.findViewById(R.id.cost_view);

//        balanceView = (TextView) root.findViewById(R.id.balance);
        setBalance(LocalStore.loadBalance(requireActivity(), KEY));
        String balanceTxt = "Wallet balance: " + getBalance() + " $";
//        balanceView.setText(balanceTxt);
//
//        balanceAfterTxView = (TextView) root.findViewById(R.id.balance_after_tx);

        seekBar = (SeekBar) root.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                float tempCost = progress * getBalance() / 100.0f;
                setCost(tempCost);

                String tempCurrency = getCurrency();
                float pricePerDollar = LocalStore.loadPrice(requireActivity(), tempCurrency);
                float tempQuantity = tempCost / pricePerDollar;
                setQuantity(tempQuantity);

                String tempCostTxt = tempCost + " $ = " + tempQuantity + " (" + tempCurrency + ")";
                tempCostView.setText(tempCostTxt);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String costTxt = getCost() + " $";
                // costView.setText(costTxt);

                float newBalance = getBalance() - getCost();
                setBalanceAfterTx(newBalance);
                String balanceAfterTxTxt = "Wallet balance after transaction: " + balanceAfterTx + " $";
                // balanceAfterTxView.setText(balanceAfterTxTxt);
            }
        });

        buyBtn = (Button) root.findViewById(R.id.btn_wallet);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // float newBalance = getBalance() - getCost();
                // saveBalance(requireActivity(), KEY, newBalance);
                if (currency != null && quantity != 0.0f) {
                    postOrder();
                } else {
                    BuyErrorDialog dialog = new BuyErrorDialog();
                    dialog.show(requireActivity().getSupportFragmentManager(), "");
                }
            }
        });
        return root;
    }

//    private void updateStatus() {
//        String tempCurrency = Constants.CURRENCY_NAMES[(
//                (WheelView) requireActivity().findViewById(R.id.wheel)).getCurrentItem()];
//        currencyEdit.setText(tempCurrency);
//        currencyView.setText(currencyEdit.getText());
//
//        // fetch fresh price by every wheel spin
//        store.fetchFreshPrice(requireActivity(), tempCurrency, "usd");
//
//        quantityView.setText(valueOf(store.loadPrice(requireActivity(), tempCurrency)));
//    }

    private void initWheel(View root) {
        WheelView wheel = (WheelView) root.findViewById(R.id.wheel);
        wheel.setViewAdapter(new ArrayWheelAdapter<String>(root.getContext(), Constants.CURRENCY_NAMES));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(0);
        wheel.setCyclic(false);
//        wheel.addChangingListener(changedListener);
//        wheel.addScrollingListener(scrolledListener);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());

        wheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                wheelScrolled = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                wheelScrolled = false;
                String tempCurrency = Constants.CURRENCIES[(
                        (WheelView) requireActivity().findViewById(R.id.wheel)).getCurrentItem()];
                setCurrency(tempCurrency);

                // currencyEdit.setText(tempCurrency);
                // currencyView.setText(currencyEdit.getText());

                // fetch fresh price by every wheel spin
                // store.fetchFreshPrice(requireActivity(), tempCurrency, "usd");

                // quantityView.setText(valueOf(store.loadPrice(requireActivity(), tempCurrency)));
            }
        });

        wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // if (!wheelScrolled) updateStatus();
            }
        });
    }

    private void postOrder() {
        String url = Constants.API + "/orders-list/" + userId;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LocalStore.saveBalance(requireActivity(), "balance", getBalanceAfterTx());

                        BuySuccessDialog dialog = new BuySuccessDialog();
                        dialog.show(requireActivity().getSupportFragmentManager(), "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BuyErrorDialog dialog = new BuyErrorDialog();
                dialog.show(requireActivity().getSupportFragmentManager(), "");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("currency", currency);
                params.put("amount", Float.toString(quantity));
                params.put("purchase_price", Float.toString(cost));
                params.put("owner_id", userId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(request);
    }
}
