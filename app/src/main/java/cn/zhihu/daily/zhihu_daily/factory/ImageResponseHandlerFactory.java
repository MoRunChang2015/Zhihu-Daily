package cn.zhihu.daily.zhihu_daily.factory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.BinaryHttpResponseHandler;

import java.util.Arrays;

import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;
import cn.zhihu.daily.zhihu_daily.Interface.OnImageDownloaded;
import cz.msebera.android.httpclient.Header;

/**
 * Created by tommy on 12/10/16.
 */

public class ImageResponseHandlerFactory {
    static public BinaryHttpResponseHandler createHandler(final OnImageDownloaded onImageDownloaded) {
        return new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                if (imageBitmap.getHeight() > 600 && imageBitmap.getHeight() < 1500) {
                    imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), 550);
                    Log.i("Get Image", "");
                }
                onImageDownloaded.Do(imageBitmap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                Log.e("Download image failed", Arrays.toString(headers));
            }
        };
    }
}
