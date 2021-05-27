package com.example.myportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashScreen extends AppCompatActivity {
Intent intent;
int SPLASH_SCREEN_TIMEOUT = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
           @Override
           public void run() {
               intent = new Intent(SplashScreen.this,StartMenu.class);
               startActivity(intent);
               finish();
           }
       },SPLASH_SCREEN_TIMEOUT);
    }
}