package cn.zhihu.daily.zhihu_daily.model;

import java.util.List;

/**
 * Created by morc on 16-12-10.
 */

public class ThemeList {
    private int limit;
    private List<Theme> subscribed;
    private List<Theme> others;

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setSubscribed(List<Theme> subscribed) {
        this.subscribed = subscribed;
    }

    public List<Theme> getSubscribed() {
        return subscribed;
    }

    public void setOthers(List<Theme> others) {
        this.others = others;
    }

    public List<Theme> getOthers() {
        return others;
    }
}
