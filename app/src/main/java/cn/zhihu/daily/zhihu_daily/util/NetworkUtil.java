package cn.zhihu.daily.zhihu_daily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cn.zhihu.daily.zhihu_daily.global.Config;
import cn.zhihu.daily.zhihu_daily.global.Constant;

/**
 * Created by morc on 16-12-7.
 */

public class NetworkUtil {
    static final String tag = "NetworkUtil";
    private static AsyncHttpClient httpClient = new AsyncHttpClient(true, 80, 443);

    private static void get(String url, TextHttpResponseHandler responseHandler) {
        httpClient.get(url, responseHandler);
    }

    private static void get(String url, JsonHttpResponseHandler responseHandler) {
        httpClient.get(url, responseHandler);
    }

    private static void get(String url, BinaryHttpResponseHandler responseHandler) {
        httpClient.get(url, responseHandler);
    }

    private static void get(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        httpClient.get(url, params, responseHandler);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void getStartImageUrl(JsonHttpResponseHandler handler) {
        NetworkUtil.get(Constant.StartImageUrl, handler);
    }

    public static void getLatestNews(TextHttpResponseHandler handler) {
        NetworkUtil.get(Constant.LatestNews, handler);
    }

    public static void getNewsDetail(int newsId, TextHttpResponseHandler handler) {
        NetworkUtil.get(Constant.NewsDetail + Integer.toString(newsId), handler);
    }

    public static void getNewsExtra(int newsId, TextHttpResponseHandler handler) {
        NetworkUtil.get(Constant.NewsExtra + Integer.toString(newsId), handler);
    }

    public static void getBeforeNews(String date, TextHttpResponseHandler handler) {
        NetworkUtil.get(Constant.BeforeNews + date, handler);
    }

    public static void getThemeList(TextHttpResponseHandler handler) {
        NetworkUtil.get(Constant.ThemeList, handler);
    }

    public static void getThemeNews(int themeId, TextHttpResponseHandler handler) {
        NetworkUtil.get(Constant.ThemeNews + Integer.toString(themeId), handler);
    }

    public static void getImage(String url, BinaryHttpResponseHandler handler) {
        if (Config.downLoadImage) {
            get(url, handler);
        }
    }
}
