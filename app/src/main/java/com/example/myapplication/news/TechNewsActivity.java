package com.example.myapplication.news;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

public class TechNewsActivity extends BaseNewsActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 父类BaseNewsActivity已经处理了所有ListView的初始化和点击事件
        // 不需要额外代码
    }

    // 由于使用ListView方式，评论功能已经在BaseNewsActivity中统一处理
    // 不再需要单独处理评论按钮点击事件
    // 可以完全删除launchWriteCommentActivity方法


}