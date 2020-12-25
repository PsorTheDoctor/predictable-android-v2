package com.wolkowycki.predictable.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.wolkowycki.predictable.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private static final String[] TAGS = {
            "Forbes", "TechCrunch", "CoinDesk", "Cointelegraph"
    };

    private RecyclerView tagsRecycler;
    private TagsAdapter tagsAdapter;
    private ArrayList<TagItem> tagItems;
    private RecyclerView newsRecycler;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsItem> newsList;
    private RequestQueue queue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        tagsRecycler = root.findViewById(R.id.tags_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                root.getContext(), LinearLayoutManager.HORIZONTAL, false);
        tagsRecycler.setLayoutManager(layoutManager);
        tagsRecycler.setItemAnimator(new DefaultItemAnimator());

        tagItems = new ArrayList<>();
        for (int i = 0; i < TAGS.length; i++) {
            TagItem item = new TagItem(TAGS[i]);
            tagItems.add(item);
        }

        tagsAdapter = new TagsAdapter(root.getContext(), tagItems);
        tagsRecycler.setAdapter(tagsAdapter);

        newsRecycler = root.findViewById(R.id.news_view);
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));

        newsList = new ArrayList<>();
        queue = Volley.newRequestQueue(root.getContext());
        parseJson();
        return root;
    }

    private void parseJson() {
        String url = "https://predictable-api.herokuapp.com/entries-list/10";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject entry = response.getJSONObject(i);
                        String header = entry.getString("header");
                        String publisher = entry.getString("publisher");
                        String link = entry.getString("link");
                        String date = entry.getString("date");

                        newsList.add(new NewsItem(header, publisher, link, date));
                    }
                    newsAdapter = new NewsAdapter(getContext(), newsList);
                    newsRecycler.setAdapter(newsAdapter);
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
