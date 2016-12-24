package cn.zhihu.daily.zhihu_daily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.model.ThemeNews;
import cn.zhihu.daily.zhihu_daily.service.ImageProvider;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by morc on 16-12-20.
 */

public class ThemeStoriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TOP_IMAGE = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private static final int TYPE_ITEM_WITHOUT_IMAGE = 3;

    private Context context;
    private List<Summary> contentList;
    private TopImageViewHolder topImageViewHolder = null;
    private String topImageUrl;
    private String topImageDescription;
    private ImageProvider imageProvider;

    public ThemeStoriesListAdapter(Context context) {
        contentList = new ArrayList<>();
        this.context = context;
        imageProvider = new ImageProvider(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 1 && position == 0) {
            return TYPE_LOADING;
        }
        if (position == 0 && getItemCount() > 1) {
            return TYPE_TOP_IMAGE;
        }
        if (contentList.get(position).getImages() == null)
            return TYPE_ITEM_WITHOUT_IMAGE;

        return TYPE_ITEM;
    }

    public void setThemeNewsList(ThemeNews themeNews) {
        contentList.addAll(themeNews.getStories());
        topImageUrl = themeNews.getBackground();
        topImageDescription = themeNews.getDescription();
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            View storyListItem = inflater.inflate(R.layout.story_list_item, parent, false);
            return new StoryListItemViewHolder(context, storyListItem);
        }
        if (viewType == TYPE_ITEM_WITHOUT_IMAGE) {
            View storyListItem = inflater.inflate(R.layout.story_list_item, parent, false);
            StoryListItemViewHolder temp = new StoryListItemViewHolder(context, storyListItem);
            ViewGroup viewgroup = (ViewGroup)temp.imageView.getParent();
            viewgroup.removeView(temp.imageView);
            temp.imageView = null;
            return temp;
        }
        if (viewType == TYPE_TOP_IMAGE) {
            if (topImageViewHolder == null) {
                View topImageView = inflater.inflate(R.layout.top_story_item, parent, false);
                topImageViewHolder = new TopImageViewHolder(topImageView);
                imageProvider.loadImage(topImageUrl, topImageViewHolder, null, null);
                topImageViewHolder.textView.setText(topImageDescription);
            }
            return topImageViewHolder;
        }
        View loadingItem = inflater.inflate(R.layout.story_list_loading, parent, false);
        return new LoadingViewHolder(loadingItem);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (contentList.size() == 0) {
            return;
        }
        Summary item = contentList.get(position);
        if (holder.getItemViewType() == TYPE_ITEM || holder.getItemViewType() == TYPE_ITEM_WITHOUT_IMAGE) {
            StoryListItemViewHolder itemViewHolder = (StoryListItemViewHolder) holder;
            itemViewHolder.textView.setText(item.getTitle());
            itemViewHolder.id = item.getId();
            if (item.getImages() != null) {
                if (item.getBitmap() != null) {
                    itemViewHolder.imageView.setImageBitmap(item.getBitmap());
                } else {
                    itemViewHolder.imageView.setImageResource(R.color.mainActivityBackground);
                    imageProvider.loadImage(item.getImages().get(0), itemViewHolder, item, itemViewHolder.id);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (contentList.size() == 0) {
            return 1;
        }
        return contentList.size();
    }
}

class TopImageViewHolder extends RecyclerView.ViewHolder implements BitmapContainer {
    private ImageView imageView;
    TextView textView;

    TopImageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.image);
        textView = (TextView)itemView.findViewById(R.id.title);
    }

    @Override
    public void setBitmap(Bitmap bitmap, int id) {
        // do nothing
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}


