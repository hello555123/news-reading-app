package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.beans.BeanUserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserSettingActivity extends AppCompatActivity {
    private TextView usernameTxt;
    private RadioGroup genderGroup;
    private RadioButton genderOp1, genderOp2;
    private Spinner jobSpinner;
    private CheckBox hobbyOp1, hobbyOp2, hobbyOp3;
    private Button saveButton;
    private String currentUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
        initViews();
        setupSpinner();
        loadUserSetting();
        setupListeners();
    }
    private void initViews(){
        usernameTxt = findViewById(R.id.username_txt);
        genderGroup = findViewById(R.id.genderGroup);
        genderOp1 = findViewById(R.id.gender_op1);
        genderOp2 = findViewById(R.id.gender_op2);
        jobSpinner = findViewById(R.id.spinner);
        hobbyOp1 = findViewById(R.id.hobby_op1);
        hobbyOp2 = findViewById(R.id.hobby_op2);
        hobbyOp3 = findViewById(R.id.hobby_op3);
        saveButton = findViewById(R.id.save_button);
    }
    private void setupSpinner(){}
    private void loadUserSetting(){
        // 接收从主界面传递过来的用户信息
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("current_username")) {
            currentUsername = intent.getStringExtra("current_username");
            if (currentUsername != null) {
                usernameTxt.setText(currentUsername);
                // 获取并显示用户现有设置
                BeanUserInfo userInfo = MainActivity.myFakeServer.getUser(currentUsername);
                if (userInfo != null) {
                    // 设置性别
                    if ("male".equals(userInfo.getGender())) {
                        genderOp1.setChecked(true);
                    } else if ("female".equals(userInfo.getGender())) {
                        genderOp2.setChecked(true);
                    }
                    // 设置职业
                    if (userInfo.getJob() != null && !"unknown".equals(userInfo.getJob())) {
                        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) jobSpinner.getAdapter();
                        int position = adapter.getPosition(userInfo.getJob());
                        if (position >= 0) {
                            jobSpinner.setSelection(position);
                        }
                    }
                    // 设置爱好
                    if (userInfo.getHobbies() != null) {
                        for (String hobby : userInfo.getHobbies()) {
                            switch (hobby) {
                                case "reading":
                                    hobbyOp1.setChecked(true);
                                    break;
                                case "sports":
                                    hobbyOp2.setChecked(true);
                                    break;
                                case "music":
                                    hobbyOp3.setChecked(true);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
    private void setupListeners(){
        // 保存按钮点击事件
        if (saveButton != null) {
            saveButton.setOnClickListener(v -> {
                saveSettings();
            });
        }
    }
    private void saveSettings(){
        if (currentUsername == null) {
            Toast.makeText(this, "未登录用户", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 获取用户信息
            BeanUserInfo userInfo = MainActivity.myFakeServer.getUser(currentUsername);
            if (userInfo == null) {
                Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
                return;
            }

            // 获取性别
            String gender = "unknown";
            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            if (selectedGenderId == R.id.gender_op1) {
                gender = "male";
            } else if (selectedGenderId == R.id.gender_op2) {
                gender = "female";
            }
            userInfo.setGender(gender);

            // 获取职业
            String job = jobSpinner.getSelectedItem().toString();
            userInfo.setJob(job);

            // 获取爱好
            List<String> selectedHobbies = new ArrayList<>();
            if (hobbyOp1.isChecked()) {
                selectedHobbies.add("reading");
            }
            if (hobbyOp2.isChecked()) {
                selectedHobbies.add("sports");
            }
            if (hobbyOp3.isChecked()) {
                selectedHobbies.add("music");
            }

            // 如果没有选择任何爱好，设置为unknown
            if (selectedHobbies.isEmpty()) {
                selectedHobbies.add("unknown");
            }

            userInfo.setHobbies(selectedHobbies.toArray(new String[0]));

            // 更新用户信息到服务器
            boolean success = MainActivity.myFakeServer.updateUser(userInfo);

            if (success) {
                Toast.makeText(this, "设置保存成功", Toast.LENGTH_SHORT).show();
                finish(); // 保存成功后返回到主界面
            } else {
                Toast.makeText(this, "保存失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "保存过程中出现错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}