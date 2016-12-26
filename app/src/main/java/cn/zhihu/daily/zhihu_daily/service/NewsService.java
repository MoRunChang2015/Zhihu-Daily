package cn.zhihu.daily.zhihu_daily.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cn.zhihu.daily.zhihu_daily.database.NewsDbHelper;
import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.factory.JsonResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Detail;
import cn.zhihu.daily.zhihu_daily.model.ThemeList;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;
import cz.msebera.android.httpclient.Header;

public class NewsService extends Service {
    final String tag = "NewsService";
    private final IBinder binder = new MyBinder();
    private NewsDbHelper newsDbHelper;
    public NewsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        newsDbHelper = new NewsDbHelper(getApplicationContext());
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void getDailyNews(final Handler handler) {
        NetworkUtil.getLatestNews(JsonResponseHandlerFactory.createHandler(DailyNews.class, handler,
                Constant.DOWNLOAD_LATEST_NEWS_SUCCESS, Constant.NETWORK_ERROR, Constant.JSON_PARSE_ERROR));
    }


    public void getStartUpImage(final Handler handler) {
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
        Detail detail = newsDbHelper.getNewsDetail(id);
        if (detail != null) {
            Message message = new Message();
            message.obj = detail;
            message.what = Constant.DOWNLOAD_NEWS_DETAIL_SUCCESS;
            handler.sendMessage(message);
            return;
        }

        Handler newsServiceHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.DOWNLOAD_NEWS_DETAIL_SUCCESS:
                        Object[] objects = (Object[])msg.obj;
                        Detail res = (Detail) objects[0];
                        String json = (String)objects[1];
                        newsDbHelper.setNewsDetail(res.getId(), json);
                        Message message = new Message();
                        message.obj = objects[0];
                        message.what = msg.what;
                        handler.sendMessage(message);
                }
            }
        };

        NetworkUtil.getNewsDetail(id, JsonResponseHandlerFactory.createHandler(Detail.class, newsServiceHandler,
                Constant.DOWNLOAD_NEWS_DETAIL_SUCCESS, Constant.NETWORK_ERROR, Constant.JSON_PARSE_ERROR));
    }

    public void getBeforeNews(String date, final Handler handler) {
        DailyNews beforeNews = newsDbHelper.getBeforeNewsAt(date);
        if (beforeNews  != null) {
            Message msg = new Message();
            msg.obj = beforeNews;
            msg.what = Constant.DOWNLOAD_BEFORE_NEWS_SUCCESS;
            handler.sendMessage(msg);
            return;
        }

        Handler newsServiceHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.DOWNLOAD_BEFORE_NEWS_SUCCESS:
                        Object[] objects = (Object[])msg.obj;
                        DailyNews dailyNews = (DailyNews) objects[0];
                        String json = (String)objects[1];
                        newsDbHelper.setBeforeNewsAt(dailyNews.getDate(), json);
                        Message message = new Message();
                        message.obj = objects[0];
                        message.what = msg.what;
                        handler.sendMessage(message);
                }
            }
        };

        NetworkUtil.getBeforeNews(date, JsonResponseHandlerFactory.createHandler(
                DailyNews.class,
                newsServiceHandler,
                Constant.DOWNLOAD_BEFORE_NEWS_SUCCESS,
                Constant.NETWORK_ERROR_NEED_RETRY,
                Constant.JSON_PARSE_ERROR));
    }


    public void getThemeList(final Handler handler) {
        NetworkUtil.getThemeList(JsonResponseHandlerFactory.createHandler(ThemeList.class, handler,
                Constant.DOWNLOAD_THEME_LIST_SUCCESS, Constant.NETWORK_ERROR, Constant.JSON_PARSE_ERROR));
    }

    public void getThemeNews(int id, final Handler handler) {
        NetworkUtil.getThemeNews(id, JsonResponseHandlerFactory.createHandler(ThemeNews.class, handler,
                Constant.DOWNLOAD_THEME_NEWS_SUCCESS, Constant.NETWORK_ERROR, Constant.JSON_PARSE_ERROR));
    }



    public class MyBinder extends Binder {
        public NewsService getService() {
            return NewsService.this;
        }
    }
}
