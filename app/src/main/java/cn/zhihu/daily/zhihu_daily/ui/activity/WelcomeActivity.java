package cn.zhihu.daily.zhihu_daily.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.zhihu.daily.zhihu_daily.R;
import cn.zhihu.daily.zhihu_daily.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.logo)
    ImageView logo;

    @Override
    protected int setLayout() {
        return R.layout.welcome_layout;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        startAnimation(background, logo);
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

        background.startAnimation(bgScaleAnim);
        logo.startAnimation(lgScaleAnim);
    }


    private void startActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
