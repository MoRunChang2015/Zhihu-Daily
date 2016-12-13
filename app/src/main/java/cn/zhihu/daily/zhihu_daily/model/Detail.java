package cn.zhihu.daily.zhihu_daily.model;

import android.graphics.Bitmap;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;

/**
 * Created by morc on 16-12-7.
 */

public class Detail implements BitmapContainer {
    private String body;
    private String image_source;
    private String title;
    private String image;
    private Bitmap bitmap;
    private int type;
    private int id;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

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

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
