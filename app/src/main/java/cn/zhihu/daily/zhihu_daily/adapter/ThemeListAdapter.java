package cn.zhihu.daily.zhihu_daily.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.model.Theme;

/**
 * Created by morc on 16-12-15.
 */

public class ThemeListAdapter extends RecyclerView.Adapter<ThemeListAdapter.ViewHolder> {

    private List<Theme> data;
    private OnItemClickListener onItemClickListener = null;
    private int currentThemeId;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(View view, Object data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ThemeListAdapter(Context context, List<Theme> data, int currentThemeId) {
        this.data = data;
        this.currentThemeId = currentThemeId;
        this.context = context;
    }

    public void setCurrentThemeId(int currentThemeId) {
        this.currentThemeId = currentThemeId;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position).getName());

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, data.get(holder.getAdapterPosition()));
                }
            });
        }
        if (data.get(position).getId() == currentThemeId) {
            ((LinearLayout) holder.textView.getParent()).setBackgroundColor(
                    ContextCompat.getColor(context, R.color.sideMenuSelectedColor));
            holder.textView.setTextColor(Color.WHITE);
        } else {
            ((LinearLayout) holder.textView.getParent()).setBackgroundColor(
                    ContextCompat.getColor(context, R.color.sideMenuColor));
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.sideMenuBorderColor));
        }

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.theme_name_text);
        }
    }
}
