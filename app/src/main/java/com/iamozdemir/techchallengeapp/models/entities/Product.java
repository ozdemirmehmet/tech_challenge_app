/*
 * Created by Mehmet Ozdemir on 9/20/19 6:42 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/20/19 6:42 PM
 */

package com.iamozdemir.techchallengeapp.models.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {

    @SerializedName("orderDetail")
    private String details;

    @SerializedName("summaryPrice")
    private double price;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
