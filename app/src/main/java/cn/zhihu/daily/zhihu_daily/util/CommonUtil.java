package cn.zhihu.daily.zhihu_daily.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by tommy on 12/10/16.
 */

public class CommonUtil {
    private View rootView;

    public CommonUtil(View rootView) {
        this.rootView = rootView;
    }

    public void promtMsg(String text) {
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).show();
    }
}
