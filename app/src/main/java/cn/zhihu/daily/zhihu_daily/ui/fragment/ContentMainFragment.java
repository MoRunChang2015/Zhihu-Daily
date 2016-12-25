package cn.zhihu.daily.zhihu_daily.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;
import cn.zhihu.daily.zhihu_daily.Interface.ExtendStoriesListHandler;
import cn.zhihu.daily.zhihu_daily.Interface.StoriesListHandler;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.adapter.StoriesListAdapter;
import cn.zhihu.daily.zhihu_daily.adapter.ThemeStoriesListAdapter;
import cn.zhihu.daily.zhihu_daily.adapter.TopStoriesAdapter;
import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.model.TopStory;
import cn.zhihu.daily.zhihu_daily.service.ImageProvider;
import cn.zhihu.daily.zhihu_daily.ui.activity.StoryDetailActivity;
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
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
            public void onEnd(String date) {
                listener.onEnd(date);
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

    public void addSummary(List<Summary> summaryList) {
        contentListAdapter.addStoriesList(summaryList);
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

    public void setDailyNews(DailyNews dailyNews) {
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        List<View> topStoryList = new ArrayList<>();
//        for (final TopStory topStory: dailyNews.getTop_stories()) {
//            final View item = inflater.inflate(R.layout.top_story_item, topStoriesViewPager.getPagesRoot(), false);
//            item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getContext(), StoryDetailActivity.class);
//                    intent.putExtra("Detail", topStory.getId());
//                    getContext().startActivity(intent);
//                }
//            });
//            ((TextView)item.findViewById(R.id.title)).setText(topStory.getTitle());
//            imageProvider.loadImage(topStory.getImage(), new BitmapContainer() {
//                @Override
//                public void setBitmap(Bitmap bitmap, int id) {
//
//                }
//
//                @Override
//                public void setBitmap(Bitmap bitmap) {
//                    ((ImageView) item.findViewById(R.id.image)).setImageBitmap(bitmap);
//                }
//
//                @Override
//                public Bitmap getBitmap() {
//                    return null;
//                }
//            }, null, null);
//
//            topStoryList.add(item);
//        }
        TopStoriesAdapter topStoriesAdapter = new TopStoriesAdapter(getContext(), topStoriesViewPager);
        topStoriesAdapter.setContent(dailyNews.getTop_stories());
        topStoriesViewPager.setAdapter(topStoriesAdapter);
        contentListAdapter.addStoriesList(dailyNews.getStories());
    }
}
