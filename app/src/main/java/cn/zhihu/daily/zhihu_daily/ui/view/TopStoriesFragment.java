package cn.zhihu.daily.zhihu_daily.ui.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.adapter.TopStoriesAdapter;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.TopStory;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by tommy on 12/7/16.
 */

public class TopStoriesFragment extends Fragment implements ViewPager.OnPageChangeListener {

    ViewPager m_viewPager;
    LinearLayout m_indicator;
    View topStoriesView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        topStoriesView = inflater.inflate(R.layout.top_stories_view, container, false);
        m_viewPager = (ViewPager)topStoriesView.findViewById(R.id.top_story);
        m_indicator = (LinearLayout)topStoriesView.findViewById(R.id.top_story_indicator);
        return topStoriesView;
    }

    public void setContent(Context context, List<TopStory> contentList) {
        LayoutInflater inflater = LayoutInflater.from(context);
        List<View> topStoryViewList = new ArrayList<>();
        for (TopStory topStory : contentList) {
            View topStoryItem = inflater.inflate(R.layout.top_story_item, null);
            ((TextView)topStoryItem.findViewById(R.id.title)).setText(topStory.getTitle());
            NetworkUtil.getImage(topStory.getImage(),
                    ImageResponseHandlerFactory.createHandler(
                            (ImageView) topStoryItem.findViewById(R.id.image)));
            topStoryViewList.add(topStoryItem);
        }
        m_viewPager.setAdapter(new TopStoriesAdapter(topStoryViewList));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
