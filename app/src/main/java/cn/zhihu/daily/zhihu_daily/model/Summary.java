package cn.zhihu.daily.zhihu_daily.model;

import android.graphics.Bitmap;

import java.util.List;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;

/**
 * Created by morc on 16-12-7.
 */

public class Summary implements BitmapContainer {
    private String title;
    private List<String> images;
    private Bitmap bitmap;
    private int id;
    private int type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

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

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void setBitmap(Bitmap bitmap, int id) {
        // do nothing
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
