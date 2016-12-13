package cn.zhihu.daily.zhihu_daily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.Summary;
import cn.zhihu.daily.zhihu_daily.ui.activity.StoryDetailActivity;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by tommy on 12/12/16.
 */

public class StoriesListAdapter extends RecyclerView.Adapter<StoryListItemViewHolder> {

    private Context context;
    private List<Summary> contentList;

    public StoriesListAdapter(Context context, List<Summary> contentList) {
        this.contentList = contentList;
        this.context = context;
    }

    @Override
    public StoryListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View storyListItem = inflater.inflate(R.layout.story_list_item, parent, false);
        return new StoryListItemViewHolder(context, storyListItem);
    }

    @Override
    public void onBindViewHolder(final StoryListItemViewHolder holder, int position) {
        Summary item = contentList.get(position);
        holder.textView.setText(item.getTitle());
        holder.id = contentList.get(position).getId();
        if (item.getBitmap() != null) {
            holder.imageView.setImageBitmap(item.getBitmap());
        } else {
            NetworkUtil.getImage(item.getImages().get(0),
                    ImageResponseHandlerFactory.createHandler(holder.imageView, item));
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
    int id;

    StoryListItemViewHolder(final Context context, View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.image);
        textView = (TextView)itemView.findViewById(R.id.title);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoryDetailActivity.class);
                Log.i("Adapter", Integer.toString(id));
                intent.putExtra("Detail", id);
                context.startActivity(intent);
            }
        });
    }
}
