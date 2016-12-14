package cn.zhihu.daily.zhihu_daily.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
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
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.adapter.StoriesListAdapter;
import cn.zhihu.daily.zhihu_daily.base.BaseActivity;
import cn.zhihu.daily.zhihu_daily.constant.Constant;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Detail;
import cn.zhihu.daily.zhihu_daily.model.Theme;
import cn.zhihu.daily.zhihu_daily.model.ThemeList;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.service.NewsService;
import cn.zhihu.daily.zhihu_daily.ui.view.TopStoriesFragment;
import cn.zhihu.daily.zhihu_daily.util.CommonUtil;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

public class MainActivity extends BaseActivity {
    final String tag = "MainActivity";

    CommonUtil commonUtil;
    private NewsService newsService;

    TopStoriesFragment topStoriesFragment;

    @BindView(R.id.story_list)
    RecyclerView storyListView;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            commonUtil.promtMsg("Network is not available");
        else {
            Intent intent = new Intent(MainActivity.this, NewsService.class);
            bindService(intent, sc, BIND_AUTO_CREATE);
        }
        topStoriesFragment = (TopStoriesFragment)getFragmentManager().
                findFragmentById(R.id.top_story_fragment);
        storyListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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
                    topStoriesFragment.setContent(MainActivity.this, dailyNews.getTop_stories());
                    storyListView.setAdapter(new StoriesListAdapter(MainActivity.this, dailyNews.getStories()));
                    commonUtil.promtMsg("Download Daily news Success!");
                    break;
                case Constant.DOWNLOAD_NEWS_DETAIL_SUCCESS:
                    Detail detail = (Detail)message.obj;
                    commonUtil.promtMsg("Download news Detail Success");
                    break;

                case Constant.DOWNLOAD_BEFORE_NEWS_SUCCESS:
                    DailyNews beforeNews = (DailyNews)message.obj;
                    commonUtil.promtMsg("Download Before News Success!");
                    break;
                case Constant.DOWNLOAD_THEME_LIST_SUCCESS:
                    ThemeList themeList = (ThemeList)message.obj;
                    setNavigation(themeList);
                    commonUtil.promtMsg("Download Theme List Success!");
                    break;
                case Constant.DOWNLOAD_THEME_NEWS_SUCCESS:
                    ThemeNews themeNews = (ThemeNews)message.obj;
                    Log.d(tag, themeNews.getDescription());
                    commonUtil.promtMsg("Download Theme News Success!");
                    break;
                case Constant.NETWORK_ERROR:
                    commonUtil.promtMsg("Network Error");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/


    private void setNavigation(ThemeList themeList) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu().getItem(0).getSubMenu();
        for (Theme theme : themeList.getOthers()) {
            menu.add(0, theme.getId(), 0, theme.getName());
        }
        //navigationView.setNavigationItemSelectedListener(this);
    }
}
