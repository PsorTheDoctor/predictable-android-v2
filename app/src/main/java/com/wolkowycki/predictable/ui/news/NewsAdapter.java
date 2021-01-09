package com.wolkowycki.predictable.ui.news;

import android.content.Context;
// import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolkowycki.predictable.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private ArrayList<NewsItem> newsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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
        final String publisher = currentItem.getPublisher();
        final String content = currentItem.getContent();
        final String link = currentItem.getLink();
        final String date = currentItem.getDate();
        final String base64 = currentItem.getImg();
//        final String formattedDate = date.substring(0, 22);
//        final int color = currentItem.getColor();

        holder.textViewPublisher.setText(publisher);
//        holder.textViewPublisher.setBackgroundColor(color);
//        if (color != Color.parseColor("#e0e0e0")) {
//            holder.textViewPublisher.setTextColor(Color.parseColor("#ffffff"));
//        }

        holder.textViewHeader.setText(header);
        holder.textViewContent.setText(content);
        holder.textViewDate.setText(date);

//        byte[] decodedStr = Base64.decode(base64.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedStr, 0, decodedStr.length);
//        holder.imgView.setImageBitmap(decodedByte);

//        holder.btnLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//                context.startActivity(browser);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewHeader;
        private TextView textViewPublisher;
        private TextView textViewContent;
        private Button btnLink;
        private TextView textViewDate;
        private ImageView imgView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewHeader = itemView.findViewById(R.id.news_header);
            this.textViewPublisher = itemView.findViewById(R.id.news_publisher);
            this.textViewContent = itemView.findViewById(R.id.news_content);
            // this.btnLink = itemView.findViewById(R.id.news_link);
            this.textViewDate = itemView.findViewById(R.id.news_date);
            this.imgView = itemView.findViewById(R.id.news_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
