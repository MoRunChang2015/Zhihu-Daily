package cn.zhihu.daily.zhihu_daily.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.zhihu.daily.zhihu_daily.Interface.BitmapContainer;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.base.BaseActivity;
import cn.zhihu.daily.zhihu_daily.constant.Constant;
import cn.zhihu.daily.zhihu_daily.factory.ImageResponseHandlerFactory;
import cn.zhihu.daily.zhihu_daily.model.Detail;
import cn.zhihu.daily.zhihu_daily.service.NewsService;
import cn.zhihu.daily.zhihu_daily.util.CommonUtil;
import cn.zhihu.daily.zhihu_daily.util.NetworkUtil;

public class StoryDetailActivity extends BaseActivity {

    private CommonUtil commonUtil;
    private NewsService newsService;

    private Detail detail;

    final String tag = "StoryDetailActivity";

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.title)
    TextView titleTextView;

    @BindView(R.id.image_source)
    TextView imageSourceTextView;

    @BindView(R.id.detail_content)
    WebView contentDetailWebView;

    @BindView(R.id.activity_story_detail)
    LinearLayout linearLayout;

    @Override
    protected int setLayout() {
        return R.layout.activity_story_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        commonUtil = new CommonUtil(findViewById(R.id.activity_story_detail));
        Intent intent = new Intent(StoryDetailActivity.this, NewsService.class);
        final int id = getIntent().getIntExtra("Detail", 0);
        linearLayout.setVisibility(View.INVISIBLE);
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                newsService = ((NewsService.MyBinder) iBinder).getService();
                if (id != 0) {
                    newsService.getNewsDetail(id, handler);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                newsService = null;
            }
        };
        bindService(intent, sc, BIND_AUTO_CREATE);
    }

    private ServiceConnection sc;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case Constant.DOWNLOAD_NEWS_DETAIL_SUCCESS:
                    linearLayout.setVisibility(View.VISIBLE);
                    Detail detail = (Detail)message.obj;
                    titleTextView.setText(detail.getTitle());
                    imageSourceTextView.setText(detail.getImage_source());
                    if (detail.getImage() != null) {
                        NetworkUtil.getImage(detail.getImage(),
                                ImageResponseHandlerFactory.createHandler(new BitmapContainer() {
                                    @Override
                                    public void setBitmap(Bitmap bitmap, int id) {
                                        //do nothing
                                    }

                                    @Override
                                    public void setBitmap(Bitmap bitmap) {
                                        imageView.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public Bitmap getBitmap() {
                                        return null;
                                    }
                                }, detail, null));
                    } else {
                        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.detail_top_image_frameLayout);
                        ViewGroup viewGroup = (ViewGroup)frameLayout.getParent();
                        viewGroup.removeView(frameLayout);
                    }
                    setWebContent(detail);
                    commonUtil.promptMsg("Download news Detail Success");
                    break;
                default:
                    break;
            }
        }
    };


    private void setWebContent(Detail detail) {
        contentDetailWebView.getSettings().setJavaScriptEnabled(true);
        if (detail.getBody() != null) {
            final String NewsStyle = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
            String content;
            content = "<html><head>" + NewsStyle + "</head><body>" + detail.getBody() + "</body></html>";
            content = content.replace("<div class=\"img-place-holder\"></div>", "");
            contentDetailWebView.loadDataWithBaseURL("x-data://base", content, "text/html", "UTF-8", null);
        } else {
            contentDetailWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view,
                                                        WebResourceRequest request) {
                    return false;
                }
            });
            contentDetailWebView.loadUrl(detail.getShare_url());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sc);
    }
}