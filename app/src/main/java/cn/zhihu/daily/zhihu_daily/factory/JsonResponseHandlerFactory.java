package cn.zhihu.daily.zhihu_daily.factory;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.TextHttpResponseHandler;

import javax.xml.transform.Templates;

import cn.zhihu.daily.zhihu_daily.constant.Constant;
import cn.zhihu.daily.zhihu_daily.model.Detail;
import cz.msebera.android.httpclient.Header;

/**
 * Created by morc on 16-12-10.
 */

public class JsonResponseHandlerFactory {
    static public <T> TextHttpResponseHandler createHandler(final Class<T> className, final Handler handler, final int SUCCESS_CODE, final int FAIL_CODE) {
        return new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Message msg = new Message();
                msg.what = FAIL_CODE;
                handler.sendMessage(msg);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                T temp = null;
                try {
                    temp = JSON.parseObject(responseString, className);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAIL_CODE;
                    handler.sendMessage(msg);
                }
                Message msg = new Message();
                if (temp != null) {
                    msg.obj = temp;
                    msg.what = SUCCESS_CODE;
                } else {
                    msg.what = FAIL_CODE;
                }
                handler.sendMessage(msg);
            }
        };
    }
}
