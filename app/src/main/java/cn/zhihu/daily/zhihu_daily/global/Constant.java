package cn.zhihu.daily.zhihu_daily.global;

/**
 * Created by morc on 16-12-7.
 */

public interface Constant {
    //The Constants in this App
    String StartImageUrl = "http://news-at.zhihu.com/api/4/start-image/1080*1776";

    String LatestNews = "http://news-at.zhihu.com/api/4/news/latest";

    String NewsDetail = "http://news-at.zhihu.com/api/4/news/";

    String BeforeNews = "http://news-at.zhihu.com/api/4/news/before/";

    String NewsExtra = "http://news-at.zhihu.com/api/4/story-extra/";

    String ThemeList = "http://news-at.zhihu.com/api/4/themes";

    String ThemeNews = "http://news-at.zhihu.com/api/4/theme/";

    String DatabaseName = "News.db";

    int NETWORK_ERROR = -1;

    int JSON_PARSE_ERROR = -2;

    int NETWORK_ERROR_NEED_RETRY = -3;

    int NO_AVAILABLE_NETWORK = -4;

    int NETWORK_ERROR_NEED_RETRY_THEME_LIST = -5;

    int DOWNLOAD_LATEST_NEWS_SUCCESS = 1;

    int DOWNLOAD_NEWS_DETAIL_SUCCESS = 2;

    int DOWNLOAD_BEFORE_NEWS_SUCCESS = 3;

    int DOWNLOAD_THEME_LIST_SUCCESS = 4;

    int DOWNLOAD_THEME_NEWS_SUCCESS = 5;

    int THEME_CHANGE = 6;

    int THEME_HOME_ID = 0;

    int ITEM_DATE_TYPE = 7;

}
