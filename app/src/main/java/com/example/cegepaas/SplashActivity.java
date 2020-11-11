package com.example.cegepaas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * SplashActivity is used to have the splash screen on the opening screen.
 */
public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    Animation animFadeIn;
    LinearLayout linearLayout;

    /**
     * onCreate method is the main method that will trigger when the activity starts.
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        animFadeIn.setAnimationListener(this);
        linearLayout = findViewById(R.id.splash_layout);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setAnimation(animFadeIn);
    }

    /**
     * used to have the animation starts on the screen when the splach screen pops up.
     * @param animation animation object.
     */
    @Override
    public void onAnimationStart(Animation animation) {

    }

    /**
     *  used to have the animation ends on the screen when the splach screen pops up.
     * @param animation animation object.
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        startActivity(new Intent(SplashActivity.this, UserSelectActivity.class));
        this.finish();
    }

    /**
     * used to repeat the animation..
     * @param animation animation object.
     */
    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}