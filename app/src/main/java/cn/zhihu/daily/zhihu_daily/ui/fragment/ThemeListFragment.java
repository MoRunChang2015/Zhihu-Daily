package cn.zhihu.daily.zhihu_daily.ui.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.adapter.ThemeListAdapter;
import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.model.Theme;
import cn.zhihu.daily.zhihu_daily.model.ThemeList;

public class ThemeListFragment extends Fragment {
    private RecyclerView themeRecyclerView;
    final Theme home = new Theme();
    private ThemeListAdapter themeListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        themeRecyclerView = (RecyclerView)view.findViewById(R.id.theme_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        themeRecyclerView.setLayoutManager(layoutManager);
    }

    public void setThemeList(ThemeList themeList, final Handler handler) {
        home.setName("首页");
        home.setId(Constant.THEME_HOME_ID);
        List<Theme> data = themeList.getOthers();
        data.add(0, home);
        themeListAdapter = new ThemeListAdapter(getActivity(), data, Constant.THEME_HOME_ID);
        themeRecyclerView.setAdapter(themeListAdapter);
        themeListAdapter.setOnItemClickListener(new ThemeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                Theme theme = (Theme)data;
                themeListAdapter.setCurrentThemeId(theme.getId());
                Message message = new Message();
                message.what = Constant.THEME_CHANGE;
                message.obj = theme;
                handler.sendMessage(message);
            }
        });
    }
}
