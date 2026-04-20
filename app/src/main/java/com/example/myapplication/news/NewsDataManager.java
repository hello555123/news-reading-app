package com.example.myapplication.news;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewsDataManager {
    private static NewsDataManager instance;
    private Map<String, NewsItem[]> newsData = new HashMap<>(); // 直接初始化
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "news_comments";

    // 图片资源数组
    private int[] selectedNewsImages = {
            R.drawable.selected_news_1, R.drawable.selected_news_2, R.drawable.selected_news_3,
            R.drawable.selected_news_4, R.drawable.selected_news_5
    };

    private int[] techNewsImages = {
            R.drawable.tech_news_1, R.drawable.tech_news_2, R.drawable.tech_news_3,
            R.drawable.tech_news_4, R.drawable.tech_news_5
    };

    private int[] lianghuiNewsImages = {
            R.drawable.lianghui_news_1, R.drawable.lianghui_news_2, R.drawable.lianghui_news_3,
            R.drawable.lianghui_news_4, R.drawable.lianghui_news_5
    };

    private NewsDataManager() {
        // 直接初始化数据
        initData();
    }

    public static NewsDataManager getInstance() {
        if (instance == null) {
            instance = new NewsDataManager();
        }
        return instance;
    }

    public void initialize(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadSavedComments(); // 只加载保存的评论
    }

    private void initData() {
        // 精选新闻板块 - 5条新闻
        NewsItem[] selectedNews = {
                new NewsItem("AI处于什么阶段？",
                        "科技媒体评论人：现在还是DOS的时代！",
                        "AI现在处于什么阶段？科技媒体评论人阑夕认为，现在还是DOS的时代，大多数应用还是在强调生产力，" +
                                "到日常生活的渗透非常弱，能否做一些很真实的事情，其实没有，很多都是既有场景的强化。\n\n" +
                                "“我们还在等待windows出来，现在看来变数还是很大的。”他表示，在消费级市场上有一个大的应用，" +
                                "能适用很多日常场景，那时候才是真正的新的时代。",
                        selectedNewsImages[0], new ArrayList<>()),

                new NewsItem("人工智能加速落地",
                        "为何要强调'自立自强'",
                        "美国在GPU架构、AI框架等基础领域占据绝对优势，而中国升腾芯片等硬件虽已突破百万级出货量，" +
                                "但软件生态完善度不足40%。人工智能是构建新质生产力的核心引擎，但其发展高度依赖芯片、算法等基础软硬件系统，" +
                                "若关键环节受制于人，可能威胁到国家安全和经济稳定。自立自强需要从应用及辅助转向基础引领应用，" +
                                "在神经形态芯片、多模态大模型等应用前沿领域实现突破。",
                        selectedNewsImages[1], new ArrayList<>()),

                new NewsItem("洪都拉斯与中国建交",
                        "中国和中美洲国家洪都拉斯正式建立外交关系",
                        "中国和中美洲国家洪都拉斯正式建立外交关系。在签署中洪建交公报后，外交部长秦刚向洪方发出邀请...",
                        selectedNewsImages[2], new ArrayList<>()),

                new NewsItem("加快培育外贸高质量发展新功能",
                        "外贸进出口是拉动经济增长的重要引擎",
                        "外贸进出口是拉动经济增长的重要引擎。过去5年，我国坚定扩大对外开放，推动外贸进出口稳中提质...",
                        selectedNewsImages[3], new ArrayList<>()),

                new NewsItem("刘强东王兴：不是'兄弟'",
                        "京东即将发力外卖",
                        "京东创始人刘强东曾向三位'老友'表达了京东即将发力外卖的想法...",
                        selectedNewsImages[4], new ArrayList<>())
        };

        // 科技前沿板块 - 5条新闻
        NewsItem[] techNews = {
                new NewsItem("ChatGPT是否会带来失业？",
                        "香颂资本沈萌表示，我们希望技术让人类社会更有效率...",
                        "香颂资本沈萌表示，我们希望技术让人类社会更有效率。第一，技术的发展不以个人意志为转移，是社会发展的大趋势..." +
                                "把职位的焦虑影射在某一项技术上并不非常正确。",
                        techNewsImages[0], new ArrayList<>()),

                new NewsItem("比尔·盖茨：ChatGPT是革命性技术进步",
                        "OpenAI开发的GPT人工智能模型是1980年现代GUI出现以来最具革命性的技术进步...",
                        "比尔·盖茨表示，OpenAI开发的GPT人工智能模型是1980年现代GUI出现以来最具革命性的技术进步..." +
                                "GPT书写的文本与人类的高度相似，也能生成几乎可以使用的计算机代码。",
                        techNewsImages[1], new ArrayList<>()),

                new NewsItem("量子计算新突破",
                        "科学家实现100量子比特计算能力",
                        "最新研究在量子计算领域取得重大突破，成功实现了100量子比特的稳定计算...",
                        techNewsImages[2], new ArrayList<>()),

                new NewsItem("5G技术演进",
                        "6G研发已启动，预计2030年商用",
                        "随着5G网络在全球范围内的部署，各国科研机构已开始着手6G技术的研发工作...",
                        techNewsImages[3], new ArrayList<>()),

                new NewsItem("元宇宙发展",
                        "虚拟与现实融合的新时代",
                        "元宇宙技术正在快速发展，各大科技公司纷纷布局...",
                        techNewsImages[4], new ArrayList<>())
        };

        // 两会典读板块 - 5条新闻
        NewsItem[] lianghuiNews = {
                new NewsItem("万物并育而不相害，道并行而不相悖",
                        "当前，世界之变、时代之变、历史之变正以前所未有的方式展开...",
                        "当前，世界之变、时代之变、历史之变正以前所未有的方式展开。世界多极化、经济全球化、社会信息化、文化多样化深入发展..." +
                                "和平发展大势不可逆转。",
                        lianghuiNewsImages[0], new ArrayList<>()),

                new NewsItem("富贵不能淫，贫贱不能移，威武不能屈",
                        "党风问题关系党的生死存亡。党的十八大以来...",
                        "党风问题关系党的生死存亡。党的十八大以来，以习近平同志为核心的党中央以刀刃向内的自我革命精神..." +
                                "消除了党、国家、军队内部存在的严重隐患。",
                        lianghuiNewsImages[1], new ArrayList<>()),

                new NewsItem("民生保障持续改善",
                        "基本养老金水平继续提高",
                        "今年政府工作报告明确提出将继续提高退休人员基本养老金水平...",
                        lianghuiNewsImages[2], new ArrayList<>()),

                new NewsItem("科技创新驱动发展",
                        "加大研发投入支持关键技术突破",
                        "国家将加大对科技创新的支持力度，在人工智能、量子信息、集成电路等重点领域实现技术突破...",
                        lianghuiNewsImages[3], new ArrayList<>()),

                new NewsItem("乡村振兴战略",
                        "推进农业农村现代化建设",
                        "乡村振兴战略将继续深化实施，通过产业发展、基础设施建设等多方面措施...",
                        lianghuiNewsImages[4], new ArrayList<>())
        };

        newsData.put("精选新闻", selectedNews);
        newsData.put("科技前沿", techNews);
        newsData.put("两会典读", lianghuiNews);

        Log.d("NewsDataManager", "数据初始化完成，共" + newsData.size() + "个板块");
    }

    // 保存评论到SharedPreferences
    private void saveComments() {
        if (sharedPreferences == null) {
            Log.e("NewsDataManager", "SharedPreferences not initialized");
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 清除旧的评论数据
        editor.clear();
        // 保存所有评论
        int totalCommentCount = 0;
        for (Map.Entry<String, NewsItem[]> entry : newsData.entrySet()) {
            String section = entry.getKey();
            NewsItem[] newsItems = entry.getValue();

            for (int i = 0; i < newsItems.length; i++) {
                List<Comment> comments = newsItems[i].getComments();
                if (comments != null && !comments.isEmpty()) {
                    // 保存每条评论
                    for (int j = 0; j < comments.size(); j++) {
                        Comment comment = comments.get(j);
                        String contentKey = getCommentContentKey(section, i, j);
                        String timeKey = getCommentTimeKey(section, i, j);

                        editor.putString(contentKey, comment.getContent());
                        editor.putString(timeKey, comment.getTimestamp());
                        totalCommentCount++;
                    }
                }
            }
        }

        editor.apply();
        Log.d("NewsDataManager", "评论保存成功，共" + totalCommentCount + "条评论");
    }

    // 从SharedPreferences加载评论
    private void loadSavedComments() {
        if (sharedPreferences == null) {
            Log.e("NewsDataManager", "SharedPreferences not initialized");
            return;
        }
        for (Map.Entry<String, NewsItem[]> entry : newsData.entrySet()) {
            String section = entry.getKey();
            NewsItem[] newsItems = entry.getValue();
            for (int i = 0; i < newsItems.length; i++) {
                List<Comment> comments = new ArrayList<>();
                // 通过循环加载评论，直到找不到对应的key为止
                int j = 0;
                while (true) {
                    String contentKey = getCommentContentKey(section, i, j);
                    String timeKey = getCommentTimeKey(section, i, j);
                    String content = sharedPreferences.getString(contentKey, "");
                    String timestamp = sharedPreferences.getString(timeKey, "");
                    // 如果内容为空，说明没有更多评论了
                    if (content.isEmpty()) {
                        break;
                    }
                    comments.add(new Comment(content, timestamp));
                    j++;
                }
                // 如果有评论，设置到新闻项中
                if (!comments.isEmpty()) {
                    newsItems[i].setComments(comments);
                    Log.d("NewsDataManager", "加载评论: " + section + " 第" + i + "条新闻，共" + comments.size() + "条评论");
                }
            }
        }

        Log.d("NewsDataManager", "评论加载完成");
    }

    // 生成评论内容key
    private String getCommentContentKey(String section, int newsIndex, int commentIndex) {
        return section + "_news_" + newsIndex + "_comment_" + commentIndex + "_content";
    }

    // 生成评论时间key
    private String getCommentTimeKey(String section, int newsIndex, int commentIndex) {
        return section + "_news_" + newsIndex + "_comment_" + commentIndex + "_time";
    }

    public NewsItem[] getNewsBySection(String section) {
        NewsItem[] result = newsData.get(section);
        if (result == null) {
            Log.e("NewsDataManager", "找不到板块: " + section);
            return new NewsItem[0]; // 返回空数组而不是null
        }
        return result;
    }

    // 添加新评论（不覆盖之前的评论）
    public void addComment(String section, int newsIndex, String commentContent) {
        NewsItem[] newsItems = newsData.get(section);
        if (newsItems != null && newsIndex < newsItems.length) {
            Comment newComment = new Comment(commentContent, new Date());
            newsItems[newsIndex].addComment(newComment);
            saveComments(); // 保存到持久化存储
            Log.d("NewsDataManager", "添加评论: " + section + " 第" + newsIndex + "条新闻");
        } else {
            Log.e("NewsDataManager", "添加评论失败: " + section + " 第" + newsIndex + "条新闻不存在");
        }
    }

    // 获取所有评论
    public List<Comment> getComments(String section, int newsIndex) {
        NewsItem[] newsItems = newsData.get(section);
        if (newsItems != null && newsIndex < newsItems.length) {
            return newsItems[newsIndex].getComments();
        }
        return new ArrayList<>();
    }

    // 评论数据模型
    public static class Comment {
        private String content;
        private String timestamp;

        public Comment(String content, Date timestamp) {
            this.content = content;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            this.timestamp = sdf.format(timestamp);
        }

        public Comment(String content, String timestamp) {
            this.content = content;
            this.timestamp = timestamp;
        }

        public String getContent() { return content; }
        public String getTimestamp() { return timestamp; }
    }

    // 新闻数据模型
    public static class NewsItem {
        private String title;
        private String shortContent;
        private String fullContent;
        private int imageRes;
        private List<Comment> comments;

        public NewsItem(String title, String shortContent, String fullContent, int imageRes, List<Comment> comments) {
            this.title = title;
            this.shortContent = shortContent;
            this.fullContent = fullContent;
            this.imageRes = imageRes;
            this.comments = comments != null ? comments : new ArrayList<>();
        }

        // Getters and Setters
        public String getTitle() { return title; }
        public String getShortContent() { return shortContent; }
        public String getFullContent() { return fullContent; }
        public int getImageRes() { return imageRes; }
        public List<Comment> getComments() { return comments; }
        public void setComments(List<Comment> comments) { this.comments = comments; }

        // 添加评论
        public void addComment(Comment comment) {
            if (comments == null) {
                comments = new ArrayList<>();
            }
            comments.add(0, comment); // 新的评论添加到前面
        }

        // 获取评论数量
        public int getCommentCount() {
            return comments != null ? comments.size() : 0;
        }

        // 获取格式化后的评论显示文本
        public String getFormattedComments() {
            if (comments == null || comments.isEmpty()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < comments.size(); i++) {
                Comment comment = comments.get(i);
                sb.append(comment.getContent());

                // 添加分隔线（除了最后一条）
                if (i < comments.size() - 1) {
                    sb.append("\n---\n");
                }
            }
            return sb.toString();
        }

        // 获取带时间戳的格式化评论
        public String getFormattedCommentsWithTime() {
            if (comments == null || comments.isEmpty()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (Comment comment : comments) {
                sb.append(comment.getContent())
                        .append("\n")
                        .append(comment.getTimestamp())
                        .append("\n\n");
            }
            return sb.toString().trim();
        }
    }
}