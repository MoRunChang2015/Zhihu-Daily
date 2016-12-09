package cn.zhihu.daily.zhihu_daily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.zhihu.daily.zhihu_daily.constant.Constant;
import cz.msebera.android.httpclient.Header;

/**
 * Created by morc on 16-12-7.
 */

public class NetworkUtil {
    static final String tag = "NetworkUtil";
    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    private static void get(String url, TextHttpResponseHandler responseHandler) {
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

    public static void getStartImageUrl(TextHttpResponseHandler handler) {
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
}
