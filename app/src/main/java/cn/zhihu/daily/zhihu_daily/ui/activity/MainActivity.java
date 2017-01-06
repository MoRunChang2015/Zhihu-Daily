package cn.zhihu.daily.zhihu_daily.ui.activity;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import cn.zhihu.daily.zhihu_daily.Interface.SensorCallback;
import cn.zhihu.daily.zhihu_daily.Interface.StoriesListHandler;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.base.BaseActivity;
import cn.zhihu.daily.zhihu_daily.database.NewsDbHelper;
import cn.zhihu.daily.zhihu_daily.global.Config;
import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.model.Theme;
import cn.zhihu.daily.zhihu_daily.model.ThemeList;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.service.NewsService;
import cn.zhihu.daily.zhihu_daily.ui.fragment.ContentMainFragment;
import cn.zhihu.daily.zhihu_daily.ui.fragment.ThemeListFragment;
import cn.zhihu.daily.zhihu_daily.util.CommonUtil;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;
import cn.zhihu.daily.zhihu_daily.util.SensorUtil;

public class MainActivity extends BaseActivity {
    final String tag = "MainActivity";

    CommonUtil commonUtil;
    private NewsService newsService;
    private SensorUtil sensorUtil;
    private NewsDbHelper newsDbHelper;
    private ArrayList<Integer> list = null;

    ContentMainFragment contentMainFragment;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        newsDbHelper = new NewsDbHelper(this);
        commonUtil = new CommonUtil(coordinatorLayout);
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorUtil = new SensorUtil(sensorManager, sensorCallback);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (!NetworkUtil.isNetworkAvailable(this))
            commonUtil.promptMsg("Network is not available");
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
        contentMainFragment = (ContentMainFragment)getFragmentManager().
                findFragmentById(R.id.content_main);
        contentMainFragment.setOnListMovedToEndListener(new StoriesListHandler() {
            @Override
            public void getBeforeNews(String date) {
                newsService.getBeforeNews(date, handler);
                // Log.d(tag, "Get news: " + date);
                // commonUtil.promptMsg("Get news: " + date);
            }

            @Override
            public void getDailyNews() {
                newsService.getDailyNews(handler);
            }

            @Override
            public void onDateChange(String date) {
                toolbar.setTitle(date + "的大新闻");
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(MainActivity.this,
                new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent event) {
                contentMainFragment.scrollToTop();
                return super.onDoubleTap(event);
            }
        });


        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    public SensorCallback sensorCallback = new SensorCallback() {
        @Override
        public void onShake() {
            int len = list.size();
            Random random = new Random();
            boolean flag = false;
            int id = 0;
            while (len != 0) {
                len--;
                id = list.get(random.nextInt(list.size()));
                if (!newsDbHelper.isRead(id)) {
                    flag = true;
                    break;
                }
            }
            if (flag && id != 0) {
                Intent intent = new Intent(MainActivity.this, StoryDetailActivity.class);
                intent.putExtra("Detail", id);
                startActivity(intent);
            } else {
                commonUtil.promptMsg("今天没有新的大新闻啦！");
            }
        }
    };

    private ServiceConnection sc =  new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            newsService = ((NewsService.MyBinder)iBinder).getService();
            newsService.getDailyNews(handler);
            newsService.getThemeList(handler);
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
                    dailyNews.getStories().add(0, new Summary());
                    list = new ArrayList<>();
                    for (Summary summary: dailyNews.getStories()) {
                        summary.setDate(dailyNews.getDate());
                        list.add(summary.getId());
                    }
                    contentMainFragment.setDailyNews(dailyNews);
                    sensorUtil.setListener();
                    break;
                case Constant.DOWNLOAD_BEFORE_NEWS_SUCCESS:
                    DailyNews beforeNews = (DailyNews)message.obj;
                    Summary dateSummary = new Summary();
                    dateSummary.setType(Constant.ITEM_DATE_TYPE);
                    beforeNews.getStories().add(0, dateSummary);
                    for (Summary summary: beforeNews.getStories()) {
                        summary.setDate(beforeNews.getDate());
                    }
                    contentMainFragment.appendStories(beforeNews.getStories());
                    break;
                case Constant.DOWNLOAD_THEME_LIST_SUCCESS:
                    ThemeList themeList = (ThemeList)((Object[])message.obj)[0];
                    setNavigation(themeList);
                    break;
                case Constant.DOWNLOAD_THEME_NEWS_SUCCESS:
                    ThemeNews themeNews = (ThemeNews)((Object[])message.obj)[0];
                    contentMainFragment.addThemeNews(themeNews);
                    // commonUtil.promptMsg("Download Theme News Success!");
                    break;
                case Constant.NETWORK_ERROR:
                    commonUtil.promptMsg("Network Error");
                    break;

                case Constant.JSON_PARSE_ERROR:
                    commonUtil.promptMsg("Json Parse Error");
                    break;
                case Constant.THEME_CHANGE:
                    drawer.closeDrawers();
                    Theme theme = (Theme) message.obj;
                    // commonUtil.promptMsg("theme name is " + theme.getName());
                    if (theme.getId() != Constant.THEME_HOME_ID) {
                        newsService.getThemeNews(theme.getId(), handler);
                        toolbar.setTitle(theme.getName());
                    } else {
                        if (contentMainFragment.themeId != Constant.THEME_HOME_ID)
                            toolbar.setTitle("今日的大新闻");
                    }
                    contentMainFragment.changeTheme(theme.getId());
                    break;
                case Constant.NETWORK_ERROR_NEED_RETRY:
                    handler.postDelayed(beforeNewsRetry, 2000);
                    // commonUtil.promptMsg("Network Error...retrying...");
                    break;
                case Constant.NO_AVAILABLE_NETWORK:
                    contentMainFragment.stopLoading();
                    commonUtil.promptMsg("No Available network");
                    break;

                case  Constant.NETWORK_ERROR_NEED_RETRY_THEME_LIST:
                    handler.postDelayed(themeListRetry, 2000);
                    break;
                default:
                    break;
            }
        }
    };

    Runnable beforeNewsRetry = new Runnable() {
        @Override
        public void run() {
            contentMainFragment.getBeforeStoriesFail();
        }
    };

    Runnable themeListRetry = new Runnable() {
        @Override
        public void run() {
            newsService.getThemeList(handler);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sensorUtil.rmListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorUtil.setListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unbindService(sc);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        sensorUtil.rmListener();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the ThemeListFragment; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_no_image) {
            if (Config.downLoadImage) {
                item.setTitle(R.string.action_download_image);
                commonUtil.promptMsg("开启无图模式");
            } else {
                item.setTitle(R.string.action_no_image);
                commonUtil.promptMsg("关闭无图模式");
            }
            Config.downLoadImage = !Config.downLoadImage;
            return true;
        }
        if (id == R.id.about) {
            AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
            aboutDialog.setView(R.layout.dialog_about)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setNavigation(ThemeList themeList) {
        ThemeListFragment themeListFragment = (ThemeListFragment)getFragmentManager()
                .findFragmentById(R.id.theme_list_fragment);
        themeListFragment.setThemeList(themeList, handler);
    }
}
