package cn.zhihu.daily.zhihu_daily.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by morc on 16-12-7.
 */

public class Summary {
    private String title;
    private List<String> images;
    private Bitmap image;
    private int id;
    private int type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
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
