package cn.zhihu.daily.zhihu_daily.Interface;

/**
 * Created by tommy on 12/17/16.
 */

public interface StoriesListHandler {
    void onEnd(String date);

    void onDateChange(String date);
}
