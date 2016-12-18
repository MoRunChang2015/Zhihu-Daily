package cn.zhihu.daily.zhihu_daily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cn.zhihu.daily.zhihu_daily.Interface.OnListMovedToEnd;
import cn.zhihu.daily.zhihu_daily.Interface.OnScrollingStateChanged;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.ui.activity.StoryDetailActivity;
import cn.zhihu.daily.zhihu_daily.ui.view.ViewPagerWithIndicator;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by tommy on 12/12/16.
 */

public class StoriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnScrollingStateChanged {

    private static final int TYPE_TOP_STORY = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_DATE = 2;

    public static final int SCROLL_UP = 1;
    public static final int SCROLL_DOWN = -1;

    private Context context;
    private List<Summary> contentList;
    private Calendar calendar = Calendar.getInstance();

    private String currentShowingDate;
    private ViewPagerWithIndicator topStores;

    private OnListMovedToEnd onListMovedToEnd;

    private int scrollingState = 0;

    public void addBeforeStoriesList(List<Summary> list) {
        contentList.addAll(list);
        notifyDataSetChanged();
    }

    public String getCurrentShowingDate() {
        return currentShowingDate;
    }

    public void setCurrentShowingDate(String currentShowingDate) {
        this.currentShowingDate = currentShowingDate;
    }

    public StoriesListAdapter(Context context, ViewPagerWithIndicator topStores,
                              List<Summary> contentList, OnListMovedToEnd onListMovedToEnd) {
        this.topStores = topStores;
        this.contentList = contentList;
        this.context = context;
        this.onListMovedToEnd= onListMovedToEnd;
        getCurrentShowingDate();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP_STORY;
        }
        if (contentList.get(position).getDate() == null) {
            return TYPE_ITEM;
        } else {
            return TYPE_DATE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_TOP_STORY) {
            return new TopStoriesViewHolder(topStores);
        }
        if (viewType == TYPE_ITEM) {
            View storyListItem = inflater.inflate(R.layout.story_list_item, parent, false);
            return new StoryListItemViewHolder(context, storyListItem);
        }
        View storyListDate = inflater.inflate(R.layout.story_list_date, parent, false);
        return new StoryListDateViewHolder(storyListDate);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Summary item = contentList.get(position);
        if (holder.getItemViewType() == TYPE_ITEM) {
            StoryListItemViewHolder itemViewHolder = (StoryListItemViewHolder) holder;
            itemViewHolder.textView.setText(item.getTitle());
            itemViewHolder.id = item.getId();
            if (item.getBitmap() != null) {
                itemViewHolder.imageView.setImageBitmap(item.getBitmap());
            } else {
                NetworkUtil.getImage(item.getImages().get(0),
                        ImageResponseHandlerFactory.createHandler(itemViewHolder.imageView, item));
            }
            if (contentList.size() - 1 == position) {
                onListMovedToEnd.onEnd(Integer.toString(calendar.get(Calendar.YEAR)) +
                        Integer.toString(calendar.get(Calendar.MONTH) + 1) +
                        Integer.toString(calendar.get(Calendar.DATE)));
            }
            if (scrollingState == SCROLL_UP) {
                if (contentList.get(position - 4).getDate() != null) {
                    calendar.add(Calendar.DATE, -1);
                }
            }
            if (scrollingState == SCROLL_DOWN) {
                if (contentList.get(position + 4).getDate() != null) {
                    calendar.add(Calendar.DATE, 1);
                }
            }
        } else if (holder.getItemViewType() == TYPE_DATE) {
            StoryListDateViewHolder dateViewHolder = (StoryListDateViewHolder)holder;
            dateViewHolder.textView.setText(item.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    @Override
    public void setScrollingState(int state) {
        scrollingState = state;
    }
}

class TopStoriesViewHolder extends RecyclerView.ViewHolder {
    TopStoriesViewHolder(View itemView) {
        super(itemView);
    }
}

class StoryListItemViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;
    int id;

    StoryListItemViewHolder(final Context context, View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.image);
        textView = (TextView)itemView.findViewById(R.id.title);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoryDetailActivity.class);
                intent.putExtra("Detail", id);
                context.startActivity(intent);
            }
        });
    }
}

class StoryListDateViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    StoryListDateViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.date);
    }
}
