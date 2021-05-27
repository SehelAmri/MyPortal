package com.example.myportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myportal.Utils.LangSetup;

public class StartMenu extends AppCompatActivity {
    LangSetup langSetup;
    TextView sm_log_in,sm_sign_up;
    Button smLangBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        langSetup = new LangSetup(StartMenu.this);
        langSetup.loadLocale();
        setContentView(R.layout.activity_start_menu);
        initView();
        smLangBtn.setOnClickListener(v -> {
            langSetup.changeAppLanguage();
        });
        sm_log_in.setOnClickListener(v -> {
            Intent intent = new Intent(StartMenu.this,Login.class);
            startActivity(intent);
        });
        sm_sign_up.setOnClickListener(v -> {
            Intent intent = new Intent(StartMenu.this,SignUp.class);
            startActivity(intent);
        });
    }
    public void initView(){
        smLangBtn = findViewById(R.id.lg_langDropDownBtn);
        sm_log_in = findViewById(R.id.sm_log_in);
        sm_sign_up = findViewById(R.id.sm_sign_in);
    }

}