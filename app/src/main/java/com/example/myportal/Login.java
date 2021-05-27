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
import android.provider.ContactsContract;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myportal.Retrofit.RetroService;
import com.example.myportal.Retrofit.RetrofitClient;
import com.example.myportal.Utils.LangSetup;
import com.google.android.material.tabs.TabLayout;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {
    Button logLogInBtn;
    String nameInput,passInput;
    EditText logPassword,logName,logEmail,logNameNo;
    TextView logHelp,edtUserOrEmail;
    LinearLayout linearUserName;
    LangSetup langSetup;
    TabLayout logTabLayout;
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
        tabLayoutFunc();
        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        retroService = retrofitClient.create(RetroService.class);
      /*  logHelp.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this,SignUp.class);
            startActivity(intent);
        });*/
        logPassword.setOnTouchListener(onPassTouchListener());
        logLogInBtn.setOnClickListener(v -> {
            if(logTabLayout.getSelectedTabPosition() == 0) {
                if(!logName.getText().toString().isEmpty() && !logPassword.getText().toString().isEmpty()
                &&!logNameNo.getText().toString().isEmpty()) {
                    loginUser(logName.getText().toString(),Integer.valueOf(logNameNo.getText().toString()),
                            logPassword.getText().toString());
                }else{
                    if(logName.getText().toString().isEmpty())
                    logName.setError(getString(R.string.valid_login_required));
                    else if(logPassword.getText().toString().isEmpty())
                        logPassword.setError(getString(R.string.valid_password_required));
                    else if(logNameNo.getText().toString().isEmpty())
                        logNameNo.setError(getString(R.string.valid_id_required));
                }
            }else if(logTabLayout.getSelectedTabPosition() == 1){
                if(!logEmail.getText().toString().isEmpty()
                   && !logPassword.getText().toString().isEmpty()) {
                    loginUserEmail(logEmail.getText().toString(),
                            logPassword.getText().toString());
                }else{
                    if(logEmail.getText().toString().isEmpty())
                        logEmail.setError(getString(R.string.valid_login_required));
                    else if(logPassword.getText().toString().isEmpty())
                        logPassword.setError(getString(R.string.valid_password_required));
                }
            }
        });
    }

    private void tabLayoutFunc() {
        logTabLayout.addTab(logTabLayout.newTab().setText(R.string.lg_username));
        logTabLayout.addTab(logTabLayout.newTab().setText(R.string.suEmail));
        logTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        logTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText() == getString(R.string.suEmail)){
                    logEmail.setVisibility(View.VISIBLE);
                    edtUserOrEmail.setText(R.string.suEmail);
                    linearUserName.setVisibility(View.GONE);
                }else if(tab.getText() == getString(R.string.lg_username)){
                    linearUserName.setVisibility(View.VISIBLE);
                    edtUserOrEmail.setText(R.string.lg_username);
                    logEmail.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loginUser(String name, Integer id, String password) {
       compositeDisposable.add(retroService.loginUser(name,id,password)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(response -> {
                   Toast.makeText(Login.this, "" + response, Toast.LENGTH_LONG).show();
                 }
               ));

    }
    private void loginUserEmail(String email, String password) {
        compositeDisposable.add(retroService.loginUserEmail(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            Toast.makeText(Login.this, "" + response, Toast.LENGTH_LONG).show();
                        }
                ));

    }

    public void initView(){
        logName = findViewById(R.id.logName);
        logNameNo = findViewById(R.id.logNameNo);
        logPassword = findViewById(R.id.logPassword);
        logLogInBtn = findViewById(R.id.logLogIn);
       // logHelp = findViewById(R.id.logHelp);
        logTabLayout = findViewById(R.id.log_tabLayout);
        edtUserOrEmail = findViewById(R.id.edtNameOrEmail);
        linearUserName = findViewById(R.id.linearUserName);
        logEmail = findViewById(R.id.logEmail);
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