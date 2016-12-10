package cn.zhihu.daily.zhihu_daily.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.base.BaseActivity;
import cn.zhihu.daily.zhihu_daily.constant.Constant;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Detail;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.model.Theme;
import cn.zhihu.daily.zhihu_daily.model.ThemeList;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.service.NewsService;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {
    final String tag = "MainActivity";
    private NewsService newsService;
    private ServiceConnection sc =  new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            newsService = ((NewsService.MyBinder)iBinder).getService();
            newsService.getDailyNews(handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            newsService = null;
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case Constant.DOWNLOAD_LATEST_NEWS_SUCCESS:
                    DailyNews dailyNews = (DailyNews)message.obj;
                    Toast.makeText(MainActivity.this, "Download Daily News Success!", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.DOWNLOAD_NEWS_DETAIL_SUCCESS:
                    Detail detail = (Detail)message.obj;
                    Toast.makeText(MainActivity.this, "Donwload News Detail Success", Toast.LENGTH_SHORT).show();
                    break;

                case Constant.DOWNLOAD_BEFORE_NEWS_SUCCESS:
                    DailyNews beforeNews = (DailyNews)message.obj;
                    Toast.makeText(MainActivity.this, "Download Before News Success!", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.DOWNLOAD_THEME_LIST_SUCCESS:
                    ThemeList themeList = (ThemeList)message.obj;
                    Toast.makeText(MainActivity.this, "Download Theme List Success!", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.DOWNLOAD_THEME_NEWS_SUCCESS:
                    ThemeNews themeNews = (ThemeNews)message.obj;
                    Log.d(tag, themeNews.getDescription());
                    Toast.makeText(MainActivity.this, "Download Theme News Success!", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.NETWORK_ERROR:
                    Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int setLayout() {
        return R.layout.main_layout;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (!NetworkUtil.isNetworkAvailable(this))
            Toast.makeText(this, "Network is not initViews", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(MainActivity.this, NewsService.class);
            bindService(intent, sc, BIND_AUTO_CREATE);
        }
    }
}
