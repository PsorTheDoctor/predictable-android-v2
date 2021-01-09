package com.wolkowycki.predictable.ui.news;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wolkowycki.predictable.R;
import com.wolkowycki.predictable.utils.Constants;
import com.wolkowycki.predictable.utils.LocalStore;

import org.json.JSONException;
import org.json.JSONObject;

import static com.wolkowycki.predictable.ui.news.NewsFragment.COLOR;
import static com.wolkowycki.predictable.ui.news.NewsFragment.DATE;
import static com.wolkowycki.predictable.ui.news.NewsFragment.HEADER;
import static com.wolkowycki.predictable.ui.news.NewsFragment.IDX;
import static com.wolkowycki.predictable.ui.news.NewsFragment.PUBLISHER;

public class EntryActivity extends AppCompatActivity {

    private RequestQueue queue;
    private String article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        int idx = intent.getIntExtra(IDX, 0);
        String header = intent.getStringExtra(HEADER);
        String publisher = intent.getStringExtra(PUBLISHER);
        String date = intent.getStringExtra(DATE);
        int color = intent.getIntExtra(COLOR, Color.parseColor("#000000"));

        TextView headerView = findViewById(R.id.entry_header);
        TextView articleView = findViewById(R.id.entry_article);
        TextView publisherView = findViewById(R.id.entry_publisher);
        TextView dateView = findViewById(R.id.entry_date);

        headerView.setText(header);
        publisherView.setText(publisher);
        publisherView.setBackgroundColor(color);
        dateView.setText(date);

        parseArticle(this, idx);

        article = LocalStore.loadArticle(this, "article&idx=" + idx);
        articleView.setText(article);
    }

    private void parseArticle(final Activity root, final int idx) {
        String url = Constants.API + "/entries-full/" + idx;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String article = response.getString("article");
                            LocalStore.saveArticle(root, "article&idx=" + idx, article);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}