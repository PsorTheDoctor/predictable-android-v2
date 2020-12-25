package com.wolkowycki.predictable.ui.news;

import android.graphics.Color;

public class NewsItem {
    private String header;
    private String publisher;
    private String link;
    private String date;
    private int color;

    public NewsItem(String header, String publisher, String link, String date) {
        this.header = header;
        this.publisher = publisher;
        this.link = link;
        this.date = date;

        switch (publisher) {
            case "Forbes":
                this.color = Color.parseColor("#000000");
                break;
            case "TechCrunch":
                this.color = Color.parseColor("#0a9e01");
                break;
            case "Reuters":
                this.color = Color.parseColor("#ff8000");
                break;
            case "CoinDesk":
                this.color = Color.parseColor("#161f36");
                break;
            case "CoinTelegraph":
                this.color = Color.parseColor("#263137");
                break;
            case "CoinGeek":
                this.color = Color.parseColor("#a7a6a7");
                break;
            case "GlobeNewswire":
                this.color = Color.parseColor("#00a4c4");
                break;
            default:
                this.color = Color.parseColor("#e0e0e0");
                break;
        }
    }

    public String getHeader() {
        return header;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public int getColor() {
        return color;
    }
}
