package cn.zhihu.daily.zhihu_daily.service;

import android.app.Service;
import android.content.Intent;
import android.net.Network;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.zhihu.daily.zhihu_daily.constant.Constant;
import cn.zhihu.daily.zhihu_daily.factory.JsonResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Detail;
import cn.zhihu.daily.zhihu_daily.model.Theme;
import cn.zhihu.daily.zhihu_daily.model.ThemeList;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;
import cz.msebera.android.httpclient.Header;

public class NewsService extends Service {
    final String tag = "NewsService";
    private final IBinder binder = new MyBinder();
    public NewsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void getDailyNews(final Handler handler) {
        NetworkUtil.getLatestNews(JsonResponseHandlerFactory.createHandler(DailyNews.class, handler,
                Constant.DOWNLOAD_LATEST_NEWS_SUCCESS, Constant.NETWORK_ERROR));
    }


    public void getStartImage(final Handler handler) {
        NetworkUtil.getStartImageUrl(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String url = response.getString("img");
                    //TODO: Download Image
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = Constant.NETWORK_ERROR;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                Message msg = new Message();
                msg.what = Constant.NETWORK_ERROR;
                handler.sendMessage(msg);
            }
        });
    }


    public void getNewsDetail(int id, final Handler handler) {
        NetworkUtil.getNewsDetail(id, JsonResponseHandlerFactory.createHandler(Detail.class, handler,
                Constant.DOWNLOAD_NEWS_DETAIL_SUCCESS, Constant.NETWORK_ERROR));
    }

    public void getBeforeNews(String date, final Handler handler) {
        NetworkUtil.getBeforeNews(date, JsonResponseHandlerFactory.createHandler(DailyNews.class, handler,
                Constant.DOWNLOAD_BEFORE_NEWS_SUCCESS, Constant.NETWORK_ERROR));
    }


    public void getThemeList(final Handler handler) {
        NetworkUtil.getThemeList(JsonResponseHandlerFactory.createHandler(ThemeList.class, handler,
                Constant.DOWNLOAD_THEME_LIST_SUCCESS, Constant.NETWORK_ERROR));
    }

    public void getThemeNews(int id, final Handler handler) {
        NetworkUtil.getThemeNews(id, JsonResponseHandlerFactory.createHandler(ThemeNews.class, handler,
                Constant.DOWNLOAD_THEME_NEWS_SUCCESS, Constant.NETWORK_ERROR));
    }



    public class MyBinder extends Binder {
        public NewsService getService() {
            return NewsService.this;
        }
    }
}
