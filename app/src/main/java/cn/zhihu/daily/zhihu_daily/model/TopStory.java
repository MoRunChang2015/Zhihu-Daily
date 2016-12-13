package cn.zhihu.daily.zhihu_daily.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;

/**
 * Created by morc on 16-12-9.
 */

public class TopStory implements BitmapContainer, Parcelable {
    private String title;
    private String image;
    private Bitmap bitmap;
    private int type;
    private int id;

    public TopStory() {}

    public TopStory(Parcel in) {
        title = in.readString();
        image = in.readString();
        type = in.readInt();
        id = in.readInt();
    }

    public static final Creator<TopStory> CREATOR = new Creator<TopStory>() {
        @Override
        public TopStory createFromParcel(Parcel in) {
            return new TopStory(in);
        }

        @Override
        public TopStory[] newArray(int size) {
            return new TopStory[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeInt(type);
        parcel.writeInt(id);
    }
}
