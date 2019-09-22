/*
 * Created by Mehmet Ozdemir on 9/20/19 6:28 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/20/19 6:28 PM
 */

package com.iamozdemir.techchallengeapp.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iamozdemir.techchallengeapp.R;
import com.iamozdemir.techchallengeapp.models.entities.Order;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter {

    //Class Constants
    private final long DETAILS_VIEW_ANIMATION_DURATION = 1 * 400;

    //Class Variables
    private Context context;
    private List<Order> orderList;
    private Calendar calendar;
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");

    public OrderListAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.calendar = Calendar.getInstance();
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {
        private TextView dayTextView;
        private TextView monthTextView;
        private TextView marketNameTextView;
        private TextView orderNameTextView;
        private View statusView;
        private TextView statusTextView;
        private TextView priceTextView;
        private View productDetailsView;
        private TextView productDetailsTextView;
        private TextView productPriceTextView;


        public OrderListViewHolder(View view) {
            super(view);
            dayTextView = view.findViewById(R.id.text_view_day);
            monthTextView = view.findViewById(R.id.text_view_month);
            marketNameTextView = view.findViewById(R.id.text_view_market_name);
            orderNameTextView = view.findViewById(R.id.text_view_order_name);
            statusView = view.findViewById(R.id.view_status);
            statusTextView = view.findViewById(R.id.text_view_status);
            priceTextView = view.findViewById(R.id.text_view_price);
            productDetailsView = view.findViewById(R.id.view_product_details);
            productDetailsTextView = view.findViewById(R.id.text_view_product_details);
            productPriceTextView = view.findViewById(R.id.text_view_product_price);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template_order_list, parent, false);
        return new OrderListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final OrderListViewHolder orderListViewHolder = (OrderListViewHolder) holder;

        final Order order = orderList.get(position);
        if (order != null) {
            calendar.set(Calendar.MONTH, Integer.parseInt(order.getMonthText()) - 1);
            orderListViewHolder.dayTextView.setText(order.getDayText());
            orderListViewHolder.monthTextView.setText(monthFormat.format(calendar.getTime()));
            orderListViewHolder.marketNameTextView.setText(order.getMarketName());
            orderListViewHolder.orderNameTextView.setText(order.getOrderName());
            switch (order.getOrderStatus()) {
                case ON_ROAD:
                    orderListViewHolder.statusTextView.setText(context.getString(R.string.info_text_on_road));
                    orderListViewHolder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_on_road));
                    orderListViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.color_on_road));
                    break;
                case PREPARING:
                    orderListViewHolder.statusTextView.setText(context.getString(R.string.info_text_preparing));
                    orderListViewHolder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_preparing));
                    orderListViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.color_preparing));
                    break;
                case WAITING_CONFIRMATION:
                    orderListViewHolder.statusTextView.setText(context.getString(R.string.info_text_waiting_confirmation));
                    orderListViewHolder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_waiting_confirmation));
                    orderListViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.color_waiting_confirmation));
                    break;
                default:
                    orderListViewHolder.statusTextView.setText(context.getString(R.string.info_text_unknown_status));
                    orderListViewHolder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_unknown_status));
                    orderListViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.color_unknown_status));
                    break;
            }
            orderListViewHolder.priceTextView.setText("₺" + order.getOrderPrice());
            orderListViewHolder.productDetailsTextView.setText(order.getProduct().getDetails());
            orderListViewHolder.productPriceTextView.setText("₺" + order.getProduct().getPrice());

            if (order.getDetailsViewHeight() == 0) {//item yüksekliği daha önce hesaplanmadıysa hasaplanacak.
                ViewTreeObserver vto = orderListViewHolder.productDetailsView.getViewTreeObserver();
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        orderListViewHolder.productDetailsView.getViewTreeObserver().removeOnPreDrawListener(this);
                        orderList.get(position).setDetailsViewHeight(orderListViewHolder.productDetailsView.getMeasuredHeight());
                        hideProductDetails(orderListViewHolder.productDetailsView, order);
                        return false;
                    }
                });
            } else {//item' ın son haline, detay açık/kapalı, göre gelmesi sağlandı.
                if (order.isDetailsVisible()) {
                    showProductDetails(orderListViewHolder.productDetailsView, order);
                } else {
                    hideProductDetails(orderListViewHolder.productDetailsView, order);
                }
            }

            orderListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (order.isDetailsVisible()) {
                        hideProductDetails(orderListViewHolder.productDetailsView, order);
                        orderList.get(position).setDetailsVisible(false);
                    } else {
                        showProductDetails(orderListViewHolder.productDetailsView, order);
                        orderList.get(position).setDetailsVisible(true);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void showProductDetails(final View view, Order order) {
        ValueAnimator scaleAnimator = ValueAnimator.ofInt(0, order.getDetailsViewHeight());
        scaleAnimator.setDuration(DETAILS_VIEW_ANIMATION_DURATION);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                view.requestLayout();
            }
        });
        scaleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d("aaa", "anEnd");
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(DETAILS_VIEW_ANIMATION_DURATION);
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(view, "rotationX", 90, -15, 15, 0),
                        ObjectAnimator.ofFloat(view, "alpha", 0f, 1)
                );
                animatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        scaleAnimator.start();
    }

    private void hideProductDetails(final View view, final Order order) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(DETAILS_VIEW_ANIMATION_DURATION);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotationX", 0, 90),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        );
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ValueAnimator scaleAnimator = ValueAnimator.ofInt(order.getDetailsViewHeight(), 0);
                scaleAnimator.setDuration(DETAILS_VIEW_ANIMATION_DURATION);
                scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        view.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                        view.requestLayout();
                    }
                });
                scaleAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }
}
