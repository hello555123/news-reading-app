package com.example.myapplication.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_SECTIONTITLE = "com.example.myapplication.extra.SECTIONTITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.new_navigation);
        setSupportActionBar(toolbar);
    }

    public void launchNewsActivity(View view){
        String title = ((Button)view).getText().toString();
        Class<?> targetActivity = getTargetActivity(title);

        Intent newsIntent = new Intent(this, targetActivity);
        newsIntent.putExtra(EXTRA_SECTIONTITLE, title);
        startActivity(newsIntent);
    }

    private Class<?> getTargetActivity(String title) {
        switch (title) {
            case "精选新闻":
                return NewsListActivity.class;
            case "科技前沿":
                return TechNewsActivity.class;
            case "两会典读":
                return LianghuiNewsActivity.class;
            default:
                return NewsListActivity.class;
        }
    }
}

