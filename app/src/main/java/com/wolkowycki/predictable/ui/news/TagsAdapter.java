package com.wolkowycki.predictable.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wolkowycki.predictable.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsViewHolder> {

    private ArrayList<TagItem> tags;
    private Context context;

    public TagsAdapter(Context context, ArrayList<TagItem> tags) {
        this.context = context;
        this.tags = tags;
    }

    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false);
        return new TagsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder holder, int position) {
        holder.tagBtn.setText(tags.get(position).getTagName());
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class TagsViewHolder extends RecyclerView.ViewHolder {
        Button tagBtn;

        public TagsViewHolder(@NonNull View itemView) {
            super(itemView);
            tagBtn = itemView.findViewById(R.id.btn_tag);
        }
    }
}
