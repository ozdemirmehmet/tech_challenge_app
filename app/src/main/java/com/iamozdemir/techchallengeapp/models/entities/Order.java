/*
 * Created by Mehmet Ozdemir on 9/20/19 6:30 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/20/19 6:30 PM
 */

package com.iamozdemir.techchallengeapp.models.entities;

import com.google.gson.annotations.SerializedName;
import com.iamozdemir.techchallengeapp.models.enums.EOrderStatus;

import java.io.Serializable;

public class Order implements Serializable {

    @SerializedName("date")
    private String dayText;

    @SerializedName("month")
    private String monthText;

    @SerializedName("marketName")
    private String marketName;

    @SerializedName("orderName")
    private String orderName;

    @SerializedName("productPrice")
    private double orderPrice;

    @SerializedName("productState")
    private String orderStatusText;

    @SerializedName("productDetail")
    private Product product;

    //Aşağıdaki iki değişken listelemede kullanılacak
    private boolean isDetailsVisible = false;
    private int detailsViewHeight = 0;

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public String getMonthText() {
        return monthText;
    }

    public void setMonthText(String monthText) {
        this.monthText = monthText;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderStatusText() {
        return orderStatusText;
    }

    public void setOrderStatusText(String orderStatusText) {
        this.orderStatusText = orderStatusText;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isDetailsVisible() {
        return isDetailsVisible;
    }

    public void setDetailsVisible(boolean detailsVisible) {
        isDetailsVisible = detailsVisible;
    }

    public int getDetailsViewHeight() {
        return detailsViewHeight;
    }

    public void setDetailsViewHeight(int detailsViewHeight) {
        this.detailsViewHeight = detailsViewHeight;
    }

    public EOrderStatus getOrderStatus() {
        if (this.orderStatusText.equals("Yolda")) {
            return EOrderStatus.ON_ROAD;
        } else if (this.orderStatusText.equals("Hazırlanıyor")) {
            return EOrderStatus.PREPARING;
        } else if (this.orderStatusText.equals("Onay Bekliyor")) {
            return EOrderStatus.WAITING_CONFIRMATION;
        } else {
            return EOrderStatus.UNKNOWN;
        }
    }
}
