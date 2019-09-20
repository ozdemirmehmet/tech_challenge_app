/*
 * Created by Mehmet Ozdemir on 9/19/19 11:41 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/19/19 11:41 PM
 */

package com.iamozdemir.techchallengeapp.fragments.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iamozdemir.techchallengeapp.R;
import com.iamozdemir.techchallengeapp.activities.LoginActivity;
import com.iamozdemir.techchallengeapp.adapters.OrderListAdapter;
import com.iamozdemir.techchallengeapp.layouts.MyDialog;
import com.iamozdemir.techchallengeapp.models.entities.Order;
import com.iamozdemir.techchallengeapp.models.events.MainPageEvent;
import com.iamozdemir.techchallengeapp.utils.ApiCall;
import com.iamozdemir.techchallengeapp.utils.Constants;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderListFragment extends Fragment {

    //Class Constants
    private final String TAG = OrderListFragment.class.getName();

    //Class Variables
    private OrderListAdapter orderListAdapter;
    private Dialog dialog = null;

    //Widgets
    private View progressContainerView;
    private View progressBar;
    private RecyclerView ordersRecyclerView;
    private Button exitButton;

    private OrderListFragment() {
        // Required empty public constructor
    }

    public static OrderListFragment getInstance() {
        OrderListFragment fragment = new OrderListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        progressContainerView = view.findViewById(R.id.view_progress_container);
        progressBar = view.findViewById(R.id.progress_bar);
        ordersRecyclerView = view.findViewById(R.id.recycler_view_orders);

        exitButton = view.findViewById(R.id.button_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                dialog = new MyDialog(getActivity())
                        .setTitleText(getString(R.string.dialog_header_text_exit))
                        .setDescriptionText(getString(R.string.dialog_description_text_exit))
                        .setPositiveButtonText(getString(R.string.button_text_yes))
                        .setNegativeButtonText(getString(R.string.button_text_no))
                        .setPositiveButtonClickListener(new MyDialog.OnMyDialogClickListener() {
                            @Override
                            public void onClick() {
                                EventBus.getDefault().post(new MainPageEvent(MainPageEvent.MainPages.LOGIN));
                            }
                        })
                        .setNegativeButtonClickListener(new MyDialog.OnMyDialogClickListener() {
                            @Override
                            public void onClick() {

                            }
                        });
                dialog.show();
            }
        });
    }

    private void getOrderList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(true);
            }
        });
        ApiCall.getApiServicesInterface().getOrderList()
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                            }
                        });
                        orderListAdapter = new OrderListAdapter(getContext(), response.body());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        ordersRecyclerView.setLayoutManager(mLayoutManager);
                        ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        ordersRecyclerView.setAdapter(orderListAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        Log.d(TAG, "retrofit onFailure reason: " + t.toString());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                dismissDialog();
                                dialog = new MyDialog(getActivity())
                                        .setTitleText(getString(R.string.dialog_header_text_error))
                                        .setDescriptionText(getString(R.string.dialog_description_text_error))
                                        .setPositiveButtonText(getString(R.string.button_text_try_again))
                                        .showNegativeButton(false)
                                        .setPositiveButtonClickListener(new MyDialog.OnMyDialogClickListener() {
                                            @Override
                                            public void onClick() {
                                                EventBus.getDefault().post(new MainPageEvent(MainPageEvent.MainPages.LOGIN));
                                            }
                                        });
                                dialog.setCancelable(false);
                                dialog.show();
                            }
                        });
                    }
                });
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

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    @Override
    public void onPause() {
        dismissDialog();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderList();
    }
}
