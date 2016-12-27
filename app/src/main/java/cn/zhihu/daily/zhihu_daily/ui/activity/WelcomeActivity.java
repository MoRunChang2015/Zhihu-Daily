package cn.zhihu.daily.zhihu_daily.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import butterknife.BindView;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.base.BaseActivity;
import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.service.NewsService;

import static android.R.id.content;
import static android.R.id.message;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.logo)
    ImageView logo;


    private NewsService newsService;

    private ServiceConnection sc =  new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            newsService = ((NewsService.MyBinder)iBinder).getService();
            newsService.updateWelcomeImage();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            newsService = null;
        }
    };


    @Override
    protected int setLayout() {
        return R.layout.welcome_layout;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        File file = new File(getFilesDir().getPath() + "/images/");
        file.mkdir();
        Intent intent = new Intent(this, NewsService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
        SharedPreferences sharedPreferences = getSharedPreferences("WelcomeImage", MODE_PRIVATE);
        String imagePath = sharedPreferences.getString("path", null);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            background.setImageBitmap(bitmap);
        }
        startAnimation(background, logo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unbindService(sc);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private void startAnimation(ImageView background, ImageView logo) {
        final ScaleAnimation bgScaleAnim = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f
                , Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5f);
        bgScaleAnim.setFillAfter(true);
        bgScaleAnim.setDuration(3000);
        bgScaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //do nothing
            }
        });
        final ScaleAnimation lgScaleAnim = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f
                , Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5f);
        final TranslateAnimation tvTranslateAnim = new TranslateAnimation(0, 0, 0, -40);
        final AnimationSet tvAnimationSet = new AnimationSet(true);
        tvAnimationSet.addAnimation(lgScaleAnim);
        tvAnimationSet.addAnimation(tvTranslateAnim);
        tvAnimationSet.setDuration(3000);
        tvAnimationSet.setFillAfter(true);

        background.startAnimation(bgScaleAnim);
        logo.startAnimation(tvAnimationSet);
    }


    private void startActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
