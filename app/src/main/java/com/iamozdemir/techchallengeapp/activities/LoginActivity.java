/*
 * Created by Mehmet Ozdemir on 9/18/19 12:04 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/18/19 11:58 AM
 */

package com.iamozdemir.techchallengeapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.iamozdemir.techchallengeapp.R;
import com.iamozdemir.techchallengeapp.fragments.login.LoginFragment;
import com.iamozdemir.techchallengeapp.fragments.login.SplashScreenFragment;
import com.iamozdemir.techchallengeapp.models.events.LoginPageEvent;
import com.iamozdemir.techchallengeapp.utils.Constants;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    //Class Constants
    private final String TAG = LoginActivity.class.getName();

    //Class Variables
    private boolean isUserLogin;
    private boolean isBackButtonPressed = false;
    private Handler backButtonControlHandler;
    private Runnable backButtonControlRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EventBus.getDefault().register(this);

        //Belli saniyede içinde iki defa üst üste geri butonuna basılırsa uygulamadan çıkış yapılacak
        //Bu durumu kontrol edecek yapılar aşağıda tanımlanmıştır.
        backButtonControlHandler = new Handler();
        backButtonControlRunnable = new Runnable() {
            @Override
            public void run() {
                isBackButtonPressed = false;
            }
        };

        //Giriş için kullanacağımız kullanıcı adı ve şifre bilgilerini native c++ kodu ile projeye ekledim.
        //Böylelikle uygulama apksı decompile edilse bile bu bilgiler erişilemez olacak.
        //Aşağıdaki kod bloğunda json text formatında c++ dosyasına eklenen bu bilgilerin elde edilmesiyle ilgili
        //gerekli işlemler yapılmaktadır.
        try {
            JSONObject keysObject = new JSONObject(getKeys());
            if (keysObject.has(Constants.LOGIN_USERNAME) && keysObject.has(Constants.LOGIN_PASSWORD)) {
                Hawk.delete(Constants.HAWK_PARAMETER_LOGIN_USERNAME);
                Hawk.put(Constants.HAWK_PARAMETER_LOGIN_USERNAME, keysObject.getString(Constants.LOGIN_USERNAME));

                Hawk.delete(Constants.HAWK_PARAMETER_LOGIN_PASSWORD);
                Hawk.put(Constants.HAWK_PARAMETER_LOGIN_PASSWORD, keysObject.getString(Constants.LOGIN_PASSWORD));
            } else {
                Log.d(TAG, "username and password are not received.");
                LoginActivity.this.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "JSON Exception: " + e.toString());
            LoginActivity.this.finish();
        }

        isUserLogin = Hawk.get(Constants.HAWK_PARAMETER_IS_USER_LOGIN, false);
        openSplashScreenFragment(isUserLogin);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (isBackButtonPressed) {
            super.onBackPressed();
        } else {
            isBackButtonPressed = true;
            Toast.makeText(this, getString(R.string.toast_text_exit), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        if (backButtonControlHandler != null && backButtonControlRunnable != null) {
            backButtonControlHandler.removeCallbacks(backButtonControlRunnable);
        }
        super.onPause();
    }

    private void openSplashScreenFragment(boolean isUserLogin) {
        SplashScreenFragment fragment = SplashScreenFragment.getInstance(isUserLogin);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commitAllowingStateLoss();
    }

    private void openLoginFragment() {
        LoginFragment fragment = LoginFragment.getInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commitAllowingStateLoss();
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(final LoginPageEvent event) {
        closeKeyboard();
        Object[] items = null;
        switch (event.getPage()) {
            case SPLASH_SCREEN:
                openSplashScreenFragment(isUserLogin);
                break;
            case LOGIN:
                openLoginFragment();
                break;
            case MAIN:
                openMainActivity();
                break;
        }
    }

    private void closeKeyboard() {
        View view1 = getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

    static {
        System.loadLibrary("native-lib");
    }

    public native String getKeys();
}
