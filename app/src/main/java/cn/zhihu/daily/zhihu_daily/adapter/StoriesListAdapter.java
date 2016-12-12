package cn.zhihu.daily.zhihu_daily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.ui.activity.MainActivity;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by tommy on 12/12/16.
 */

public class StoriesListAdapter extends RecyclerView.Adapter<StoryListItemViewHolder> {

    private List<Summary> contentList;

    public StoriesListAdapter(List<Summary> contentList) {
        this.contentList = contentList;
    }

    @Override
    public StoryListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View storyListItem = inflater.inflate(R.layout.story_list_item, parent, false);
        return new StoryListItemViewHolder(storyListItem);
    }

    @Override
    public void onBindViewHolder(StoryListItemViewHolder holder, int position) {
        Summary item = contentList.get(position);
        holder.textView.setText(item.getTitle());
        if (item.getImage() != null) {
            holder.imageView.setImageBitmap(item.getImage());
        } else {
            NetworkUtil.getImage(item.getImages().get(0),
                    ImageResponseHandlerFactory.createHandler(holder.imageView));
        }
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }
}

class StoryListItemViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;

    StoryListItemViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.image);
        textView = (TextView)itemView.findViewById(R.id.title);
    }
}
