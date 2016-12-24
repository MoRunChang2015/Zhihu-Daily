package cn.zhihu.daily.zhihu_daily.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileOutputStream;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;
import cn.zhihu.daily.zhihu_daily.Interface.OnImageDownloaded;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.util.CommonUtil;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

/**
 * Created by tommy on 12/23/16.
 */

public class ImageProvider {
    private String imagePath;

    private Context context;
    public ImageProvider(Context context) {
        this.context = context;
        imagePath = context.getFilesDir().getPath() + "/";
    }
    public void loadImage(String url,
                                 final BitmapContainer imageContainer,
                                 @Nullable final BitmapContainer bitmapContainer,
                                 @Nullable final Integer id) {
        String md5 = "1";
        try {
            md5 = CommonUtil.getMD5(url);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath + md5);
            if (bitmap == null)
                throw new NullPointerException();
            Log.d("ImageProvider", "load image " + md5 + " success!");
            imageContainer.setBitmap(bitmap);
            if (id != null) {
                bitmapContainer.setBitmap(bitmap, id);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            final String finalMd = md5;
            NetworkUtil.getImage(url,
                    ImageResponseHandlerFactory.createHandler(new OnImageDownloaded() {
                        @Override
                        public void Do(Bitmap bitmap) {
                            imageContainer.setBitmap(bitmap);
                            if (id != null) {
                                bitmapContainer.setBitmap(bitmap, id);
                            }
                            try (FileOutputStream out = context.openFileOutput(finalMd, Context.MODE_PRIVATE)) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }));
        }

    }
}
