package com.example.myapplication.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseNewsActivity extends AppCompatActivity {
    protected String currentSection;
    protected NewsDataManager.NewsItem[] currentNews;
    // 常量定义
    public static final String EXTRA_NEWS_INDEX = "com.example.myapplication.extra.NEWS_INDEX";
    public static final String EXTRA_NEWSTITLE = "com.example.myapplication.extra.NEWSTITLE";
    public static final String EXTRA_SECTION = "com.example.myapplication.extra.SECTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list_base);

        // 检查用户是否登录
        if (!MainActivity.isUserLoggedIn(this)) {
            Toast.makeText(this, "请先登录才能查看新闻", Toast.LENGTH_SHORT).show();
            finish(); // 关闭当前Activity
            return;
        }

        setContentView(R.layout.activity_news_list_base);
        // 初始化NewsDataManager（必须在获取数据之前）
        NewsDataManager.getInstance().initialize(this);
        // 获取传递的数据
        Intent data = getIntent();
        currentSection = data.getStringExtra(MainActivity.EXTRA_SECTIONTITLE);

        if (currentSection == null) {
            currentSection = "精选新闻"; // 默认值
        }
        setupToolbar();
        loadNewsData();
        setupListView();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(currentSection);
        }
    }

    private void loadNewsData() {
        currentNews = NewsDataManager.getInstance().getNewsBySection(currentSection);
    }

    private void setupListView() {
        if (currentNews == null) return;
        ListView listView = findViewById(R.id.newsListView);
        // 准备数据源
        ArrayList<Map<String, Object>> dataList = new ArrayList<>();
        for (NewsDataManager.NewsItem newsItem : currentNews) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", newsItem.getImageRes());
            map.put("title", newsItem.getTitle());
            map.put("shortContent", newsItem.getShortContent());
            dataList.add(map);
        }
        // 创建SimpleAdapter
        String[] from = {"image", "title", "shortContent"};
        int[] to = {R.id.newsImage, R.id.newsTitle, R.id.newsShortContent};

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                dataList,
                R.layout.list_item_news,
                from,
                to
        );
        listView.setAdapter(adapter);
        // 设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Intent intent = new Intent(BaseNewsActivity.this, NewsDetailActivity.class);
                intent.putExtra(EXTRA_SECTION, currentSection);
                intent.putExtra(EXTRA_NEWS_INDEX, position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}