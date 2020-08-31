package com.wolkowycki.predictable.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wolkowycki.predictable.R;

public class PaymentDetailsActivity extends AppCompatActivity {
    // TextView txtId, txtAmount, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
    }
        /* DETAILS WON'T BE DISPLAYED THIS WAY! JUST SHORT INFO

        txtId = (TextView) findViewById(R.id.txt_id);
        txtAmount = (TextView) findViewById(R.id.txt_amount);
        txtStatus = (TextView) findViewById(R.id.txt_status);

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String amount) {
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText(response.getString(("$" + amount)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    */
}
