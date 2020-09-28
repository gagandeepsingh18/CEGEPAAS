package com.example.cegepaas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener{
    Animation animFadeIn;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        animFadeIn = AnimationUtils.loadAnimation(this,R.anim.anim_fade_in);
        animFadeIn.setAnimationListener(this);
        linearLayout = findViewById(R.id.splash_layout);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setAnimation(animFadeIn);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}