package com.wolkowycki.predictable.ui.news;

public class NewsItem {
    // private String imageUrl;
    private String header;
    private String author;

    public NewsItem(String header, String author) {
        // this.imageUrl = imageUrl;
        this.header = header;
        this.author = author;
    }

    public String getHeader() {
        return header;
    }

    public String getAuthor() {
        return author;
    }
}
