/*
 * Created by Mehmet Ozdemir on 9/18/19 12:11 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/18/19 12:01 PM
 */

package com.iamozdemir.techchallengeapp.fragments.login;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iamozdemir.techchallengeapp.R;
import com.iamozdemir.techchallengeapp.models.events.LoginPageEvent;
import com.iamozdemir.techchallengeapp.utils.Constants;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    //Class Constants
    private final String TAG = LoginFragment.class.getName();
    private final long WAIT_DURATION = 1 * 1000;

    //Class Variables
    private String username;
    private String password;
    private Handler waitHandler;
    private Runnable waitRunable;

    //Widgets
    private View progressContainerView;
    private View progressBar;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private SwitchCompat rememberMeSwitch;
    private Button loginButton;

    private LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment getInstance() {
        LoginFragment fragment = new LoginFragment();
        fragment.waitHandler = new Handler();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        //kullanıcı adı ve şifre bilgileri hawk kütüphanesi yardımıyla elde ediliyor.
        username = Hawk.get(Constants.HAWK_PARAMETER_LOGIN_USERNAME);
        password = Hawk.get(Constants.HAWK_PARAMETER_LOGIN_PASSWORD);

        progressContainerView = view.findViewById(R.id.view_progress_container);
        progressBar = view.findViewById(R.id.progress_bar);
        usernameEditText = view.findViewById(R.id.edit_text_username);
        passwordEditText = view.findViewById(R.id.edit_text_password);
        rememberMeSwitch = view.findViewById(R.id.switch_remember_me);
        loginButton = view.findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });
    }

    private void checkLogin() {
        usernameEditText.setError(null);
        passwordEditText.setError(null);

        boolean loginControl = true;
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if (username.isEmpty()) {
            loginControl = false;
            usernameEditText.setError(getString(R.string.error_text_blank_field));
        }

        if (password.isEmpty()) {
            loginControl = false;
            passwordEditText.setError(getString(R.string.error_text_blank_field));
        }

        if (loginControl) {
            showProgress(true);
            waitRunable = new Runnable() {
                @Override
                public void run() {
                    showProgress(false);
                    if (LoginFragment.this.username.equals(username) && LoginFragment.this.password.equals(password)) {
                        Log.d(TAG, "success login");
                        if (rememberMeSwitch.isChecked()) {
                            Log.d(TAG, "remember me stored");
                            //Otomatik login sisteminin çalışması için, boolean değişken hawk kütüphanesi
                            //yardımı ile cihaza kaydedildi.
                            Hawk.put(Constants.HAWK_PARAMETER_IS_REMEMBER_ME, true);
                        }
                        EventBus.getDefault().post(new LoginPageEvent(LoginPageEvent.LoginPages.MAIN));
                    } else {
                        Log.d(TAG, "login error");
                        Toast.makeText(getActivity(), getString(R.string.toast_text_login_error), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            waitHandler.postDelayed(waitRunable, WAIT_DURATION);
        }
    }

    private void showProgress(boolean isVisible) {
        if (isVisible) {
            progressContainerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressContainerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        //onPause durumu oluşması halinde devam eden handler yapılarının callback'leri kaldırılarak olası crash'lerin önüne geçilmiştir.
        if (waitHandler != null && waitRunable != null) {
            waitHandler.removeCallbacks(waitRunable);
        }
        super.onPause();
    }
}
