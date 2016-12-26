package cn.zhihu.daily.zhihu_daily.Interface;

/**
 * Created by tommy on 12/17/16.
 */

public interface StoriesListHandler {
    void getBeforeNews(String date);
    void getDailyNews();
    void onDateChange(String date);
}
