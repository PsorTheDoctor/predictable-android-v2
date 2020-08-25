package com.wolkowycki.predictable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private static int TIME_OUT = 2000;
    private Animation logoAnim, titleAnim;
    private ImageView logo;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        titleAnim = AnimationUtils.loadAnimation(this, R.anim.title_animation);

        logo = findViewById(R.id.splash_logo);
        title = findViewById(R.id.splash_title);

        logo.setAnimation(logoAnim);
        title.setAnimation(titleAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);
    }
}
