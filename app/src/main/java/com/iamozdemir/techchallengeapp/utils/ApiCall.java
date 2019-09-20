/*
 * Created by Mehmet Ozdemir on 9/20/19 8:37 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/24/18 5:03 PM
 */

package com.iamozdemir.techchallengeapp.utils;

import android.content.Context;

import com.iamozdemir.techchallengeapp.R;
import com.iamozdemir.techchallengeapp.models.interfaces.ApiServicesInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {

    private static ApiCall instance;

    private static ApiServicesInterface apiServicesInterface;

    public final static void createInstance(Context context) {
        if (instance == null) {
            instance = new ApiCall(context);
        }
    }

    private ApiCall(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();
        Retrofit locationboxServicesRetrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.api_base_url))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServicesInterface = locationboxServicesRetrofit.create(ApiServicesInterface.class);
    }

    public static ApiServicesInterface getApiServicesInterface() {
        return apiServicesInterface;
    }
}
