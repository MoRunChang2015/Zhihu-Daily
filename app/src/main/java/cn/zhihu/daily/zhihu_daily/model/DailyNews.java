package cn.zhihu.daily.zhihu_daily.model;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by morc on 16-12-9.
 */

public class DailyNews {
    private String date;
    private List<Summary> stories;
    private List<TopStories> top_stories;

    public List<TopStories> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStories> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Summary> getStories() {
        return stories;
    }

    public void setStories(List<Summary> stories) {
        this.stories = stories;
    }
}
