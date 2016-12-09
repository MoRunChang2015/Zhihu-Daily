package cn.zhihu.daily.zhihu_daily.service;

import android.app.Service;
import android.content.Intent;
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
import org.json.JSONObject;

import cn.zhihu.daily.zhihu_daily.constant.Constant;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
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

    public void downloadDailyNews(final Handler handler) {
        NetworkUtil.getLatestNews(new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                // called when response HTTP status is "200 OK"
                Log.d(tag, response);
                DailyNews dailyNews = null;
                try {
                    dailyNews = JSON.parseObject(response, DailyNews.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = Constant.NETWORK_ERROR;
                    handler.sendMessage(msg);
                }
                Message msg = new Message();
                if (dailyNews != null) {
                    msg.obj = dailyNews;
                    msg.what = Constant.DOWNLOAD_LATEST_NEWS_SUCCESS;
                } else {
                    msg.what = Constant.NETWORK_ERROR;
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Message msg = new Message();
                msg.what = Constant.NETWORK_ERROR;
                handler.sendMessage(msg);
            }
        });
    }

    public class MyBinder extends Binder {
        public NewsService getService() {
            return NewsService.this;
        }
    }
}
