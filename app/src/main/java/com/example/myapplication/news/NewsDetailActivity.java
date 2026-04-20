package com.example.myapplication.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.MainActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class NewsDetailActivity extends AppCompatActivity {

    private int newsIndex;
    private String currentSection;
    private NewsDataManager.NewsItem currentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 检查用户是否登录
        if (!MainActivity.isUserLoggedIn(this)) {
            Toast.makeText(this, "请先登录才能查看新闻详情", Toast.LENGTH_SHORT).show();
            finish(); // 关闭当前Activity
            return;
        }
        setContentView(R.layout.activity_news_detail);

        // 获取传递的数据
        Intent intent = getIntent();
        currentSection = intent.getStringExtra(BaseNewsActivity.EXTRA_SECTION);
        newsIndex = intent.getIntExtra(BaseNewsActivity.EXTRA_NEWS_INDEX, -1);

        setupToolbar();
        setupViews();
        setupCommentButton();
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViews() {
        if (newsIndex == -1) return;

        NewsDataManager.NewsItem[] newsItems = NewsDataManager.getInstance().getNewsBySection(currentSection);
        if (newsItems == null || newsIndex >= newsItems.length) return;

        currentNews = newsItems[newsIndex];

        ImageView newsImage = findViewById(R.id.detailNewsImage);
        TextView newsTitle = findViewById(R.id.detailNewsTitle);
        TextView newsContent = findViewById(R.id.detailNewsContent);
        TextView commentText = findViewById(R.id.commentText);

        // 设置新闻内容
        newsImage.setImageResource(currentNews.getImageRes());
        newsTitle.setText(currentNews.getTitle());
        newsContent.setText(currentNews.getFullContent());

        // 显示评论内容
        updateCommentsDisplay();
    }

    private void updateCommentsDisplay() {
        TextView commentText = findViewById(R.id.commentText);

        if (currentNews.getCommentCount() > 0) {
            // 使用带时间戳的格式化评论
            commentText.setText(currentNews.getFormattedCommentsWithTime());
            commentText.setVisibility(View.VISIBLE);
        } else {
            commentText.setVisibility(View.GONE);
        }
    }

    private void setupCommentButton() {
        Button commentBtn = findViewById(R.id.commentBtn);
        commentBtn.setOnClickListener(v -> {
            EditText commentInput = findViewById(R.id.commentInput);
            String comment = commentInput.getText().toString().trim();

            if (!comment.isEmpty()) {
                // 添加新评论（不覆盖之前的）
                NewsDataManager.getInstance().addComment(currentSection, newsIndex, comment);

                // 更新显示
                updateCommentsDisplay();

                // 清空输入框
                commentInput.setText("");

                // 显示成功提示
                Toast.makeText(this, "评论发表成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
            }
        });
    }
}