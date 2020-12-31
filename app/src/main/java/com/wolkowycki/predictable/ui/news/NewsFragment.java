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
import com.wolkowycki.predictable.utils.Constants;
import com.wolkowycki.predictable.utils.LocalStore;
import com.wolkowycki.predictable.utils.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private static final String[] TAGS = {
            "Forbes", "TechCrunch", "Reuters", "Guardian"
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
        Store store = new Store(requireActivity());

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

        tagsAdapter = new TagsAdapter(root.getContext(), tagItems, store);
        tagsRecycler.setAdapter(tagsAdapter);

        newsRecycler = root.findViewById(R.id.news_view);
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));

        newsList = new ArrayList<>();
        queue = Volley.newRequestQueue(root.getContext());

        int numOfNews = 10;
//        if (LocalStore.containsNews(requireActivity(), "news&idx=0&value=header")) {
//            loadNews(numOfNews);
//        } else {
//            removeNews(numOfNews);
//            parseJson(numOfNews);
//        }
        removeNews(numOfNews);
        parseJson(numOfNews);
        return root;
    }

    private void parseJson(int numOfNews) {
        String url = Constants.API + "/entries-list/" + String.valueOf(numOfNews);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject entry = response.getJSONObject(i);
                        String header = entry.getString("header");
                        String publisher = entry.getString("publisher");
                        String content = entry.getString("content");
                        String link = entry.getString("link");
                        String date = entry.getString("n_days_ago");
                        String img = entry.getString("img");

                        newsList.add(new NewsItem(header, publisher, content, link, date, img));

                        String idx = String.valueOf(i);
                        LocalStore.saveNews(requireActivity(), "news&idx=" + idx + "&value=header", header);
                        LocalStore.saveNews(requireActivity(), "news&idx=" + idx + "&value=publisher", publisher);
                        LocalStore.saveNews(requireActivity(), "news&idx=" + idx + "&value=content", content);
                        LocalStore.saveNews(requireActivity(), "news&idx=" + idx + "&value=link", link);
                        LocalStore.saveNews(requireActivity(), "news&idx=" + idx + "&value=date", date);
                        LocalStore.saveNews(requireActivity(), "news&idx=" + idx + "&value=img", img);
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

    private void loadNews(int numOfNews) {
        for (int i = 0; i < numOfNews; i++) {
            String idx = String.valueOf(i);
            String header = LocalStore.loadNews(requireActivity(), "news%idx=" + idx + "&value=header");
            String publisher = LocalStore.loadNews(requireActivity(), "news&idx=" + idx + "&value=publisher");
            String content = LocalStore.loadNews(requireActivity(), "news&idx=" + idx + "&value=content");
            String link = LocalStore.loadNews(requireActivity(), "news&idx=" + idx + "&value=link");
            String date = LocalStore.loadNews(requireActivity(), "news&idx=" + idx + "&value=date");
            String img = LocalStore.loadNews(requireActivity(), "news&idx=" + idx + "&value=img");

            newsList.add(new NewsItem(header, publisher, content, link, date, img));
        }
        newsAdapter = new NewsAdapter(getContext(), newsList);
        newsRecycler.setAdapter(newsAdapter);
    }

    private void removeNews(int numOfNews) {
        for (int i = 0; i < numOfNews; i++) {
            String idx = String.valueOf(i);
            LocalStore.removeNews(requireActivity(), "news&idx=" + idx + "&value=header");
            LocalStore.removeNews(requireActivity(), "news&idx=" + idx + "&value=publisher");
            LocalStore.removeNews(requireActivity(), "news&idx=" + idx + "&value=content");
            LocalStore.removeNews(requireActivity(), "news&idx=" + idx + "&value=link");
            LocalStore.removeNews(requireActivity(), "news&idx=" + idx + "&value=date");
            LocalStore.removeNews(requireActivity(), "news&idx=" + idx + "&value=img");
        }
    }
}
