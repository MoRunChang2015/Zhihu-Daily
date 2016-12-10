package cn.zhihu.daily.zhihu_daily.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.BinaryHttpResponseHandler;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

/**
 * Created by tommy on 12/10/16.
 */

public class ImageResponseHandlerFactory {
    static public BinaryHttpResponseHandler createHandler(final ImageView imageContainer) {
        return new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                imageContainer.setImageBitmap(imageBitmap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                Log.e("Download image failed", Arrays.toString(headers));
            }
        };
    }
}
