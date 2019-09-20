/*
 * Created by Mehmet Ozdemir on 9/18/19 12:11 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/18/19 12:09 PM
 */

package com.iamozdemir.techchallengeapp.fragments.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iamozdemir.techchallengeapp.R;
import com.iamozdemir.techchallengeapp.models.events.LoginPageEvent;
import com.iamozdemir.techchallengeapp.utils.Constants;

import org.greenrobot.eventbus.EventBus;

public class SplashScreenFragment extends Fragment {

    //Class Constants
    private final String TAG = SplashScreenFragment.class.getName();

    //Class Variables
    private boolean isRememberMe;
    private Handler waitHandler;
    private Runnable waitRunnable;

    private SplashScreenFragment() {
        // Required empty public constructor
    }

    public static SplashScreenFragment getInstance(boolean isRememberMe) {
        SplashScreenFragment fragment = new SplashScreenFragment();
        fragment.isRememberMe = isRememberMe;
        fragment.waitHandler = new Handler();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        waitRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRememberMe) {
                    EventBus.getDefault().post(new LoginPageEvent(LoginPageEvent.LoginPages.MAIN));
                } else {
                    EventBus.getDefault().post(new LoginPageEvent(LoginPageEvent.LoginPages.LOGIN));
                }
            }
        };
    }

    @Override
    public void onPause() {
        if (waitHandler != null && waitRunnable != null) {
            waitHandler.removeCallbacks(waitRunnable);
        }
        Log.d(TAG, "Splash Screen duration stopped");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Splash Screen duration started");
        waitHandler.postDelayed(waitRunnable, Constants.SPLASH_SCREEN_WAITING_DURATION);
    }
}
