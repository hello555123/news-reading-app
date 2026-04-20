package com.example.myapplication.server;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.example.myapplication.beans.BeanUserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class MyFakeServer {
    // 模拟服务器端的用户信息
    private ArrayList<BeanUserInfo> userList = new ArrayList<>();
    private String trueCode = "1234";
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserData";
    private static final String KEY_USERS = "users";
    private static final String KEY_TEST_USER_ADDED = "test_user_added";
    // 带Context的构造函数
    public MyFakeServer(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadUsersFromPreferences();

        // 如果首次运行，添加测试用户
        boolean testUserAdded = sharedPreferences.getBoolean(KEY_TEST_USER_ADDED, false);
        if (!testUserAdded && userList.isEmpty()) {
            BeanUserInfo userInfo = new BeanUserInfo("Test0001", "aaaaaa", "13100000000");
            userList.add(userInfo);
            saveUsersToPreferences();
            sharedPreferences.edit().putBoolean(KEY_TEST_USER_ADDED, true).apply();
        }
    }
    // 无参构造函数（保持兼容性）
    public MyFakeServer() {
        // 模拟服务器端的用户信息
        BeanUserInfo userInfo = new BeanUserInfo("Test0001", "aaaaaa", "13100000000");
        userList.add(userInfo);
    }

    // 从SharedPreferences加载用户数据
    private void loadUsersFromPreferences() {
        String usersJson = sharedPreferences.getString(KEY_USERS, "[]");
        try {
            JSONArray jsonArray = new JSONArray(usersJson);
            userList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                String userJson = jsonArray.getString(i);
                BeanUserInfo userInfo = BeanUserInfo.fromJson(userJson);
                if (userInfo != null) {
                    userList.add(userInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // 如果解析失败，初始化一个空列表
            userList.clear();
        }
    }
    // 保存用户数据到SharedPreferences
    private void saveUsersToPreferences() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (BeanUserInfo user : userList) {
                jsonArray.put(user.toJson());
            }
            sharedPreferences.edit().putString(KEY_USERS, jsonArray.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean userRegister(BeanUserInfo filledInfo) {
        // 检查用户是否已存在
        if (userExist(filledInfo.getUsername())) {
            return false;
        }
        userList.add(filledInfo);
        saveUsersToPreferences(); // 注册后保存到SharedPreferences
        return true;
    }
    public String sendCode() {
        return trueCode;
    }
    public boolean validateCode(@NonNull String filledCode) {
        return filledCode.equals(trueCode);
    }
    public boolean userExist(BeanUserInfo userInfo) {
        return userExist(userInfo.getUsername());
    }
    public boolean userExist(String username) {
        for (BeanUserInfo user : userList) {
            if (user.getUsername().equals(username)) {
                return true; // 用户已存在
            }
        }
        return false; // 用户不存在
    }
    public BeanUserInfo getUser(String username) {
        for (BeanUserInfo user : userList) {
            if (user.getUsername().equals(username)) {
                return user; // 用户存在
            }
        }
        return null; // 用户不存在
    }
    public boolean updateUser(BeanUserInfo userInfo) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(userInfo.getUsername())) {
                userList.set(i, userInfo);
                saveUsersToPreferences(); // 更新后保存到SharedPreferences
                return true;
            }
        }
        return false;
    }
    public boolean userLogin(BeanUserInfo userInfo) {
        return userLogin(userInfo.getUsername(), userInfo.getPassword());
    }
    public boolean userLogin(String username, String password) {
        for (BeanUserInfo user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // 登录成功
            }
        }
        return false; // 失败
    }
    // 获取所有用户（用于调试）
    public ArrayList<BeanUserInfo> getAllUsers() {
        return new ArrayList<>(userList);
    }
    // 清空所有用户数据（用于测试）
    public void clearAllUsers() {
        userList.clear();
        sharedPreferences.edit().remove(KEY_USERS).apply();
    }
}