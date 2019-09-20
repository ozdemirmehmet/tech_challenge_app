/*
 * Created by Mehmet Ozdemir on 9/20/19 10:44 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 6/4/18 10:20 AM
 */

package com.iamozdemir.techchallengeapp.layouts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iamozdemir.techchallengeapp.R;

public class MyDialog extends Dialog implements View.OnClickListener {

    //Button click listeners
    private OnMyDialogClickListener positiveButtonClickListener;
    private OnMyDialogClickListener negativeButtonClickListener;

    //Widgets
    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button positiveButton;
    private Button negativeButton;

    //Variables
    private String dialogTitle;
    private String dialogDescription;
    private String dialogPositiveButtonText;
    private String dialogNegativeButtonText;
    private boolean isShowDialogNegativeButton;

    public interface OnMyDialogClickListener {
        void onClick();
    }

    public MyDialog(Context context) {
        super(context, R.style.MyDialogStyle);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);

        titleTextView = findViewById(R.id.text_view_title);
        descriptionTextView = findViewById(R.id.text_view_description);
        positiveButton = findViewById(R.id.button_positive);
        negativeButton = findViewById(R.id.button_negative);

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        setTitleText(dialogTitle);
        setDescriptionText(dialogDescription);
        setNegativeButtonText(dialogNegativeButtonText);
        setPositiveButtonText(dialogPositiveButtonText);
        showNegativeButton(isShowDialogNegativeButton);
    }

    public MyDialog setTitleText(String text) {
        dialogTitle = text;
        if (titleTextView != null && dialogTitle != null) {
            titleTextView.setText(dialogTitle);
        }
        return this;
    }

    public MyDialog setDescriptionText(String text) {
        dialogDescription = text;
        if (descriptionTextView != null && dialogDescription != null) {
            descriptionTextView.setText(dialogDescription);
        }
        return this;
    }

    public MyDialog showNegativeButton(boolean isShow) {
        isShowDialogNegativeButton = isShow;
        if (negativeButton != null) {
            negativeButton.setVisibility(isShowDialogNegativeButton ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public MyDialog setNegativeButtonText(String text) {
        dialogNegativeButtonText = text;
        if (negativeButton != null && dialogNegativeButtonText != null) {
            showNegativeButton(true);
            negativeButton.setText(dialogNegativeButtonText);
        }
        return this;
    }

    public MyDialog setPositiveButtonText(String text) {
        dialogPositiveButtonText = text;
        if (positiveButton != null && dialogPositiveButtonText != null) {
            positiveButton.setText(dialogPositiveButtonText);
        }
        return this;
    }

    public MyDialog setNegativeButtonClickListener(OnMyDialogClickListener listener) {
        negativeButtonClickListener = listener;
        return this;
    }

    public MyDialog setPositiveButtonClickListener(OnMyDialogClickListener listener) {
        positiveButtonClickListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_negative) {
            if (negativeButtonClickListener != null) {
                negativeButtonClickListener.onClick();
            }
            dismiss();
        } else if (v.getId() == R.id.button_positive) {
            if (positiveButtonClickListener != null) {
                positiveButtonClickListener.onClick();
            }
            dismiss();
        }
    }
}
