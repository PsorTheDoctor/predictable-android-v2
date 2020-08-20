package com.wolkowycki.predictable.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wolkowycki.predictable.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsItem> newsList;
    private RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = root.findViewById(R.id.news_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        parseJson();
        return root;
    }

    private void parseJson() {
        String url = "https://raw.githubusercontent.com/PsorTheDoctor/predictable-api/master/temp.json";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO!!
                try {
                    JSONArray entries = response.getJSONArray("entries");

                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject entry = entries.getJSONObject(i);
                        String header = entry.getString("title");
                        String link = entry.getString("link");
                        String date = entry.getString("date");

                        newsList.add(new NewsItem(header, link, date));
                    }
                    newsAdapter = new NewsAdapter(getContext(), newsList);
                    recyclerView.setAdapter(newsAdapter);
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
        requestQueue.add(request);
    }
}
