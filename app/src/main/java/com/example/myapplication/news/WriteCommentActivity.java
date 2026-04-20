package com.example.myapplication.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.MainActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class WriteCommentActivity extends AppCompatActivity {

    public static final String EXTRA_COMMENT = "com.example.myapplication.extra.COMMENT";
    private int newsIndex;
    private String currentSection;
    private String newsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 检查用户是否登录
        if (!MainActivity.isUserLoggedIn(this)) {
            Toast.makeText(this, "请先登录才能发表评论", Toast.LENGTH_SHORT).show();
            finish(); // 关闭当前Activity
            return;
        }
        setContentView(R.layout.activity_write_comment);

        // 设置 Toolbar 和返回按钮
        Toolbar toolbar = findViewById(R.id.new_navigation);
        setSupportActionBar(toolbar);

        // 启用返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("发表评论");
        }

        // 获取传递的数据
        Intent intent = getIntent();
        newsTitle = intent.getStringExtra(BaseNewsActivity.EXTRA_NEWSTITLE);
        newsIndex = intent.getIntExtra(BaseNewsActivity.EXTRA_NEWS_INDEX, -1);
        currentSection = intent.getStringExtra(BaseNewsActivity.EXTRA_SECTION);

        // 设置新闻标题
        TextView textView = findViewById(R.id.txv_comment_news_title);
        textView.setText(newsTitle);

        System.out.println("接收到的数据 - title: " + newsTitle + ", newsIndex: " + newsIndex + ", section: " + currentSection);
    }

    // 处理返回按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // 关闭当前 Activity，返回上一页
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void returnComment(View view) {
        EditText commentEditText = findViewById(R.id.edt_write_comment);
        String strComment = commentEditText.getText().toString().trim();

        if (strComment.isEmpty()) {
            commentEditText.setError("请输入评论内容");
            return;
        }

        Intent commentIntent = new Intent();
        commentIntent.putExtra(EXTRA_COMMENT, strComment);
        commentIntent.putExtra(BaseNewsActivity.EXTRA_NEWS_INDEX, newsIndex);
        commentIntent.putExtra(BaseNewsActivity.EXTRA_SECTION, currentSection);
        setResult(RESULT_OK, commentIntent);
        finish();
    }
}