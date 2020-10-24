package com.wolkowycki.predictable.ui.wallet;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wolkowycki.predictable.utils.Constants;
import com.wolkowycki.predictable.R;
import com.wolkowycki.predictable.utils.LocalStore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private final static int DELAY = 100;

    private Activity root;
    private Context context;
    private ArrayList<OrderItem> ordersList;
    private RequestQueue queue;
    private MediaPlayer player;

    private OrderItem currentItem;
    private String currency;
    private float amount;
    private float purchasePrice;
    private float currentPrice;

    public OrdersAdapter(Activity root, Context context, ArrayList<OrderItem> ordersList) {
        this.root = root;
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);

        queue = Volley.newRequestQueue(v.getContext());
        player = MediaPlayer.create(v.getContext(), R.raw.coin_drop);

        return new OrdersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, final int position) {
        currentItem = ordersList.get(position);

        // final long orderId = currentItem.getOrderId();
        currency = currentItem.getCurrency();
        amount = currentItem.getAmount();
        purchasePrice = currentItem.getPurchasePrice();
        currentPrice = currentItem.getCurrentPrice();

        String purchasePriceTxt = "Purchase price: " + purchasePrice + " $";
        String currentPriceTxt = "Current price: " + currentPrice + " $";

        holder.currencyView.setText(currency);
        holder.amountView.setText(String.valueOf(amount));
        holder.purchasePriceView.setText(purchasePriceTxt);
        holder.currentPriceView.setText(currentPriceTxt);

        holder.btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrder(root, currentItem);
                // player.start();

                // invoke refresh after delay to update order list
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                }, DELAY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView currencyView;
        public TextView amountView;
        public TextView purchasePriceView;
        public TextView currentPriceView;
        public Button btnSell;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            this.currencyView = itemView.findViewById(R.id.order_currency);
            this.amountView = itemView.findViewById(R.id.order_amount);
            this.purchasePriceView = itemView.findViewById(R.id.order_purchase_price);
            this.currentPriceView = itemView.findViewById(R.id.order_current_price);
            this.btnSell = itemView.findViewById(R.id.btn_sell);
        }
    }

    private void deleteOrder(final Activity root, final OrderItem item) {
        String url = Constants.API + "/orders/" + item.getOrderId();

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        float balance = LocalStore.loadBalance(root, "balance");
                        float newBalance = balance + item.getCurrentPrice();
                        LocalStore.saveBalance(root, "balance", newBalance);
                        ordersList.remove(item);
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
