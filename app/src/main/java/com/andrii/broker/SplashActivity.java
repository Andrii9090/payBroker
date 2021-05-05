package com.andrii.broker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Context context = this;

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        };

        handler.postDelayed(r, 3000);
    }
}