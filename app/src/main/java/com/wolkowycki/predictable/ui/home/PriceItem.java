package com.wolkowycki.predictable.ui.home;

public class PriceItem {
    private String currency;
    private String vsCurrency;
    private String value;

//    public PriceItem(String currency, String vsCurrency, String value) {
//        this.currency = currency;
//        this.vsCurrency = vsCurrency;
//        this.value = value;
//    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVsCurrency() {
        return vsCurrency;
    }

    public void setVsCurrency(String vsCurrency) {
        this.vsCurrency = vsCurrency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
