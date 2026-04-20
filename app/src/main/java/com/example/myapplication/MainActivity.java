package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.news.LianghuiNewsActivity;
import com.example.myapplication.news.NewsListActivity;
import com.example.myapplication.news.TechNewsActivity;
import com.example.myapplication.server.MyFakeServer;

public class MainActivity extends AppCompatActivity {
    private String currentUserName = null;
    ActivityResultLauncher<Intent> launcher;
    static public MyFakeServer myFakeServer;

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private Button mainLoginButton;
    private ImageButton userSettingButton;
    private TextView mainUsername;

    public static final String EXTRA_SECTIONTITLE = "com.example.myapplication.extra.SECTIONTITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myFakeServer = new MyFakeServer(this);

        // 从SharedPreferences加载登录状态
        loadLoginState();

        System.out.println("DEBUG: MainActivity onCreate - currentUser: " + currentUserName);

        initViews();
        setupActivityResultLauncher();
        setupClickListeners();
        updateUI();

        Toolbar toolbar = findViewById(R.id.new_navigation);
        setSupportActionBar(toolbar);
    }

    private void loadLoginState() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            currentUserName = prefs.getString(KEY_USERNAME, null);
        }
    }

    private void saveLoginState() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (currentUserName != null) {
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putString(KEY_USERNAME, currentUserName);
        } else {
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.remove(KEY_USERNAME);
        }
        editor.apply();
    }

    private void initViews() {
        mainLoginButton = findViewById(R.id.main_login);
        userSettingButton = findViewById(R.id.user_setting);
        mainUsername = findViewById(R.id.main_username);
    }

    private void setupClickListeners() {
        // 登录按钮点击事件 - 跳转到登录页面
        mainLoginButton.setOnClickListener(v -> {
            System.out.println("DEBUG: Login button clicked - currentUser: " + currentUserName);
            if (currentUserName == null) {
                // 未登录状态，使用launcher启动登录页面
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                System.out.println("DEBUG: Launching LoginActivity with launcher");
                launcher.launch(intent);
            } else {
                // 已登录状态，退出登录
                currentUserName = null;
                saveLoginState(); // 保存退出登录状态
                updateUI();
                Toast.makeText(MainActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
            }
        });

        // 用户设置按钮点击事件 - 跳转到用户设置页面
        userSettingButton.setOnClickListener(v -> {
            System.out.println("DEBUG: User setting button clicked - currentUser: " + currentUserName);
            if (currentUserName != null) {
                Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
                intent.putExtra("current_username", currentUserName);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupActivityResultLauncher() {
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    System.out.println("DEBUG: ActivityResult received - ResultCode: " + result.getResultCode());
                    System.out.println("DEBUG: ActivityResult - Data: " + result.getData());

                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        // 检查是否是从RegisterActivity返回
                        if (data.hasExtra("action") && "registered".equals(data.getStringExtra("action"))) {
                            String registeredUsername = data.getStringExtra("registered_username");
                            System.out.println("DEBUG: User registered: " + registeredUsername);

                            // 自动重新启动LoginActivity
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.putExtra("registered_username", registeredUsername);
                            launcher.launch(intent);
                            return;
                        }

                        // 检查是否是跳转到注册页面的action
                        if (data.hasExtra("action") && "goto_register".equals(data.getStringExtra("action"))) {
                            System.out.println("DEBUG: Starting RegisterActivity from LoginActivity");
                            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                            launcher.launch(intent);
                            return;
                        }

                        // 处理登录成功返回的数据
                        String username = data.getStringExtra("logged_username");
                        System.out.println("DEBUG: Received username from intent: " + username);

                        if (username != null && !username.isEmpty()) {
                            currentUserName = username;
                            saveLoginState(); // 保存登录状态
                            updateUI();
                            Toast.makeText(MainActivity.this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI() {
        System.out.println("DEBUG: updateUI called - currentUser: " + currentUserName);
        System.out.println("DEBUG: userSettingButton visibility before: " + userSettingButton.getVisibility());

        if (currentUserName != null) {
            // 已登录状态
            mainLoginButton.setText("退出登录");
            mainUsername.setText("Welcome " + currentUserName);
            mainUsername.setVisibility(View.VISIBLE);
            userSettingButton.setVisibility(View.VISIBLE);
            System.out.println("DEBUG: Setting userSettingButton to VISIBLE");
        } else {
            // 未登录状态
            mainLoginButton.setText("登录");
            mainUsername.setVisibility(View.GONE);
            userSettingButton.setVisibility(View.GONE);
            System.out.println("DEBUG: Setting userSettingButton to GONE");
        }

        System.out.println("DEBUG: userSettingButton visibility after: " + userSettingButton.getVisibility());
    }

    public void launchNewsActivity(View view) {
        // 检查用户是否登录
        if (currentUserName == null) {
            Toast.makeText(this, "请先登录才能查看新闻", Toast.LENGTH_SHORT).show();

            // 跳转到登录页面
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            launcher.launch(intent);
            return;
        }

        // 用户已登录，正常跳转到新闻页面
        String title = ((Button) view).getText().toString();
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

    // 添加静态方法供其他Activity检查登录状态
    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // 添加静态方法获取当前用户名
    public static String getCurrentUserName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, null);
    }
}