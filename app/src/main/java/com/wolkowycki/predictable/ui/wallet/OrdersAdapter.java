package com.wolkowycki.predictable.ui.wallet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    // private MediaPlayer player;

    private OrderItem currentItem;
    private String currency;
    private float amount;
    private float purchasePrice;
    private float currentPrice;
    private float profit;
    private float percentageProfit;

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
        // player = MediaPlayer.create(v.getContext(), R.raw.coin_drop);

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
        profit = Math.round((currentPrice - purchasePrice) * 10000.0f) / 10000.0f;
        percentageProfit = Math.round((profit / purchasePrice) * 10000.0f) / 100.0f;

        String profitTxt = profit + " $";
        String percentageProfitTxt = percentageProfit + " %";
        String purchasePriceTxt = "Purchase price: " + purchasePrice + " $";
        String currentPriceTxt = "Current price: " + currentPrice + " $";

        switch (currency) {
            case "bitcoin": holder.currencyImgView.setImageResource(R.drawable.bitcoin); break;
            case "ethereum": holder.currencyImgView.setImageResource(R.drawable.ethereum); break;
            case "ripple": holder.currencyImgView.setImageResource(R.drawable.ripple); break;
            case "tether": holder.currencyImgView.setImageResource(R.drawable.tether); break;
            case "chainlink": holder.currencyImgView.setImageResource(R.drawable.chainlink); break;
            case "bitcoin-cash": holder.currencyImgView.setImageResource(R.drawable.bitcoin_cash); break;
            case "cardano": holder.currencyImgView.setImageResource(R.drawable.cardano); break;
            case "litecoin": holder.currencyImgView.setImageResource(R.drawable.litecoin); break;
            case "bitcoin-cash-sv": holder.currencyImgView.setImageResource(R.drawable.bitcoin_sv); break;
            case "eos": holder.currencyImgView.setImageResource(R.drawable.eos); break;
            case "binancecoin": holder.currencyImgView.setImageResource(R.drawable.binance_coin); break;
            case "crypto-com-chain": holder.currencyImgView.setImageResource(R.drawable.crypto_coin); break;
            case "tezos": holder.currencyImgView.setImageResource(R.drawable.tezos); break;
            case "stellar": holder.currencyImgView.setImageResource(R.drawable.stellar); break;
            case "tron": holder.currencyImgView.setImageResource(R.drawable.tron); break;
            case "okb": holder.currencyImgView.setImageResource(R.drawable.okb); break;
            case "monero": holder.currencyImgView.setImageResource(R.drawable.monero); break;
            case "cosmos": holder.currencyImgView.setImageResource(R.drawable.cosmos); break;
            case "vechain": holder.currencyImgView.setImageResource(R.drawable.vechain); break;
            case "leo-token": holder.currencyImgView.setImageResource(R.drawable.leo_token); break;
            default: break;
        }

        holder.currencyView.setText(currency);
        holder.amountView.setText(String.valueOf(amount));
        holder.purchasePriceView.setText(purchasePriceTxt);
        holder.currentPriceView.setText(currentPriceTxt);

        if (profit == 0.0f) {
            profitTxt = "";
            percentageProfitTxt = "";
        } else if (profit < 0.0f) {
            holder.profitView.setTextColor(Color.RED);
            holder.percentageProfitView.setTextColor(Color.RED);
        } else {
            profitTxt = "+" + profitTxt;
            percentageProfitTxt = "+" + percentageProfitTxt;
            holder.profitView.setTextColor(Color.parseColor("#009933"));
            holder.percentageProfitView.setTextColor(Color.parseColor("#009933"));
        }
        holder.profitView.setText(profitTxt);
        holder.percentageProfitView.setText(percentageProfitTxt);

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

        private ImageView currencyImgView;
        private TextView currencyView;
        private TextView profitView;
        private TextView amountView;
        private TextView percentageProfitView;
        private TextView purchasePriceView;
        private TextView currentPriceView;
        private Button btnSell;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            this.currencyImgView = itemView.findViewById(R.id.order_currency_img);
            this.currencyView = itemView.findViewById(R.id.order_currency);
            this.profitView = itemView.findViewById(R.id.order_profit);
            this.amountView = itemView.findViewById(R.id.order_amount);
            this.percentageProfitView = itemView.findViewById(R.id.order_percentage_profit);
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
