package com.wolkowycki.predictable.ui.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wolkowycki.predictable.R;

import static com.wolkowycki.predictable.ui.news.NewsFragment.ARTICLE;
import static com.wolkowycki.predictable.ui.news.NewsFragment.HEADER;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Intent intent = getIntent();
        String header = intent.getStringExtra(HEADER);
        String article = intent.getStringExtra(ARTICLE);

        TextView headerView = findViewById(R.id.entry_header);
        TextView articleView = findViewById(R.id.entry_article);

        headerView.setText(header);
        articleView.setText(article);
    }
}
