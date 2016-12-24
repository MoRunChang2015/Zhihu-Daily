package cn.zhihu.daily.zhihu_daily.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import cn.zhihu.daily.zhihu_daily.service.ImageProvider;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        setContentView(setLayout());
        ButterKnife.bind(this);
        initViews(savedInstanceState);
    }

    protected void initVariables() {
    }

    protected abstract int setLayout();

    protected abstract void initViews(Bundle savedInstanceState);
}
