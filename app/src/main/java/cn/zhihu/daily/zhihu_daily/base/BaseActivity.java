package cn.zhihu.daily.zhihu_daily.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        setLayout();
        setContentView(setLayout());
        initViews(savedInstanceState);
    }

    protected void initVariables() {
        //TODO: initialize Bmob and ConetentHelper
    }

    protected abstract int setLayout();

    protected abstract void initViews(Bundle savedInstanceState);
}
