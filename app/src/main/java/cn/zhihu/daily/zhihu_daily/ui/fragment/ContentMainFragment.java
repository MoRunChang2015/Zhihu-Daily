package cn.zhihu.daily.zhihu_daily.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zhihu.daily.zhihu_daily.Interface.OnListMovedToEnd;
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
    OnListMovedToEnd listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentList = (RecyclerView) getActivity().findViewById(R.id.story_list);
        contentList.setLayoutManager(new LinearLayoutManager(getContext()));
        contentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollingDirection = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && scrollingDirection != 1) {
                    scrollingDirection = StoriesListAdapter.SCROLL_UP;
                    contentListAdapter.setScrollingState(scrollingDirection);
                    Log.i("Scrolling state", scrollingDirection > 0 ? "up" : "down");
                } else if (dy < 0 && scrollingDirection != -1) {
                    scrollingDirection = StoriesListAdapter.SCROLL_DOWN;
                    contentListAdapter.setScrollingState(scrollingDirection);
                    Log.i("Scrolling state", scrollingDirection > 0 ? "up" : "down");
                }
            }
        });
        topStoriesViewPager = new ViewPagerWithIndicator(getContext());
    }

    public void setOnListMovedToEndListener(OnListMovedToEnd listener) {
        this.listener = listener;
    }

    public void setTopStory(List<TopStory> topStoryList) {

    }

    public void addSummary(List<Summary> summaryList) {
        contentListAdapter.addBeforeStoriesList(summaryList);
    }

    public void setDailyNews(DailyNews dailyNews) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        List<View> topStoryList = new ArrayList<>();
        for (final TopStory topStory: dailyNews.getTop_stories()) {
            View item = inflater.inflate(R.layout.top_story_item, topStoriesViewPager.getPagesRoot(), false);
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
                            (ImageView) item.findViewById(R.id.image), null));
            topStoryList.add(item);
        }
        PagerAdapter topStoriesAdapter = new TopStoriesAdapter(topStoryList);
        topStoriesViewPager.setAdapter(topStoriesAdapter);
        contentListAdapter = new StoriesListAdapter(
                getContext(), topStoriesViewPager, dailyNews.getStories(), listener);
        contentList.setAdapter(contentListAdapter);

    }
}
