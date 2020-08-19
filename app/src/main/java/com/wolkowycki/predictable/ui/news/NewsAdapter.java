package com.wolkowycki.predictable.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolkowycki.predictable.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private ArrayList<NewsItem> newsList;

    public NewsAdapter(Context context, ArrayList<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem currentItem = newsList.get(position);

        // String imageUrl = currentItem.getImageUrl();
        String header = currentItem.getHeader();
        String author = currentItem.getAuthor();

        holder.textViewHeader.setText(header);
        holder.textViewAuthor.setText(author);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        // public ImageView imageView;
        public TextView textViewHeader;
        public TextView textViewAuthor;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewHeader = itemView.findViewById(R.id.news_header);
            this.textViewAuthor = itemView.findViewById(R.id.news_author);

        }
    }
}
