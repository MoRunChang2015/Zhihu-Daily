package cn.zhihu.daily.zhihu_daily.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.File;
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

    public ImageProvider(Context context) {
        imagePath = context.getFilesDir().getPath() + "/images/";
    }
    public void loadImage(String url,
                          final BitmapContainer imageContainer,
                          @Nullable final Integer id,
                          @Nullable final BitmapContainer bitmapContainer) {
        String md5 = "";
        try {
            md5 = CommonUtil.getMD5(url);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath + md5);
            if (bitmap == null)
                throw new NullPointerException();
            //Log.d("ImageProvider", "load image " + md5 + " success!");
            if (id != null) {
                imageContainer.setBitmap(bitmap, id);
            } else {
                imageContainer.setBitmap(bitmap);
            }
            if (bitmapContainer != null) {
                bitmapContainer.setBitmap(bitmap);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            final String finalMd = md5;
            NetworkUtil.getImage(url,
                    ImageResponseHandlerFactory.createHandler(new OnImageDownloaded() {
                        @Override
                        public void Do(Bitmap bitmap) {
                            if (id != null) {
                                imageContainer.setBitmap(bitmap, id);
                            } else {
                                imageContainer.setBitmap(bitmap);
                            }
                            if (bitmapContainer != null) {
                                bitmapContainer.setBitmap(bitmap);
                            }
                            try (FileOutputStream out = new FileOutputStream(new File(imagePath, finalMd))) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }));
        }

    }
}
