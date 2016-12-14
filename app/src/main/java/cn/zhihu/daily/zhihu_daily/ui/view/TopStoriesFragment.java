package cn.zhihu.daily.zhihu_daily.ui.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import cn.zhihu.daily.zhihu_daily.ui.activity.StoryDetailActivity;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by tommy on 12/7/16.
 */

public class TopStoriesFragment extends Fragment implements ViewPager.OnPageChangeListener {

    final String tag = "TopStoriesFragment";

    ViewPager m_viewPager;

    LinearLayout m_indicatorLayout;
    List<ImageView> m_indicatorNormal = new ArrayList<>();

    View topStoriesView;

    int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        topStoriesView = inflater.inflate(R.layout.top_stories_view, container, false);
        m_viewPager = (ViewPager)topStoriesView.findViewById(R.id.top_story);
        m_indicatorLayout = (LinearLayout)topStoriesView.findViewById(R.id.top_story_indicator);

        m_viewPager.addOnPageChangeListener(this);

        return topStoriesView;
    }

    public void setContent(final Context context, List<TopStory> contentList) {
        LayoutInflater inflater = LayoutInflater.from(context);
        List<View> topStoryViewList = new ArrayList<>();
        for (final TopStory topStory : contentList) {
            final View topStoryItem = inflater.inflate(R.layout.top_story_item, m_viewPager, false);

            topStoryItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StoryDetailActivity.class);
                    intent.putExtra("Detail", topStory.getId());
                    startActivity(intent);
                }
            });

            ((TextView)topStoryItem.findViewById(R.id.title)).setText(topStory.getTitle());
            NetworkUtil.getImage(topStory.getImage(),
                    ImageResponseHandlerFactory.createHandler(
                            (ImageView) topStoryItem.findViewById(R.id.image), topStory));
            topStoryViewList.add(topStoryItem);
        }

        int indicatorNum = m_indicatorLayout.getChildCount();
        while (indicatorNum != contentList.size()) {
            if (indicatorNum > contentList.size()) {
                m_indicatorLayout.removeViewAt(indicatorNum - 1);
            } else if (indicatorNum < contentList.size()) {
                View indicator = inflater.inflate(R.layout.indicator, m_indicatorLayout, false);
                m_indicatorLayout.addView(indicator);
            }
            indicatorNum = m_indicatorLayout.getChildCount();
        }

        ((ImageView)m_indicatorLayout.getChildAt(0)).setImageResource(R.drawable.indicator_selected);

        m_viewPager.setAdapter(new TopStoriesAdapter(topStoryViewList));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Log.i("Scroll", Integer.toString(position) + " " + Float.toString(positionOffset) + " " + Integer.toString(positionOffsetPixels));
    }

    @Override
    public void onPageSelected(int position) {
        // Log.i("Select", Integer.toString(position));
        ((ImageView)m_indicatorLayout.getChildAt(currentPage)).setImageResource(R.drawable.indicator_normal);
        currentPage = position;
        ((ImageView)m_indicatorLayout.getChildAt(currentPage)).setImageResource(R.drawable.indicator_selected);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Log.i("Change", Integer.toString(state));
    }
}
