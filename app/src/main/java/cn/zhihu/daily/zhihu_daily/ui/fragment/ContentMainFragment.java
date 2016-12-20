package cn.zhihu.daily.zhihu_daily.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
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
import cn.zhihu.daily.zhihu_daily.adapter.TopStoriesAdapter;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.model.TopStory;
import cn.zhihu.daily.zhihu_daily.ui.activity.StoryDetailActivity;
import cn.zhihu.daily.zhihu_daily.ui.view.ViewPagerWithIndicator;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by tommy on 12/16/16.
 */

public class ContentMainFragment extends Fragment {
    ViewPagerWithIndicator topStoriesViewPager;
    RecyclerView contentList;
    StoriesListAdapter contentListAdapter;
    StoriesListHandler listener;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    public void setTopStory(List<TopStory> topStoryList) {
        //
    }

    public void addSummary(List<Summary> summaryList) {
        contentListAdapter.addStoriesList(summaryList);
    }

    public void getBeforeStoriesFail() {
        contentListAdapter.getBeforeStoriesFail();
    }

    Handler mHandler = new Handler();

    public void scrollToTop() {
        contentList.smoothScrollToPosition(0);
        mHandler.postDelayed(runnable, 100);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (linearLayoutManager.findFirstVisibleItemPosition() < 3) {
                return;
            }
            contentList.stopScroll();
            contentList.scrollToPosition(1);
            contentList.smoothScrollToPosition(0);
        }
    };

    public void setDailyNews(DailyNews dailyNews) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        List<View> topStoryList = new ArrayList<>();
        for (final TopStory topStory: dailyNews.getTop_stories()) {
            final View item = inflater.inflate(R.layout.top_story_item, topStoriesViewPager.getPagesRoot(), false);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), StoryDetailActivity.class);
                    intent.putExtra("Detail", topStory.getId());
                    getContext().startActivity(intent);
                }
            });
            ((TextView)item.findViewById(R.id.title)).setText(topStory.getTitle());
            NetworkUtil.getImage(topStory.getImage(),
                    ImageResponseHandlerFactory.createHandler(
                            new BitmapContainer() {
                                @Override
                                public void setBitmap(Bitmap bitmap) {
                                    ((ImageView) item.findViewById(R.id.image)).setImageBitmap(bitmap);
                                }

                                @Override
                                public Bitmap getBitmap() {
                                    return null;
                                }
                            }, null));
            topStoryList.add(item);
        }
        PagerAdapter topStoriesAdapter = new TopStoriesAdapter(topStoryList);
        topStoriesViewPager.setAdapter(topStoriesAdapter);
        contentListAdapter.addStoriesList(dailyNews.getStories());
    }
}
