package cn.zhihu.daily.zhihu_daily.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by tommy on 12/7/16.
 */

public class TopStoriesAdapter extends PagerAdapter {

    private List<View> contentList;

    public TopStoriesAdapter(List<View> contentList) {
        this.contentList = contentList;
    }

    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(contentList.get(position));
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(contentList.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == contentList.get((Integer) object);
    }
}
