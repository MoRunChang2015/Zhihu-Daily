package cn.zhihu.daily.zhihu_daily.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;
import cn.zhihu.daily.zhihu_daily.Interface.ExtendStoriesListHandler;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.service.ImageProvider;
import cn.zhihu.daily.zhihu_daily.ui.activity.StoryDetailActivity;
import cn.zhihu.daily.zhihu_daily.ui.view.ViewPagerWithIndicator;
import cn.zhihu.daily.zhihu_daily.util.CommonUtil;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by tommy on 12/12/16.
 */

public class StoriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOP_STORY = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_DATE = 2;
    private static final int TYPE_LOADING = 3;
    private static Calendar today = null;

    private Context context;
    private List<Summary> contentList;
    private Calendar calendar = Calendar.getInstance();

    private Boolean isLoading = false;
    private String loadingDate;
    private ViewPagerWithIndicator topStores;
    private ExtendStoriesListHandler extendStoriesListHandler;
    private ImageProvider imageProvider;

    public StoriesListAdapter(Context context, ViewPagerWithIndicator topStores,
                              ExtendStoriesListHandler extendStoriesListHandler) {
        this.context = context;
        this.topStores = topStores;
        contentList = new ArrayList<>();
        this.extendStoriesListHandler = extendStoriesListHandler;
        isLoading = true;
        imageProvider = new ImageProvider(context);
    }

    public StoriesListAdapter(Context context, ViewPagerWithIndicator topStores,
                              List<Summary> contentList, ExtendStoriesListHandler extendStoriesListHandler) {
        this.topStores = topStores;
        this.contentList = contentList;
        this.context = context;
        this.extendStoriesListHandler = extendStoriesListHandler;
        today = formatDateToCalendar(contentList.get(0).getDate());
    }

    public void addStoriesList(List<Summary> list) {
        isLoading = false;

        contentList.addAll(list);

        if (today == null)
            today = formatDateToCalendar(contentList.get(0).getDate());
        notifyDataSetChanged();
    }


    public void getBeforeStoriesFail() {
        extendStoriesListHandler.onEnd(loadingDate);
    }

    private String getCurrentShowingDate() {
        if (calendar.before(today))
            return Integer.toString(calendar.get(Calendar.MONTH) + 1) + "月" +
                    Integer.toString(calendar.get(Calendar.DATE)) + "日";
        else
            return "今日";
    }

    private String getCurrentFormatDate() {
        return Integer.toString(calendar.get(Calendar.YEAR)) +
                (calendar.get(Calendar.MONTH) + 1 < 10 ? "0":"") +
                Integer.toString(calendar.get(Calendar.MONTH) + 1) +
                (calendar.get(Calendar.DATE) < 10 ? "0":"") +
                Integer.toString(calendar.get(Calendar.DATE));
    }

    private String FormatDateToShowingDate(String date) {
        return date.substring(0, 4) + "年" +
                Integer.toString(Integer.parseInt(date.substring(4, 6))) + "月" +
                Integer.toString(Integer.parseInt(date.substring(6, 8))) + "日";
    }

    private Calendar formatDateToCalendar(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 && getItemCount() > 1) {
            return TYPE_TOP_STORY;
        }
        if (position == getItemCount() - 1) {
            return TYPE_LOADING;
        }
        if (contentList.get(position).getType() == Constant.ITEM_DATE_TYPE) {
            return TYPE_DATE;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            View storyListItem = inflater.inflate(R.layout.story_list_item, parent, false);
            return new StoryListItemViewHolder(context, storyListItem);
        }
        if (viewType == TYPE_DATE) {
            View storyListDate = inflater.inflate(R.layout.story_list_date, parent, false);
            return new StoryListDateViewHolder(storyListDate);
        }
        if (viewType == TYPE_TOP_STORY) {
            return new TopStoriesViewHolder(topStores);
        }

        View loadingItem = inflater.inflate(R.layout.story_list_loading, parent, false);
        return new LoadingViewHolder(loadingItem);
    }

    private void checkDateChange() {
        int firstPosition = extendStoriesListHandler.getFirstVisibleItemPosition();
        if (firstPosition == -1)
            return;
        if (contentList.get(firstPosition).getDate() != null) {
            Calendar temp = formatDateToCalendar(contentList.get(firstPosition).getDate());
            if (calendar.before(temp) || calendar.after(temp)) {
                calendar.setTime(temp.getTime());
                extendStoriesListHandler.onDateChange(getCurrentShowingDate());
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            return;
        }
        Summary item = contentList.get(position);
        if (holder.getItemViewType() == TYPE_ITEM) {
            StoryListItemViewHolder itemViewHolder = (StoryListItemViewHolder) holder;
            itemViewHolder.textView.setText(item.getTitle());
            itemViewHolder.id = item.getId();
            if (item.getBitmap() != null) {
                itemViewHolder.imageView.setImageBitmap(item.getBitmap());
            } else {
                itemViewHolder.imageView.setImageResource(R.color.mainActivityBackground);
                imageProvider.loadImage(item.getImages().get(0), itemViewHolder, item, itemViewHolder.id);
            }
            if (contentList.size() - 10 == position && !isLoading) {
                isLoading = true;
                loadingDate = getCurrentFormatDate();
                extendStoriesListHandler.onEnd(loadingDate);
            }
            checkDateChange();
        } else if (holder.getItemViewType() == TYPE_DATE) {
            StoryListDateViewHolder dateViewHolder = (StoryListDateViewHolder)holder;
            String showingText = FormatDateToShowingDate(item.getDate()) + "的大新闻";
            dateViewHolder.textView.setText(showingText);
        }
    }

    @Override
    public int getItemCount() {
        return contentList.size() + 1;
    }
}

class LoadingViewHolder extends RecyclerView.ViewHolder {
    LoadingViewHolder(View itemView) {
        super(itemView);
    }
}

class TopStoriesViewHolder extends RecyclerView.ViewHolder {
    TopStoriesViewHolder(View itemView) {
        super(itemView);
    }
}

class StoryListItemViewHolder extends RecyclerView.ViewHolder implements BitmapContainer {

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

    StoryListItemViewHolder getSelf() {
        return this;
    }

    @Override
    public void setBitmap(Bitmap bitmap, int id) {
        if (this.id == id) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        // do nothing
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}

class StoryListDateViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    StoryListDateViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.date);
    }
}
