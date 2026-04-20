package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.server.MyFakeServer;

public class LoginActivity extends AppCompatActivity {
    private EditText editLoginAccount;
    private EditText editLoginPsw;
    private Button btnLogin;
    private Button btnToSignup;
    private CheckBox cbRememberPwd;

    private SharedPreferences loginPrefs;
    private static final String LOGIN_PREFS = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";

    private String currentUserName = null;
    private MyFakeServer myFakeServer = MainActivity.myFakeServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        // 初始化视图
        initViews();

        // 加载保存的登录信息
        loadSavedCredentials();

        // 检查是否有从注册页面传递过来的用户名
        checkForRegisteredUsername();

        // 设置点击监听器
        setupClickListenersWithLambda();
    }

    private void initViews() {
        editLoginAccount = findViewById(R.id.login_name_txt);
        editLoginPsw = findViewById(R.id.login_pwd_txt);
        btnLogin = findViewById(R.id.login_button);
        btnToSignup = findViewById(R.id.login_reg_btn);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd); // 需要在布局中添加这个CheckBox

        loginPrefs = getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
    }

    private void loadSavedCredentials() {
        boolean remember = loginPrefs.getBoolean(KEY_REMEMBER, false);
        if (remember) {
            String username = loginPrefs.getString(KEY_USERNAME, "");
            String password = loginPrefs.getString(KEY_PASSWORD, "");
            editLoginAccount.setText(username);
            editLoginPsw.setText(password);
            cbRememberPwd.setChecked(true);
        }
    }

    private void checkForRegisteredUsername() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("registered_username")) {
            Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
            intent.removeExtra("registered_username");
        }
    }

    private void setupClickListenersWithLambda() {
        // 记住密码复选框监听
        cbRememberPwd.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 如果取消记住密码，清除保存的密码
            if (!isChecked) {
                SharedPreferences.Editor editor = loginPrefs.edit();
                editor.remove(KEY_PASSWORD);
                editor.remove(KEY_REMEMBER);
                editor.apply();
            }
        });

        // 登录按钮点击事件
        btnLogin.setOnClickListener(v -> {
            String account = editLoginAccount.getText().toString().trim();
            String psw = editLoginPsw.getText().toString().trim();
            System.out.println("DEBUG: Login attempt - username: " + account + ", password: " + psw);
            // 验证用户名和密码
            if (account.isEmpty()) {
                editLoginAccount.setError("请输入用户名");
                return;
            }

            if (psw.isEmpty()) {
                editLoginPsw.setError("请输入密码");
                return;
            }

            // 清除错误提示
            editLoginAccount.setError(null);
            editLoginPsw.setError(null);

            if (myFakeServer.userLogin(account, psw)) {
                System.out.println("DEBUG: Login successful, setting result and finishing");
                // 登录成功
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                // 保存登录信息
                saveLoginInfo(account, psw);

                // 创建返回结果
                Intent resultIntent = new Intent();
                resultIntent.putExtra("logged_username", account);
                setResult(RESULT_OK, resultIntent);

                // 关闭登录页面
                finish();
            } else {
                // 登录失败
                Toast.makeText(LoginActivity.this, "登录失败，用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

        // 注册按钮点击事件
        btnToSignup.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "去注册", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("action", "goto_register");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void saveLoginInfo(String username, String password) {
        SharedPreferences.Editor editor = loginPrefs.edit();
        if (cbRememberPwd.isChecked()) {
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.putBoolean(KEY_REMEMBER, true);
        } else {
            editor.remove(KEY_USERNAME);
            editor.remove(KEY_PASSWORD);
            editor.putBoolean(KEY_REMEMBER, false);
        }
        editor.apply();
    }
}