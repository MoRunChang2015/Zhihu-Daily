package cn.zhihu.daily.zhihu_daily.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by morc on 16-12-9.
 */

public class TopStories {
    private String title;
    private String image;
    private int type;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
