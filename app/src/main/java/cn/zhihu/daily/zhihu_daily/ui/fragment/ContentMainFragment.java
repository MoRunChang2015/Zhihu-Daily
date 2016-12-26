package cn.zhihu.daily.zhihu_daily.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.zhihu.daily.zhihu_daily.Interface.ExtendStoriesListHandler;
import cn.zhihu.daily.zhihu_daily.Interface.StoriesListHandler;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.adapter.StoriesListAdapter;
import cn.zhihu.daily.zhihu_daily.adapter.ThemeStoriesListAdapter;
import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.ui.view.ViewPagerWithIndicator;

/**
 * Created by tommy on 12/16/16.
 */

public class ContentMainFragment extends Fragment {
    ViewPagerWithIndicator topStoriesViewPager;
    RecyclerView contentList;
    StoriesListAdapter contentListAdapter;
    ThemeStoriesListAdapter themeStoriesListAdapter;
    StoriesListHandler listener;
    SwipeRefreshLayout refreshLayout;

    private LinearLayoutManager linearLayoutManager;

    public int themeId = Constant.THEME_HOME_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        refreshLayout = (SwipeRefreshLayout)inflater.inflate(R.layout.content_main, container);
        return refreshLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout.setColorSchemeColors(getContext().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.getDailyNews();
            }
        });
        refreshLayout.setRefreshing(true);
        contentList = (RecyclerView) getActivity().findViewById(R.id.story_list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        contentList.setLayoutManager(linearLayoutManager);
        topStoriesViewPager = new ViewPagerWithIndicator(getContext());
        contentListAdapter = new StoriesListAdapter(getContext(), topStoriesViewPager,
                new ExtendStoriesListHandler() {
                    @Override
                    public int getFirstVisibleItemPosition() {
                        return linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    }

                    @Override
                    public void getBeforeNews(String date) {
                listener.getBeforeNews(date);
            }

                    @Override
                    public void getDailyNews() {
                        listener.getDailyNews();
                    }

                    @Override
                    public void onDateChange(String date) {
                        listener.onDateChange(date);
                    }
                });

        contentList.setAdapter(contentListAdapter);
    }

    public void setOnListMovedToEndListener(StoriesListHandler listener) {
        this.listener = listener;
    }

    public void appendStories(List<Summary> summaryList) {
        contentListAdapter.appendStories(summaryList);
    }

    public void getBeforeStoriesFail() {
        contentListAdapter.getBeforeStoriesFail();
    }

    public void changeTheme(int themeId) {
        if (themeId == Constant.THEME_HOME_ID) {
            if (contentList.getAdapter() != contentListAdapter)
                contentList.setAdapter(contentListAdapter);
            this.themeId = Constant.THEME_HOME_ID;
        } else {
            themeStoriesListAdapter = new ThemeStoriesListAdapter(getContext());
            contentList.setAdapter(themeStoriesListAdapter);
            this.themeId = themeId;
        }
    }

    public void addThemeNews(ThemeNews themeNews) {
        if (themeStoriesListAdapter != null) {
            themeStoriesListAdapter.setThemeNewsList(themeNews);
        }
    }

    public void scrollToTop() {
        contentList.smoothScrollToPosition(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (linearLayoutManager.findFirstVisibleItemPosition() < 3) {
                    return;
                }
                contentList.stopScroll();
                contentList.scrollToPosition(1);
                contentList.smoothScrollToPosition(0);
            }
        }, 200);
    }

    public void stopLoading() {
        refreshLayout.setRefreshing(false);
    }

    public void setDailyNews(DailyNews dailyNews) {
        refreshLayout.setRefreshing(false);
        topStoriesViewPager.setContent(dailyNews.getTop_stories());
        contentListAdapter.updateDailyNews(dailyNews.getStories());
    }
}
