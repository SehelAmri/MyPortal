package com.example.myportal.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AlertDialog;

import com.example.myportal.Login;
import com.example.myportal.R;

import java.util.Locale;

public class LangSetup {
    Activity activity;
    String[] languageArray,languageInitialsArray;
    Locale locale;

    public LangSetup(Activity activity) {
        this.activity = activity;
        languageArray  = activity.getResources().getStringArray(R.array.languages);
        languageInitialsArray  = activity.getResources().getStringArray(R.array.languages_initials);
    }

    public void changeAppLanguage() {
        AlertDialog.Builder langOptBuilder = new AlertDialog.Builder(activity);
        langOptBuilder.setTitle(R.string.lg_adb_set_lang_title);
        langOptBuilder.setSingleChoiceItems(languageArray, -1, (dialog, i) -> {
            setLocale(languageInitialsArray[i]);
            activity.recreate();
            dialog.dismiss();
        });
        AlertDialog langOptDialog = langOptBuilder.create();
        langOptDialog.show();
    }
    private void setLocale(String lang) {
        locale = new Locale(lang);
        Locale.setDefault(locale);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        Configuration config = activity.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        activity.getResources().updateConfiguration(config, dm);
        //Save Data to Preferences
        SharedPreferences.Editor editor = activity.getSharedPreferences("Settings",activity.MODE_PRIVATE).edit();
        editor.putString("My Lang",lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences savedLang = activity.getSharedPreferences("Settings",activity.MODE_PRIVATE);
        String language = savedLang.getString("My Lang",Locale.getDefault().getLanguage());
        setLocale(language);
    }
}
