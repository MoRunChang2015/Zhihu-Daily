package cn.zhihu.daily.zhihu_daily.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.model.TopStory;
import cn.zhihu.daily.zhihu_daily.service.ImageProvider;
import cn.zhihu.daily.zhihu_daily.ui.activity.StoryDetailActivity;

/**
 * Created by tommy on 12/7/16.
 */

public class TopStoriesAdapter extends PagerAdapter {

    private Context context;
    private ViewPager viewGroup;

    private List<TopStory> topStoryList = new ArrayList<>();

    private List<View> containers = new ArrayList<>();
    private Queue<Integer> availableViews = new LinkedList<>();

    private ImageProvider imageProvider;

    public TopStoriesAdapter(Context context, ViewGroup root) {
        this.viewGroup = (ViewPager) root;
        imageProvider = new ImageProvider(context);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(this.context);
        for (int i = 0; i < 4; i++) {
            View view = inflater.inflate(R.layout.top_story_item, root, false);
            TopStoryViewHolder viewHolder = new TopStoryViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            viewHolder.textView = (TextView) view.findViewById(R.id.title);
            viewHolder.viewID = i;
            view.setTag(viewHolder);
            containers.add(view);
            availableViews.add(viewHolder.viewID);
        }
    }

    public void setContent(List<TopStory> topStoryList) {
        this.topStoryList = topStoryList;
//        removeAllViews();
//        instantiateItem(viewGroup, 0);
//        instantiateItem(viewGroup, 1);
        notifyDataSetChanged();
//        viewGroup.setCurrentItem(0);
    }

    private void removeAllViews() {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            TopStoryViewHolder viewHolder = (TopStoryViewHolder) view.getTag();
            destroyItem(viewGroup, viewHolder.position, view);
        }
    }


    @Override
    public int getCount() {
        return topStoryList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("ViewPager", "Instantiate " + position + " available " + availableViews);
        final TopStory topStory = topStoryList.get(position);

        int tobeUsed = availableViews.poll();
        View view = containers.get(tobeUsed);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoryDetailActivity.class);
                intent.putExtra("Detail", topStory.getId());
                context.startActivity(intent);
            }
        });
        TopStoryViewHolder viewHolder = (TopStoryViewHolder) view.getTag();
        viewHolder.id = topStory.getId();
        viewHolder.position = position;
        if (topStory.getBitmap() == null) {
            imageProvider.loadImage(topStory.getImage(), viewHolder, viewHolder.id, topStory);
        } else {
            viewHolder.imageView.setImageBitmap(topStory.getBitmap());
        }
        viewHolder.textView.setText(topStoryList.get(position).getTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        availableViews.add(((TopStoryViewHolder)((View) object).getTag()).viewID);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

class TopStoryViewHolder implements BitmapContainer{
    ImageView imageView;
    TextView textView;
    int viewID;
    int id;
    int position;

    @Override
    public void setBitmap(Bitmap bitmap, int id) {
        if (this.id == id) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void setBitmap(Bitmap bitmap) {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}