package cn.zhihu.daily.zhihu_daily.model;

import java.util.List;

/**
 * Created by morc on 16-12-9.
 */

public class DailyNews {
    private String date;
    private List<Summary> stories;
    private List<TopStory> top_stories;

    public List<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStory> top_stories) {
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
