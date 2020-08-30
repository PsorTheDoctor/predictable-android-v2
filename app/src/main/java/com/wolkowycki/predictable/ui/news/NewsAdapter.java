package com.wolkowycki.predictable.ui.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        final String header = currentItem.getHeader();
        final String link = currentItem.getLink();
        final String date = currentItem.getDate();
        final String formattedDate = date.substring(0, 22);

        holder.textViewHeader.setText(header);
        holder.textViewDate.setText(formattedDate);

        holder.btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                context.startActivity(browser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHeader;
        public Button btnLink;
        public TextView textViewDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewHeader = itemView.findViewById(R.id.news_header);
            this.btnLink = itemView.findViewById(R.id.news_link);
            this.textViewDate = itemView.findViewById(R.id.news_date);
        }
    }
}
