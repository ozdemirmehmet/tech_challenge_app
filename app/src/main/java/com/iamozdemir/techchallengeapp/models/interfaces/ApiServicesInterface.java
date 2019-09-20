/*
 * Created by Mehmet Ozdemir on 9/20/19 8:35 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/29/18 12:30 PM
 */

package com.iamozdemir.techchallengeapp.models.interfaces;

import com.iamozdemir.techchallengeapp.models.entities.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServicesInterface {

    @GET("/")
    Call<List<Order>> getOrderList();
}
