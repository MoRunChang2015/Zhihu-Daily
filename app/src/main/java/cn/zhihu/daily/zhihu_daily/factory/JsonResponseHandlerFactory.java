package cn.zhihu.daily.zhihu_daily.factory;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

/**
 * Created by morc on 16-12-10.
 */

public class JsonResponseHandlerFactory {
    static public <T> TextHttpResponseHandler createHandler(final Class<T> className, final Handler handler, final int SUCCESS_CODE, final int FAIL_CODE, final int JSON_PARSE_ERROR) {
        return new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Message msg = new Message();
                msg.what = FAIL_CODE;
                handler.sendMessage(msg);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                T temp;
                try {
                    temp = JSON.parseObject(responseString, className);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = JSON_PARSE_ERROR;
                    handler.sendMessage(msg);
                    return;
                }

                Message msg = new Message();
                msg.obj = temp;
                msg.what = SUCCESS_CODE;
                handler.sendMessage(msg);
            }
        };
    }
}
