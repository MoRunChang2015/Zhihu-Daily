package cn.zhihu.daily.zhihu_daily.Interface;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

/**
 * Created by tommy on 12/13/16.
 */

public interface BitmapContainer {
    void setBitmap(Bitmap bitmap, int id);
    void setBitmap(Bitmap bitmap);
    Bitmap getBitmap();
}
