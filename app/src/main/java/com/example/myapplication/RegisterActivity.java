package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.beans.BeanUserInfo;
import com.example.myapplication.server.MyFakeServer;


public class RegisterActivity extends AppCompatActivity {

    private EditText usernameTxt, pwdTxt, pwdConfirmTxt, phoneTxt, vfTxt;
    private Button sendCodeButton, regButton;
    private MyFakeServer myFakeServer=MainActivity.myFakeServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        usernameTxt = findViewById(R.id.username_txt);
        pwdTxt = findViewById(R.id.pwd_txt);
        pwdConfirmTxt = findViewById(R.id.pwd_confirm_txt);
        phoneTxt = findViewById(R.id.phone_txt);
        vfTxt = findViewById(R.id.vf_txt);
        sendCodeButton = findViewById(R.id.send_code_button);
        regButton = findViewById(R.id.reg_button);
    }

    private void setupClickListeners() {
        // 发送验证码按钮点击事件
        sendCodeButton.setOnClickListener(v -> {
            String phone = phoneTxt.getText().toString().trim();
            if (validatePhoneForCode()) {
                String code = myFakeServer.sendCode();
                Toast.makeText(RegisterActivity.this, "验证码已发送: " + code, Toast.LENGTH_SHORT).show();
            }
        });

        // 注册按钮点击事件 - 实现注册流程
        regButton.setOnClickListener(v -> {
            if (validateAllFields()) {
                performRegistration();
            }
        });


    }
    private boolean validateUsername() {
        String username = usernameTxt.getText().toString().trim();
        if (username.isEmpty()) {
            usernameTxt.setError("用户名不能为空");
            return false;
        }

        // 检查用户名是否已存在
        if (myFakeServer.userExist(username)) {
            usernameTxt.setError("用户名已存在");
            return false;
        }

        // 可以添加其他用户名规则验证，如长度、字符限制等
        if (username.length() < 4 || username.length() > 20) {
            usernameTxt.setError("用户名长度为4-20位");
            return false;
        }

        usernameTxt.setError(null);
        return true;
    }
    private boolean validatePassword() {
        String password = pwdTxt.getText().toString().trim();
        if (password.isEmpty()) {
            pwdTxt.setError("密码不能为空");
            return false;
        }

        // 验证密码长度6-20位
        if (password.length() < 6 || password.length() > 20) {
            pwdTxt.setError("密码长度为6-20位");
            return false;
        }

        // 验证密码只包含字母和数字
//        if (!password.matches("^[a-zA-Z0-9]+$")) {
//            pwdTxt.setError("密码只能包含字母和数字");
//            return false;
//        }

        pwdTxt.setError(null);
        return true;
    }
    private boolean validateConfirmPassword() {
        String password = pwdTxt.getText().toString().trim();
        String confirmPassword = pwdConfirmTxt.getText().toString().trim();

        if (confirmPassword.isEmpty()) {
            pwdConfirmTxt.setError("请确认密码");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            pwdConfirmTxt.setError("两次输入的密码不一致");
            return false;
        }

        pwdConfirmTxt.setError(null);
        return true;
    }
    private boolean validatePhone() {
        String phone = phoneTxt.getText().toString().trim();
        if (phone.isEmpty()) {
            phoneTxt.setError("手机号不能为空");
            return false;
        }

        // 验证手机号位数11位
        if (phone.length() != 11) {
            phoneTxt.setError("手机号必须是11位");
            return false;
        }

        // 验证手机号格式（简单验证，实际应该更严格）
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            phoneTxt.setError("手机号格式不正确");
            return false;
        }

        phoneTxt.setError(null);
        return true;
    }
    private boolean validatePhoneForCode() {
        String phone = phoneTxt.getText().toString().trim();
        if (phone.isEmpty()) {
            phoneTxt.setError("请输入手机号");
            return false;
        }

        if (phone.length() != 11) {
            phoneTxt.setError("手机号必须是11位");
            return false;
        }

        if (!phone.matches("^1[3-9]\\d{9}$")) {
            phoneTxt.setError("手机号格式不正确");
            return false;
        }

        phoneTxt.setError(null);
        return true;
    }
    private boolean validateVerificationCode() {
        String code = vfTxt.getText().toString().trim();
        if (code.isEmpty()) {
            vfTxt.setError("验证码不能为空");
            return false;
        }

        // 验证码是4位数字
        if (code.length() != 4) {
            vfTxt.setError("验证码必须是4位");
            return false;
        }

        // 验证验证码是否正确
        if (!myFakeServer.validateCode(code)) {
            vfTxt.setError("验证码错误");
            return false;
        }

        vfTxt.setError(null);
        return true;
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        if (!validateUsername()) isValid = false;
        if (!validatePassword()) isValid = false;
        if (!validateConfirmPassword()) isValid = false;
        if (!validatePhone()) isValid = false;
        if (!validateVerificationCode()) isValid = false;

        return isValid;
    }

    private void performRegistration() {
        // 获取用户输入的信息
        String username = usernameTxt.getText().toString().trim();
        String password = pwdTxt.getText().toString().trim();
        String phone = phoneTxt.getText().toString().trim();

        // 创建用户信息对象
        BeanUserInfo userInfo = new BeanUserInfo(username, password, phone);

        // 将用户信息写入假服务器
        boolean success = myFakeServer.userRegister(userInfo);

        if (success) {
            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();

            System.out.println("DEBUG: Registration successful, returning to MainActivity");

            // 返回结果给MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("action", "registered");
            resultIntent.putExtra("registered_username", username);
            setResult(RESULT_OK, resultIntent);

            finish();
        } else {
            Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}