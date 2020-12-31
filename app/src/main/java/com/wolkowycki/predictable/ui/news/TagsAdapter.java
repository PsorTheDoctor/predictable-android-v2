package com.wolkowycki.predictable.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wolkowycki.predictable.R;
import com.wolkowycki.predictable.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsViewHolder> {

    private ArrayList<TagItem> tags;
    private Context context;
    private RequestQueue queue;

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
    public void onBindViewHolder(@NonNull TagsViewHolder holder, final int position) {
        holder.tagBtn.setText(tags.get(position).getTagName());

        holder.tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQuery(tags.get(position).getTagName());
            }
        });
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

    private void postQuery(final String query) {
        String url = Constants.API + "/entry-search/" + query;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("query", query);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(request);
    }
}
