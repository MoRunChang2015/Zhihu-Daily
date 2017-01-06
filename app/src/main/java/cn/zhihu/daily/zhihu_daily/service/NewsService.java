package cn.zhihu.daily.zhihu_daily.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;
import cn.zhihu.daily.zhihu_daily.Interface.OnImageDownloaded;
import cn.zhihu.daily.zhihu_daily.database.NewsDbHelper;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.factory.JsonResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Detail;
import cn.zhihu.daily.zhihu_daily.model.ThemeList;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.util.CommonUtil;
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
        Calendar calendar = Calendar.getInstance();
        final String date = CommonUtil.getCurrentFormatDate(calendar);
        DailyNews dailyNews = newsDbHelper.getLatestNews(date);
        if (dailyNews != null) {
            Log.d("Get latest from db", date);
            Message message = new Message();
            message.obj = dailyNews;
            message.what = Constant.DOWNLOAD_LATEST_NEWS_SUCCESS;
            handler.sendMessage(message);
        }
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Message message = new Message();
            message.what = Constant.NO_AVAILABLE_NETWORK;
            handler.sendMessage(message);
            return;
        }

        Handler newsServiceHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.DOWNLOAD_LATEST_NEWS_SUCCESS:
                        Object[] objects = (Object[])msg.obj;
                        String json = (String)objects[1];
                        newsDbHelper.setLatestNews(date, json);
                        Message message = new Message();
                        message.obj = objects[0];
                        message.what = msg.what;
                        handler.sendMessage(message);
                }
            }
        };
        NetworkUtil.getLatestNews(JsonResponseHandlerFactory.createHandler(DailyNews.class, newsServiceHandler,
                Constant.DOWNLOAD_LATEST_NEWS_SUCCESS, Constant.NETWORK_ERROR, Constant.JSON_PARSE_ERROR));
    }


    public void updateWelcomeImage() {
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            return;
        }
        NetworkUtil.getStartImageUrl(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    final String url = response.getString("img");
                    final SharedPreferences sharedPreferences = getSharedPreferences("WelcomeImage", MODE_PRIVATE);
                    String oldUrl = sharedPreferences.getString("url", null);
                    if (oldUrl == null || !oldUrl.equals(url)) {
                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        final String imagePath = getFilesDir().getPath() + "/images/";
                        final String md5 = CommonUtil.getMD5(url);
                        NetworkUtil.getImage(url,
                                ImageResponseHandlerFactory.createHandler(new OnImageDownloaded() {
                                    @Override
                                    public void Do(Bitmap bitmap) {
                                        try (FileOutputStream out = new FileOutputStream(new File(imagePath, md5))) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                            editor.putString("url", url);
                                            editor.putString("path", imagePath + md5);
                                            editor.apply();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                // do nothing
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
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Message message = new Message();
            message.what = Constant.NO_AVAILABLE_NETWORK;
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
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Message message = new Message();
            message.what = Constant.NETWORK_ERROR_NEED_RETRY;
            handler.sendMessage(message);
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
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Message message = new Message();
            message.what = Constant.NETWORK_ERROR_NEED_RETRY_THEME_LIST;
            handler.sendMessage(message);
            return;
        }
        NetworkUtil.getThemeList(JsonResponseHandlerFactory.createHandler(ThemeList.class, handler,
                Constant.DOWNLOAD_THEME_LIST_SUCCESS, Constant.NETWORK_ERROR_NEED_RETRY_THEME_LIST, Constant.JSON_PARSE_ERROR));
    }

    public void getThemeNews(int id, final Handler handler) {
        if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            Message message = new Message();
            message.what = Constant.NO_AVAILABLE_NETWORK;
            handler.sendMessage(message);
            return;
        }
        NetworkUtil.getThemeNews(id, JsonResponseHandlerFactory.createHandler(ThemeNews.class, handler,
                Constant.DOWNLOAD_THEME_NEWS_SUCCESS, Constant.NETWORK_ERROR, Constant.JSON_PARSE_ERROR));
    }



    public class MyBinder extends Binder {
        public NewsService getService() {
            return NewsService.this;
        }
    }
}
