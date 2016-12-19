package cn.zhihu.daily.zhihu_daily.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
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
import android.util.Log;

import butterknife.BindView;
import cn.zhihu.daily.zhihu_daily.Interface.StoriesListHandler;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.base.BaseActivity;
import cn.zhihu.daily.zhihu_daily.constant.Constant;
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

public class MainActivity extends BaseActivity {
    final String tag = "MainActivity";

    CommonUtil commonUtil;
    private NewsService newsService;

    ContentMainFragment contentMainFragment;


    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        commonUtil = new CommonUtil(fab);

        if (!NetworkUtil.isNetworkAvailable(this))
            commonUtil.promptMsg("Network is not available");
        else {
            Intent intent = new Intent(MainActivity.this, NewsService.class);
            bindService(intent, sc, BIND_AUTO_CREATE);
        }
        contentMainFragment = (ContentMainFragment)getFragmentManager().
                findFragmentById(R.id.content_main);
        contentMainFragment.setOnListMovedToEndListener(new StoriesListHandler() {
            @Override
            public void onEnd(String date) {
                newsService.getBeforeNews(date, handler);
                Log.d(tag, "Get news: " + date);
                commonUtil.promptMsg("Get news: " + date);
            }

            @Override
            public void onDateChange(String date) {
                toolbar.setTitle(date + "的大新闻");
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
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
                    for (Summary summary: dailyNews.getStories()) {
                        summary.setDate(dailyNews.getDate());
                    }
                    contentMainFragment.setDailyNews(dailyNews);
                    break;
                case Constant.DOWNLOAD_BEFORE_NEWS_SUCCESS:
                    DailyNews beforeNews = (DailyNews)message.obj;
                    Summary dateSummary = new Summary();
                    dateSummary.setType(Constant.ITEM_DATE_TYPE);
                    beforeNews.getStories().add(0, dateSummary);
                    for (Summary summary: beforeNews.getStories()) {
                        summary.setDate(beforeNews.getDate());
                    }
                    contentMainFragment.addSummary(beforeNews.getStories());
                    break;
                case Constant.DOWNLOAD_THEME_LIST_SUCCESS:
                    ThemeList themeList = (ThemeList)message.obj;
                    setNavigation(themeList);
                    break;
                case Constant.DOWNLOAD_THEME_NEWS_SUCCESS:
                    ThemeNews themeNews = (ThemeNews)message.obj;
                    Log.d(tag, themeNews.getDescription());
                    commonUtil.promptMsg("Download Theme News Success!");
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
                    commonUtil.promptMsg("theme name is " + theme.getName());
                    break;
                case Constant.NETWORK_ERROR_NEED_RETRY:
                    contentMainFragment.getBeforeStoriesFail();
                    commonUtil.promptMsg("Network Error...retrying...");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(sc);
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
        if (id == R.id.action_settings) {
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
