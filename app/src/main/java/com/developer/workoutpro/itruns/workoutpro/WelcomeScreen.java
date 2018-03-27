package com.developer.workoutpro.itruns.workoutpro;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

public class WelcomeScreen extends AppCompatActivity{

    private static int delay = 5000;
    private static int SPLASH_TIME_OUT = delay;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(delay);
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, delay); // see this max value coming back here, we animate towards that value
        animation.setDuration (delay); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                progressBar.clearAnimation();
                Intent homeIntent = new Intent(WelcomeScreen.this, MainClass.class);
                startActivity(homeIntent);
                finish();
            }

        },SPLASH_TIME_OUT);
    }

}
