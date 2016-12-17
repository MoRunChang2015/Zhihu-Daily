package cn.zhihu.daily.zhihu_daily.ui.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.zhihu.daily.zhihu_daily.R;

/**
 * Created by tommy on 12/16/16.
 */

public class ViewPagerWithIndicator extends RelativeLayout implements ViewPager.OnPageChangeListener {

    Context context;

    ViewPager m_viewPager;
    LinearLayout m_indicatorLayout;

    int currentPage = 0;

    public ViewPagerWithIndicator(Context context) {
        this(context, null, 0, 0);
    }

    public ViewPagerWithIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public ViewPagerWithIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ViewPagerWithIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.top_stories_view, this);
        m_viewPager = (ViewPager)findViewById(R.id.top_story);
        m_indicatorLayout = (LinearLayout)findViewById(R.id.top_story_indicator);
        m_viewPager.addOnPageChangeListener(this);
    }

    public void setAdapter(PagerAdapter adapter) {
        int indicatorNum = m_indicatorLayout.getChildCount();
        LayoutInflater inflater = LayoutInflater.from(context);
        while (indicatorNum != adapter.getCount()) {
            if (indicatorNum > adapter.getCount()) {
                m_indicatorLayout.removeViewAt(indicatorNum - 1);
            } else if (indicatorNum < adapter.getCount()) {
                View indicator = inflater.inflate(R.layout.indicator, m_indicatorLayout, false);
                m_indicatorLayout.addView(indicator);
            }
            indicatorNum = m_indicatorLayout.getChildCount();
        }
        if (indicatorNum > currentPage) {
            ((ImageView)m_indicatorLayout.getChildAt(currentPage)).setImageResource(R.drawable.indicator_normal);
        }
        currentPage = 0;
        ((ImageView)m_indicatorLayout.getChildAt(0)).setImageResource(R.drawable.indicator_selected);
        m_viewPager.setAdapter(adapter);
    }

    public ViewGroup getPagesRoot() {
        return m_viewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((ImageView)m_indicatorLayout.getChildAt(currentPage)).setImageResource(R.drawable.indicator_normal);
        currentPage = position;
        ((ImageView)m_indicatorLayout.getChildAt(currentPage)).setImageResource(R.drawable.indicator_selected);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
