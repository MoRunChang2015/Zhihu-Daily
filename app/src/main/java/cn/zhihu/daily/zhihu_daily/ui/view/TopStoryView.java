package cn.zhihu.daily.zhihu_daily.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.zhihu.daily.zhihu_daily.R;

/**
 * Created by tommy on 12/7/16.
 */

public class TopStoryView extends Fragment implements ViewPager.OnPageChangeListener {

    ViewPager m_viewPager;
    LinearLayout m_indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View topStoryView = inflater.inflate(R.layout.top_story_view, container, false);
        m_viewPager = (ViewPager)topStoryView.findViewById(R.id.top_story);
        m_indicator = (LinearLayout)topStoryView.findViewById(R.id.top_story_indicator);
        return topStoryView;
    }

    // public void setAdaptor()

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
