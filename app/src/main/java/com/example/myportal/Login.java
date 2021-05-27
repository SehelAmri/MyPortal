package com.example.myportal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myportal.Retrofit.RetroService;
import com.example.myportal.Retrofit.RetrofitClient;
import com.example.myportal.Utils.LangSetup;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {
    Button logLogInBtn;
    String nameInput,passInput;
    EditText logPassword,logName;
    TextView logHelp;
    LangSetup langSetup;
    Boolean isVisible = true;
   CompositeDisposable compositeDisposable = new CompositeDisposable();
    RetroService retroService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.lg_log_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_login);
        initView();
        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        retroService = retrofitClient.create(RetroService.class);
        logHelp.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this,SignUp.class);
            startActivity(intent);
        });

        logPassword.addTextChangedListener(textWatcher());
        logName.addTextChangedListener(textWatcher());
        logPassword.setOnTouchListener(onPassTouchListener());
        logLogInBtn.setOnClickListener(v -> {
               loginUser(logName.getText().toString(),
                       logPassword.getText().toString());
        });
    }

    private void loginUser(String name, String password) {
       compositeDisposable.add(retroService.loginUser(name,password)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(response -> {
                   Toast.makeText(Login.this, "" + response, Toast.LENGTH_LONG).show();
                 }
               ));

    }

    public void initView(){
        logName = findViewById(R.id.logName);
        logPassword = findViewById(R.id.logPassword);
        logLogInBtn = findViewById(R.id.logLogIn);
        logHelp = findViewById(R.id.logHelp);
    }

    public TextWatcher textWatcher(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameInput = logName.getText().toString().trim();
                passInput = logPassword.getText().toString().trim();
                logLogInBtn.setClickable(!nameInput.isEmpty() && !passInput.isEmpty());
                logLogInBtn.setEnabled(!nameInput.isEmpty() && !passInput.isEmpty());
                if(!nameInput.isEmpty() && !passInput.isEmpty()) {
                    logLogInBtn.setBackground(getResources().getDrawable(R.drawable.enabled_button));
                }else {
                    logLogInBtn.setBackground(getResources().getDrawable(R.drawable.disabled_button));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }
    public View.OnTouchListener onPassTouchListener(){
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (logPassword.getRight() - logPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(!isVisible) {
                            int start = logPassword.getSelectionStart();
                            int end = logPassword.getSelectionEnd();
                            logPassword.setCompoundDrawablesWithIntrinsicBounds(null, null
                                    , getResources().getDrawable(R.drawable.ic_baseline_visibility_24), null);
                            logPassword.setTransformationMethod(new PasswordTransformationMethod());
                            logPassword.setSelection(start,end);
                            isVisible = true;
                        }else if(isVisible){
                            int start = logPassword.getSelectionStart();
                            int end = logPassword.getSelectionEnd();
                            logPassword.setCompoundDrawablesWithIntrinsicBounds(null, null
                                    , getResources().getDrawable(R.drawable.ic_baseline_visibility_off_24), null);
                            logPassword.setTransformationMethod(null);
                            logPassword.setSelection(start,end);
                            isVisible = false;
                        }
                        return true;
                    }
                }
                return false;
            }
        };
        return onTouchListener;
    }
}