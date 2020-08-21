package com.wolkowycki.predictable.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.wolkowycki.predictable.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsFragment extends Fragment {

    public static final int PAYPAL_REQUEST_CODE = 7171;
    public static final String PAYPAL_CLIENT_ID = "Aat7y99mOjfNzsylLZTnySsQ46eJefV_RaNyDCc0OzT2wmUBg5_TDt0gipE4yBJLAOalJ4VE_p55v-39";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

    String amount = "0.01";
    LinearLayout shareBtn;
    LinearLayout donateBtn;
    EditText amountEdit;

    private ExpandableListView listView;
    private SettingsListAdapter listAdapter;
    private List<String> listHeader;
    private HashMap<String, List<String>> listHash;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        shareBtn = (LinearLayout) root.findViewById(R.id.btn_share);
        donateBtn = (LinearLayout) root.findViewById(R.id.btn_donate);
        // amountEdit = (EditText) root.findViewById(R.id.amount);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String content = "Check predictable app: *link to Google Play here*";
                intent.putExtra(Intent.EXTRA_TEXT, content);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processDonation();
            }
        });

        listView = (ExpandableListView) root.findViewById(R.id.settings_list);
        initData();
        listAdapter = new SettingsListAdapter(getContext(), listHeader, listHash);
        listView.setAdapter(listAdapter);

        return root;
    }

    public void processDonation() {
        // amount = amountEdit.getText().toString();
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
                "Help us growing up", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void initData() {
        listHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listHeader.add("How does it work?");
        listHeader.add("Can I buy coins directly from you?");

        List<String> description = new ArrayList<>();
        description.add("We use artificial intelligence methods called deep neural networks to predict future cryptocurrency prices.");
        listHash.put(listHeader.get(0), description);

        List<String> answer = new ArrayList<>();
        answer.add("No, we don't sell coins.");
        listHash.put(listHeader.get(1), answer);
    }
}
