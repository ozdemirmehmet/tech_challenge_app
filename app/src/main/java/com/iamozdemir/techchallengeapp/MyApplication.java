/*
 * Created by Mehmet Ozdemir on 9/18/19 3:19 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/18/19 11:48 AM
 */

package com.iamozdemir.techchallengeapp;

import android.app.Application;
import android.util.Log;

import com.iamozdemir.techchallengeapp.utils.ApiCall;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogInterceptor;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //ApiCall init
        ApiCall.createInstance(getApplicationContext());
        //Hawk init
        Hawk.init(this).setLogInterceptor(new LogInterceptor() {
            @Override public void onLog(String message) {
                Log.d("HAWK", message);
            }
        }).build();
    }
}
