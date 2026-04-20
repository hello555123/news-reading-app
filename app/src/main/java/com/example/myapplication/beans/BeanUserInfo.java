package com.example.myapplication.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BeanUserInfo {
    String username;
    String password;
    String phone;
    String gender = "unknown";
    String[] hobbies = {"unknown"};
    String job = "unknown";

    public BeanUserInfo(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public BeanUserInfo(String username, String password, String phone, String gender, String[] hobbies, String job) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.hobbies = hobbies;
        this.job = job;
    }

    // Getter and Setter methods
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String[] getHobbies() { return hobbies; }
    public void setHobbies(String[] hobbies) { this.hobbies = hobbies; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    // 序列化为JSON字符串
    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
            json.put("phone", phone);
            json.put("gender", gender);
            json.put("job", job);

            // 序列化爱好数组
            JSONArray hobbiesArray = new JSONArray();
            if (hobbies != null) {
                for (String hobby : hobbies) {
                    hobbiesArray.put(hobby);
                }
            }
            json.put("hobbies", hobbiesArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    // 从JSON字符串反序列化
    public static BeanUserInfo fromJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            String username = json.getString("username");
            String password = json.getString("password");
            String phone = json.getString("phone");
            String gender = json.optString("gender", "unknown");
            String job = json.optString("job", "unknown");

            // 解析爱好数组
            JSONArray hobbiesArray = json.optJSONArray("hobbies");
            String[] hobbies = {"unknown"};
            if (hobbiesArray != null && hobbiesArray.length() > 0) {
                List<String> hobbyList = new ArrayList<>();
                for (int i = 0; i < hobbiesArray.length(); i++) {
                    hobbyList.add(hobbiesArray.getString(i));
                }
                hobbies = hobbyList.toArray(new String[0]);
            }

            return new BeanUserInfo(username, password, phone, gender, hobbies, job);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}