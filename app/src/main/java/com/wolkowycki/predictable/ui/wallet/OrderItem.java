package com.wolkowycki.predictable.ui.wallet;

public class OrderItem {

    private long orderId;
    private String currency;
    private float amount;
    private float purchasePrice;
    private float currentPrice;

    public OrderItem(long orderId,
                     String currency,
                     float amount,
                     float purchasePrice,
                     float currentPrice) {
        this.orderId = orderId;
        this.currency = currency;
        this.amount = amount;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public float getAmount() {
        return amount;
    }

    public float getPurchasePrice() {
        return purchasePrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }
}
