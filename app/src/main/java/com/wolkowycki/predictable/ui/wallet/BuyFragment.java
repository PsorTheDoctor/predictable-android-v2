package com.wolkowycki.predictable.ui.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wolkowycki.predictable.Constants;
import com.wolkowycki.predictable.R;
import com.wolkowycki.predictable.Store;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class BuyFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String KEY = "balance";

    private String currency;
    private float balance;
    private float cost;

    private boolean wheelScrolled = false;
    private EditText currencyEdit;
    private TextView tempCostView;
    private SeekBar seekBar;

    private TextView currencyView;
    private TextView quantityView;
    private TextView costView;
    private TextView balanceView;
    private TextView balanceAfterTxView;

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

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public static BuyFragment newInstance(int index) {
        BuyFragment fragment = new BuyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    // Key is "balance"
    private void saveBalance(Activity root, String key, float balance) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, balance);
        editor.apply();
    }

    // Key is "balance"
    public float loadBalance(Activity root, String key) {
        SharedPreferences prefs = root.getPreferences(Context.MODE_PRIVATE);
        return prefs.getFloat(key, 100.0f);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buy, container, false);

        final Store store = new Store(requireActivity());

        initWheel(root);
        currencyEdit = (EditText) root.findViewById(R.id.currency_edit);

        tempCostView = (TextView) root.findViewById(R.id.temp_cost);

        currencyView = (TextView) root.findViewById(R.id.currency_view);
        quantityView = (TextView) root.findViewById(R.id.quantity_view);
        costView = (TextView) root.findViewById(R.id.cost_view);

        balanceView = (TextView) root.findViewById(R.id.balance);
        setBalance(loadBalance(requireActivity(), KEY));
        String balanceTxt = "Wallet balance: " + getBalance() + " $";
        balanceView.setText(balanceTxt);

        balanceAfterTxView = (TextView) root.findViewById(R.id.balance_after_tx);

        seekBar = (SeekBar) root.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                float tempCost = progress * getBalance() / 100.0f;
                setCost(tempCost);

                String tempCurrency = getCurrency();
                float pricePerDollar = store.loadPrice(requireActivity(), tempCurrency);
                float tempQuantity = tempCost / pricePerDollar;
                String tempCostTxt = tempCost + " $ = " + tempQuantity + " (" + tempCurrency + ")";
                tempCostView.setText(tempCostTxt);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String costTxt = getCost() + " $";
                costView.setText(costTxt);

                float balanceAfterTx = getBalance() - getCost();
                String balanceAfterTxTxt = "Wallet balance after transaction: " + balanceAfterTx + " $";
                balanceAfterTxView.setText(balanceAfterTxTxt);
            }
        });

        currencyView = (TextView) root.findViewById(R.id.currency_view);

        buyBtn = (Button) root.findViewById(R.id.btn_wallet);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //float newBalance = getBalance() - getCost();
                //saveBalance(requireActivity(), KEY, newBalance);
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

        final Store store = new Store(requireActivity());

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

                currencyEdit.setText(tempCurrency);
                currencyView.setText(currencyEdit.getText());

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
}
