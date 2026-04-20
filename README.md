项目概述
新闻阅览App 是一个基于 Android 原生开发的移动应用，实现用户登录注册、新闻浏览、评论互动等核心功能。

技术栈
分类	技术
开发语言	Java
UI布局	XML（ConstraintLayout、LinearLayout）
数据存储	SharedPreferences、ArrayList
列表展示	ListView + SimpleAdapter
数据格式	JSONObject/JSONArray
核心功能模块
1. 用户模块
注册：用户名/密码/手机号验证、验证码校验、正则表达式验证

登录：账号密码验证、记住密码功能（SharedPreferences）

用户设置：修改性别、职业、爱好等信息

2. 新闻模块
多板块新闻列表：支持多个新闻分类，每个板块展示多条新闻

新闻详情：点击进入查看完整新闻内容

评论功能：对新闻进行评论，评论内容本地持久化存储

3. 权限控制
用户必须登录后才能查看新闻内容（未登录点击新闻会提示登录）

项目亮点
模拟服务器：通过 MyFakeServer 类模拟后端接口，存储用户信息

数据持久化：用户信息用 SharedPreferences 存储，评论数据按新闻维度独立存储

代码复用：通过 BaseNewsActivity 基类抽象公共逻辑，减少重复代码

用户体验：记住密码、注册成功自动登录、登录后主页显示用户名
