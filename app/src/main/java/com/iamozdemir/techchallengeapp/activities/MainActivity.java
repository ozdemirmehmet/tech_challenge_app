/*
 * Created by Mehmet Ozdemir on 9/18/19 12:04 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/18/19 11:36 AM
 */

package com.iamozdemir.techchallengeapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.iamozdemir.techchallengeapp.R;
import com.iamozdemir.techchallengeapp.fragments.main.OrderListFragment;
import com.iamozdemir.techchallengeapp.models.events.MainPageEvent;
import com.iamozdemir.techchallengeapp.utils.Constants;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        openOrderListFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void openOrderListFragment() {
        OrderListFragment fragment = OrderListFragment.getInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commitAllowingStateLoss();
    }

    private void openLoginActivity() {
        //Otomatik login sistemini devre dışı bırakmak için boolean değişken cihazdan siliniyor.
        Hawk.delete(Constants.HAWK_PARAMETER_IS_REMEMBER_ME);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    //EventBus tarafından MainPageEvent objesi ile yayın yapılması halinde tetiklenecek method.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(final MainPageEvent event) {
        closeKeyboard();
        Object[] items = null;
        switch (event.getPage()) {
            case LOGIN:
                openLoginActivity();
                break;
            case ORDER_LIST:
                openOrderListFragment();
                break;
        }
    }

    //Klavye eğer açıksa kapatmak için kullanılan method.
    private void closeKeyboard() {
        View view1 = getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }
}
