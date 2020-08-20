package com.wolkowycki.predictable.ui.news;

public class NewsItem {
    // private String imageUrl;
    private String header;
    private String link;
    private String date;

    public NewsItem(String header, String link, String date) {
        // this.imageUrl = imageUrl;
        this.header = header;
        this.link = link;
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public String getLink() {
        return link;
    }

    public String getDate() { return date; }
}
