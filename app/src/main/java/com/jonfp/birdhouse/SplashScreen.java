package com.jonfp.birdhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        LottieAnimationView animationView = findViewById(R.id.lottieAnimationView);
        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Start your main activity or any other desired activity after animation ends
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish(); // Finish the splash activity so it's not in the back stack
            }
        });
    }
}