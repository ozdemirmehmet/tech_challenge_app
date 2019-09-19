/*
 * Created by Mehmet Ozdemir on 9/18/19 12:04 PM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 9/18/19 12:03 PM
 */

package com.iamozdemir.techchallengeapp.models.events;

public class LoginPageEvent {

    private LoginPages page;
    private Object[] pageItems;

    public LoginPageEvent(LoginPages page, Object... pageItems) {
        this.page = page;
        this.pageItems = pageItems;
    }

    public LoginPages getPage() {
        return page;
    }

    public Object[] getPageItems() {
        return pageItems;
    }

    public enum LoginPages {
        SPLASH_SCREEN,
        LOGIN,
        MAIN
    }
}
